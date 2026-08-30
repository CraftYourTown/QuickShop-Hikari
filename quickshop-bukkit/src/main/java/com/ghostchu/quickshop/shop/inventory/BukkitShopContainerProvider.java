package com.ghostchu.quickshop.shop.inventory;

import com.ghostchu.quickshop.api.inventory.InventoryWrapper;
import com.ghostchu.quickshop.api.inventory.InventoryWrapperManager;
import com.ghostchu.quickshop.api.inventory.ShopContainerProvider;
import com.ghostchu.quickshop.util.Util;
import org.bukkit.block.Block;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Resolves the ordinary Bukkit inventory blocks supported by QuickShop configuration.
 */
public final class BukkitShopContainerProvider implements ShopContainerProvider {

  private final InventoryWrapperManager manager;

  public BukkitShopContainerProvider(@NotNull final InventoryWrapperManager manager) {

    this.manager = manager;
  }

  @Override
  public int priority() {

    return Integer.MIN_VALUE;
  }

  @Override
  public boolean supports(@NotNull final Block block) {

    return Util.isShoppables(block.getType()) && block.getState(false) instanceof InventoryHolder;
  }

  @Override
  public @NotNull InventoryWrapperManager getInventoryWrapperManager() {

    return manager;
  }

  @Override
  public @NotNull InventoryWrapper createInventoryWrapper(@NotNull final Block block) throws IllegalArgumentException {

    if(!(block.getState(false) instanceof InventoryHolder holder)) {
      throw new IllegalArgumentException("Target block is not a Bukkit inventory holder");
    }
    return new BukkitInventoryWrapper(holder.getInventory());
  }

  @Override
  public boolean allowsBlockMarker() {

    return true;
  }
}
