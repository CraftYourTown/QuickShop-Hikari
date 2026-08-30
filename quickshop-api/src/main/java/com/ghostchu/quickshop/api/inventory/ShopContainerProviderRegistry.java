package com.ghostchu.quickshop.api.inventory;

import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Stores the block-container providers available to QuickShop.
 *
 * <p>The highest priority provider wins. Providers with equal priority retain registration order,
 * making resolution deterministic across repeated lookups.</p>
 */
public final class ShopContainerProviderRegistry {

  private static final Comparator<Entry> RESOLUTION_ORDER = Comparator
          .comparingInt((Entry entry) -> entry.provider().priority())
          .reversed()
          .thenComparingLong(Entry::sequence);

  private final List<Entry> providers = new CopyOnWriteArrayList<>();
  private final AtomicLong sequence = new AtomicLong();

  /**
   * Registers a provider owned by a plugin.
   *
   * @param plugin the provider owner
   * @param provider the provider
   */
  public void register(@NotNull final Plugin plugin, @NotNull final ShopContainerProvider provider) {

    providers.add(new Entry(plugin, provider, sequence.getAndIncrement()));
  }

  /**
   * Removes every provider registered by a plugin.
   *
   * @param plugin the provider owner
   */
  public void unregister(@NotNull final Plugin plugin) {

    providers.removeIf(entry -> entry.plugin().equals(plugin));
  }

  /**
   * Resolves the provider responsible for a block.
   *
   * @param block the candidate block
   * @return the selected provider, or {@code null} when unsupported
   */
  @Nullable
  public ShopContainerProvider resolve(@NotNull final Block block) {

    return providers.stream()
            .sorted(RESOLUTION_ORDER)
            .map(Entry::provider)
            .filter(provider -> provider.supports(block))
            .findFirst()
            .orElse(null);
  }

  /**
   * Finds the provider associated with an inventory-wrapper manager.
   *
   * @param manager the wrapper manager
   * @return the provider, or {@code null} when none is registered
   */
  @Nullable
  public ShopContainerProvider find(@NotNull final InventoryWrapperManager manager) {

    return providers.stream()
            .sorted(RESOLUTION_ORDER)
            .map(Entry::provider)
            .filter(provider -> provider.getInventoryWrapperManager() == manager
                    || provider.getInventoryWrapperManager().equals(manager))
            .findFirst()
            .orElse(null);
  }

  private record Entry(
          @NotNull Plugin plugin,
          @NotNull ShopContainerProvider provider,
          long sequence
  ) {
  }
}
