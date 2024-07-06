package com.frahhs.cash.feature.money.gui;

import com.frahhs.cash.feature.money.item.Money;
import com.frahhs.lightlib.LightListener;
import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.item.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChangeGUIListener extends LightListener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onButtonClick(InventoryClickEvent e) {
        if(!(e.getClickedInventory().getHolder() instanceof ChangeGUI gui))
            return;

        if(gui.isButtonSlot(e.getSlot())) {
            ItemManager itemManager = LightPlugin.getItemsManager();
            double amount = 0.0;
            List<Money> moneyList = new ArrayList<>();
            for(int slot : gui.getStorageSlots()) {
                if(itemManager.get(gui.getInventory().getItem(slot)) instanceof Money money) {
                    moneyList.add(money);
                    amount += money.getValue() * gui.getInventory().getItem(slot).getAmount();
                }
            }

            Map<Money, Integer> change;
            if(moneyList.size() == 1)
                change = Change.getChangeCombinations(amount, moneyList.get(0).getValue());
            else {
                change = Change.getChangeCombinations(amount);
            }


            for(Money money : change.keySet()) {
                ItemStack itemStack = money.getItemStack();
                itemStack.setAmount(change.get(money));
                e.getWhoClicked().getInventory().addItem(itemStack);
            }
            gui.reset();
            e.getWhoClicked().closeInventory();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onUpdate(InventoryDragEvent e) {
        if(!(e.getInventory().getHolder() instanceof ChangeGUI gui))
            return;

        ItemManager itemManager = LightPlugin.getItemsManager();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            double amount = 0.0;
            List<Money> moneyList = new ArrayList<>();
            for(int slot : gui.getStorageSlots()) {
                if(itemManager.get(gui.getInventory().getItem(slot)) instanceof Money money) {
                    moneyList.add(money);
                    amount += money.getValue() * gui.getInventory().getItem(slot).getAmount();
                }
            }

            if(moneyList.size() == 1)
                gui.getChange().setChange(amount, moneyList.get(0).getValue());
            else {
                gui.getChange().setChange(amount);
            }
            gui.draw();
        }, 1);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onUpdate(InventoryClickEvent e) {
        if(!(e.getInventory().getHolder() instanceof ChangeGUI gui))
            return;

        ItemManager itemManager = LightPlugin.getItemsManager();
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            double amount = 0.0;
            List<Money> moneyList = new ArrayList<>();
            for(int slot : gui.getStorageSlots()) {
                if(itemManager.get(gui.getInventory().getItem(slot)) instanceof Money money) {
                    moneyList.add(money);
                    amount += money.getValue() * gui.getInventory().getItem(slot).getAmount();
                }
            }

            if(moneyList.size() == 1)
                gui.getChange().setChange(amount, moneyList.get(0).getValue());
            else {
                gui.getChange().setChange(amount);
            }
            gui.draw();
        }, 1);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == null)
            return;

        if(!(e.getClickedInventory().getHolder() instanceof ChangeGUI gui))
            return;

        if(!gui.isStorageSlot(e.getSlot())) {
            e.setCancelled(true);
            return;
        }

        ItemManager itemManager = LightPlugin.getItemsManager();

        ItemStack itemOnCursor = e.getWhoClicked().getItemOnCursor();

        boolean isMoney = itemManager.get(itemOnCursor) instanceof Money;
        boolean isAir = itemOnCursor.getType() == Material.AIR;

        if(!isMoney && !isAir)
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMoveToOtherInventory(InventoryClickEvent e) {
        System.out.println("here1");
        if(e.getClickedInventory() == null)
            return;

        System.out.println("here2");
        if(!(e.getInventory().getHolder() instanceof ChangeGUI gui))
            return;

        System.out.println("here3");
        if(e.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY)
            return;

        System.out.println("here4");
        if(e.getClickedInventory() == e.getInventory())
            return;

        System.out.println("here5");
        if(gui.getFirstEmptySlot() == null)
            return;

        System.out.println("here6");
        if(gui.isStorageSlot(gui.getFirstEmptySlot()))
            return;

        System.out.println("here7");
        for(int slot : gui.getStorageSlots()) {
            if (gui.getInventory().getItem(slot) != null) {
                if (gui.getInventory().getItem(slot).isSimilar(e.getCurrentItem())) {
                    if (gui.getInventory().getItem(slot).getAmount() < 64) {
                        e.getCurrentItem().setAmount(e.getCurrentItem().getAmount() - (64 - gui.getInventory().getItem(slot).getAmount()));
                        gui.getInventory().getItem(slot).setAmount(64);
                    }
                }
            }
        }

        System.out.println("here8");
        if(gui.getFirstEmptyStorageSlot() == null) {
            e.setCancelled(true);
            return;
        }

        System.out.println("here9");
        if(gui.isStorageSlot(gui.getFirstEmptySlot()))
            return;

        System.out.println("here10");
        e.setCancelled(true);
        gui.getInventory().setItem(gui.getFirstEmptyStorageSlot(), e.getCurrentItem().clone());
        e.getClickedInventory().setItem(e.getSlot(), null);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDrag(InventoryDragEvent e) {
        if(!(e.getInventory().getHolder() instanceof ChangeGUI gui))
            return;

        boolean onlyBottomInventory = true;
        for(int slot : e.getRawSlots()) {
            if(slot <= 54) {
                onlyBottomInventory = false;
            }
        }

        if(onlyBottomInventory)
            return;

        for(int slot : e.getRawSlots()) {
            if(!gui.isStorageSlot(slot)) {
                e.setCancelled(true);
                return;
            }
        }

        ItemManager itemManager = LightPlugin.getItemsManager();
        ItemStack itemOnCursor = e.getOldCursor();
        boolean isMoney = itemManager.get(itemOnCursor) instanceof Money;

        if(!isMoney)
            e.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(!(e.getInventory().getHolder() instanceof ChangeGUI gui))
            return;

        for(int slot : gui.getStorageSlots()) {
            if(gui.getInventory().getItem(slot) != null) {
                e.getPlayer().getInventory().addItem(gui.getInventory().getItem(slot));
            }
        }
    }
}
