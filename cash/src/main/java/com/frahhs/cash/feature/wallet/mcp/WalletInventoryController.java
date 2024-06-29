package com.frahhs.cash.feature.wallet.mcp;

import com.frahhs.cash.feature.wallet.item.Wallet;
import com.frahhs.lightlib.feature.LightController;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WalletInventoryController extends LightController {

    public void openInventory(Player player, ItemStack wallet) {
        WalletInventory walletInventory = new WalletInventory(wallet);
        player.openInventory(walletInventory.getInventory());
    }

    public void updateInventory(ItemStack wallet, ItemStack[] content) {
        WalletInventory walletInventory = new WalletInventory(wallet);
        walletInventory.save(content);
    }
}
