package com.frahhs.cash.feature.money.gui;

import com.frahhs.cash.Cash;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The ChangeGUI class represents a graphical user interface for displaying and handling change transactions.
 */
public class ChangeGUI implements InventoryHolder {
    private final Inventory inventory;
    private final Material fillerMaterial = Material.GRAY_STAINED_GLASS_PANE;
    private final Material buttonMaterial = Material.ARROW;
    private Change change;

    /**
     * Constructor to initialize the ChangeGUI.
     */
    public ChangeGUI() {
        change = new Change();
        inventory = Bukkit.createInventory(this, 9 * 6, "Change");
        draw();
    }

    /**
     * Draws the initial GUI with buttons, filler items, and change slots.
     */
    public void draw() {
        inventory.setItem(getButtonSlot(), getButtonItem());

        for (int slot : getFillerSlots()) {
            inventory.setItem(slot, getFillerItem());
        }

        for (int i = 0; i < getChangeSlots().length; i++) {
            ItemStack itemStack = null;
            if (change.getChange()[i] != null) {
                itemStack = change.getChange()[i].clone();
                ItemMeta itemMeta = itemStack.getItemMeta();
                assert itemMeta != null;
                NamespacedKey namespacedKey = new NamespacedKey(Cash.getInstance(), "change");
                itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.BOOLEAN, true);
                itemStack.setItemMeta(itemMeta);
            }
            inventory.setItem(getChangeSlots()[i], itemStack);
        }
    }

    /**
     * Resets the GUI, clearing change items and re-drawing the GUI elements.
     */
    public void reset() {
        inventory.setItem(getButtonSlot(), getButtonItem());
        change = new Change();

        for (int i = 0; i < 54; i++) {
            if (isFillerSlot(i)) {
                inventory.setItem(i, getFillerItem());
            } else {
                inventory.setItem(i, null);
            }
        }
    }

    /**
     * Gets the Change object associated with this GUI.
     *
     * @return The Change object.
     */
    public Change getChange() {
        return change;
    }

    /**
     * Creates an ItemStack representing a filler item.
     *
     * @return The filler item ItemStack.
     */
    private ItemStack getFillerItem() {
        ItemStack itemStack = new ItemStack(fillerMaterial);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * Creates an ItemStack representing the button item.
     *
     * @return The button item ItemStack.
     */
    private ItemStack getButtonItem() {
        ItemStack itemStack = new ItemStack(buttonMaterial);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ChatColor.DARK_GREEN + "Change");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * Gets the first empty slot in the inventory.
     *
     * @return The first empty slot index, or null if no empty slot is found.
     */
    Integer getFirstEmptySlot() {
        for (int i = 0; i < 54; i++) {
            if (inventory.getItem(i) == null || Objects.requireNonNull(inventory.getItem(i)).getType() == Material.AIR) {
                return i;
            }
        }
        return null;
    }

    /**
     * Gets the first empty storage slot in the inventory.
     *
     * @return The first empty storage slot index, or null if no empty storage slot is found.
     */
    Integer getFirstEmptyStorageSlot() {
        for (int i = 0; i < 54; i++) {
            if (isStorageSlot(i) && (inventory.getItem(i) == null || Objects.requireNonNull(inventory.getItem(i)).getType() == Material.AIR)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Checks if a slot is a storage slot.
     *
     * @param slot The slot index to check.
     * @return True if the slot is a storage slot, false otherwise.
     */
    public boolean isStorageSlot(int slot) {
        return switch (slot) {
            case 10, 11, 12, 19, 20, 21, 28, 29, 30 -> true;
            default -> false;
        };
    }

    /**
     * Checks if a slot is a change slot.
     *
     * @param slot The slot index to check.
     * @return True if the slot is a change slot, false otherwise.
     */
    public boolean isChangeSlot(int slot) {
        return switch (slot) {
            case 14, 15, 16, 23, 24, 25, 32, 33, 34, 41, 42, 43 -> true;
            default -> false;
        };
    }

    /**
     * Checks if a slot is the button slot.
     *
     * @param slot The slot index to check.
     * @return True if the slot is the button slot, false otherwise.
     */
    public boolean isButtonSlot(int slot) {
        return slot == 22;
    }

    /**
     * Checks if a slot is a filler slot.
     *
     * @param slot The slot index to check.
     * @return True if the slot is a filler slot, false otherwise.
     */
    public boolean isFillerSlot(int slot) {
        return slot <= 9 * 6 && !isChangeSlot(slot) && !isStorageSlot(slot) && !isButtonSlot(slot);
    }

    /**
     * Gets an array of indices for the storage slots.
     *
     * @return An array of storage slot indices.
     */
    int[] getStorageSlots() {
        return new int[]{10, 11, 12, 19, 20, 21, 28, 29, 30};
    }

    /**
     * Gets an array of indices for the change slots.
     *
     * @return An array of change slot indices.
     */
    int[] getChangeSlots() {
        return new int[]{14, 15, 16, 23, 24, 25, 32, 33, 34, 41, 42, 43};
    }

    /**
     * Gets the index of the button slot.
     *
     * @return The button slot index.
     */
    int getButtonSlot() {
        return 22;
    }

    /**
     * Gets an array of indices for the filler slots.
     *
     * @return An array of filler slot indices.
     */
    int[] getFillerSlots() {
        List<Integer> fillerSlots = new ArrayList<>();
        for (int slot = 0; slot < 54; slot++) {
            if (isFillerSlot(slot)) {
                fillerSlots.add(slot);
            }
        }
        return fillerSlots.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Gets the inventory associated with this GUI.
     *
     * @return The inventory.
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Displays the GUI to the given player.
     *
     * @param player The player to show the GUI to.
     */
    public void show(Player player) {
        player.openInventory(inventory);
    }
}
