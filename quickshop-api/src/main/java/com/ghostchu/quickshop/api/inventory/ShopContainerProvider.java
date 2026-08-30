package com.ghostchu.quickshop.api.inventory;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Resolves a placed block to the inventory wrapper used by a QuickShop.
 *
 * <p>Providers are consulted by priority before QuickShop's Bukkit container fallback. A provider
 * must not load chunks while checking support or creating a wrapper.</p>
 */
public interface ShopContainerProvider {

  /**
   * Gets this provider's resolution priority.
   *
   * @return the priority; greater values are resolved first
   */
  default int priority() {

    return 0;
  }

  /**
   * Checks whether this provider owns the supplied block.
   *
   * @param block the candidate shop block
   * @return whether this provider supports it
   */
  boolean supports(@NotNull Block block);

  /**
   * Gets the manager used to persist and later locate this provider's wrappers.
   *
   * @return the registered wrapper manager
   */
  @NotNull
  InventoryWrapperManager getInventoryWrapperManager();

  /**
   * Creates a wrapper for the supplied block.
   *
   * @param block the supported block
   * @return its inventory wrapper
   * @throws IllegalArgumentException when the block is no longer supported
   */
  @NotNull
  InventoryWrapper createInventoryWrapper(@NotNull Block block) throws IllegalArgumentException;

  /**
   * Performs a provider-specific access check during shop creation.
   *
   * @param player the player creating the shop
   * @param block the supported block
   * @return whether creation is allowed
   */
  default boolean canCreateShop(@NotNull final Player player, @NotNull final Block block) {

    return true;
  }

  /**
   * Resolves the item stored on a newly created shop.
   *
   * <p>Virtual containers may use this hook to replace the player's requested item with the
   * container's canonical registered item. The returned stack controls item matching for the
   * shop, while its amount controls the trade stack size.</p>
   *
   * @param block the supported shop block
   * @param requestedItem the item selected by the player
   * @return the item QuickShop should store on the shop
   */
  @NotNull
  default ItemStack resolveShopItem(
          @NotNull final Block block,
          @NotNull final ItemStack requestedItem) {

    return requestedItem.clone();
  }

  /**
   * Controls whether QuickShop may write its optimization marker to the physical block state.
   *
   * <p>External providers default to {@code false} because updating a foreign tile state may
   * overwrite plugin-owned data such as player-head textures.</p>
   *
   * @return whether the block marker may be written
   */
  default boolean allowsBlockMarker() {

    return false;
  }
}
