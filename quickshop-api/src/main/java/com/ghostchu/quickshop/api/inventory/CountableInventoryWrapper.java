package com.ghostchu.quickshop.api.inventory;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * InventoryWrapper for countable Inventory
 */
public interface CountableInventoryWrapper extends InventoryWrapper {

  /**
   * Counting the items
   *
   * @param predicate {@link ItemPredicate}
   *
   * @return the items
   */
  int countItem(@NotNull ItemPredicate predicate);

  /**
   * Counts items while also exposing the immutable template requested by QuickShop.
   *
   * <p>Specialized virtual inventories may need the template to calculate exact compatibility.
   * Existing wrappers retain their predicate-only behavior.</p>
   *
   * @param template the requested item template
   * @param predicate the configured item matcher
   * @return the matching item count
   */
  default int countItem(@NotNull final ItemStack template, @NotNull final ItemPredicate predicate) {

    return countItem(predicate);
  }

  /**
   * Counting the spaces
   *
   * @param predicate {@link ItemPredicate}
   *
   * @return the space
   */
  int countSpace(@NotNull ItemPredicate predicate);

  /**
   * Counts capacity while also exposing the immutable template requested by QuickShop.
   *
   * @param template the item QuickShop intends to insert
   * @param predicate the configured item matcher
   * @return the available capacity
   */
  default int countSpace(@NotNull final ItemStack template, @NotNull final ItemPredicate predicate) {

    return countSpace(predicate);
  }

  /**
   * The item predicate for calculating
   */
  interface ItemPredicate {

    /**
     * Check if the item match the predicate
     *
     * @param input the item want to check
     *
     * @return if the item match the predicate
     */
    boolean isMatch(@NotNull ItemStack input);
  }
}
