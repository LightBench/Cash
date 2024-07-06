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
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChangeGUI implements InventoryHolder {
    private final Inventory inventory;
    private final Material fillerMaterial = Material.GRAY_STAINED_GLASS_PANE;
    private final Material buttonMaterial = Material.ARROW;

    private Change change;
    
    public ChangeGUI() {
        change = new Change();
        inventory = Bukkit.createInventory(this, 9*6, "Change");
        draw();
    }
    
    public void draw() {
        inventory.setItem(getButtonSlot(), getButtonItem());

        for(int slot : getFillerSlots()) {
            inventory.setItem(slot, getFillerItem());
        }

        for(int i = 0; i < getChangeSlots().length; i++) {
            ItemStack itemStack = null;
            if(change.getChange()[i] != null) {
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

    public void reset() {
        inventory.setItem(getButtonSlot(), getButtonItem());
        change = new Change();

        for(int i = 0; i < 54; i++) {
            if(isFillerSlot(i)) {
                inventory.setItem(i, getFillerItem());
            } else {
                inventory.setItem(i, null);
            }
        }
    }

    public Change getChange() {
        return change;
    }

    private ItemStack getFillerItem() {
        ItemStack itemStack = new ItemStack(fillerMaterial);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack getButtonItem() {
        ItemStack itemStack = new ItemStack(buttonMaterial);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ChatColor.DARK_GREEN + "Change");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    Integer getFirstEmptySlot() {
        for(int i = 0; i < 54; i++) {
            if(inventory.getItem(i) == null) {
                return i;
            }
            if(Objects.requireNonNull(inventory.getItem(i)).getType() == Material.AIR) {
                return i;
            }
        }

        return null;
    }

    Integer getFirstEmptyStorageSlot() {
        for(int i = 0; i < 54; i++) {
            if(isStorageSlot(i)) {
                if(inventory.getItem(i) == null) {
                    return i;
                }
                if(Objects.requireNonNull(inventory.getItem(i)).getType() == Material.AIR) {
                    return i;
                }
            }
        }

        return null;
    }

    public boolean isStorageSlot(int slot) {
        return switch (slot) {
            case 10, 11, 12, 19, 20, 21, 28, 29, 30 -> true;
            default -> false;
        };
    }

    public boolean isChangeSlot(int slot) {
        return switch (slot) {
            case 14, 15, 16, 23, 24, 25, 32, 33, 34, 41, 42, 43 -> true;
            default -> false;
        };
    }

    public boolean isButtonSlot(int slot) {
        return slot == 22;
    }

    public boolean isFillerSlot(int slot) {
        if(slot > 9 * 6)
            return false;

        return (!isChangeSlot(slot)) && (!isStorageSlot(slot)) && (!isButtonSlot(slot));
    }

    int[] getStorageSlots() {
        return new int[] {10, 11, 12, 19, 20, 21, 28, 29, 30};
    }

    int[] getChangeSlots() {
        return new int[] {14, 15, 16, 23, 24, 25, 32, 33, 34, 41, 42, 43};
    }

    int getButtonSlot() {
        return 22;
    }

    int[] getFillerSlots() {
        List<Integer> fillerSlots = new ArrayList<>();
        for(int slot = 0; slot < 54; slot++) {
            if(isFillerSlot(slot)) {
                fillerSlots.add(slot);
            }
        }

        return fillerSlots.stream().mapToInt(Integer::intValue).toArray();
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void show(Player player) {
        player.openInventory(inventory);
    }
}
