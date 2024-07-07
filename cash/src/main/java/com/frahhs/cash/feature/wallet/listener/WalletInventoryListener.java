package com.frahhs.cash.feature.wallet.listener;

import com.frahhs.cash.feature.money.item.Money;
import com.frahhs.cash.feature.wallet.item.Wallet;
import com.frahhs.cash.feature.wallet.mcp.WalletInventory;
import com.frahhs.cash.feature.wallet.mcp.WalletInventoryController;
import com.frahhs.lightlib.LightListener;
import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.item.ItemManager;
import com.frahhs.lightlib.item.LightItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class WalletInventoryListener extends LightListener {
    @EventHandler
    public void onWalletClick(PlayerInteractEvent e) {
        if(e.getHand() == null)
            return;

        if(!e.getHand().equals(EquipmentSlot.HAND))
            return;

        if(!(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
            return;

        ItemManager itemManager = LightPlugin.getItemsManager();
        if(!itemManager.isRegistered(e.getItem()))
            return;

        LightItem item = itemManager.get(e.getItem());
        if(!(item instanceof Wallet))
            return;

        WalletInventoryController controller = new WalletInventoryController();
        controller.openInventory(e.getPlayer(), e.getItem());
    }

    @EventHandler
    public void onWalletUpdate(InventoryClickEvent e) {
        if(!(e.getInventory().getHolder() instanceof WalletInventory walletInventory))
            return;

        boolean isMoney = LightPlugin.getItemsManager().get(e.getCurrentItem()) instanceof Money;
        boolean isAir = false;

        if(e.getCurrentItem() == null)
            isAir = true;
        else if(e.getCurrentItem().getType().isAir())
            isAir = true;

        if(!(isMoney || isAir)) {
            String message = messages.getMessage("wallet.only_money");
            e.getWhoClicked().sendMessage(message);
            e.setCancelled(true);
            return;
        }

        WalletInventoryController controller = new WalletInventoryController();
        controller.updateInventory(walletInventory.getWallet(), e.getInventory().getContents());
    }

    @EventHandler
    public void onWalletClose(InventoryCloseEvent e) {
        if(!(e.getInventory().getHolder() instanceof WalletInventory walletInventory))
            return;

        WalletInventoryController controller = new WalletInventoryController();
        controller.updateInventory(walletInventory.getWallet(), e.getInventory().getContents());
    }
}
