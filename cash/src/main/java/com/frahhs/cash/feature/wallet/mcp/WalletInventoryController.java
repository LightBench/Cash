package com.frahhs.cash.feature.wallet.mcp;

import com.frahhs.lightlib.feature.LightController;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * WalletInventoryController is responsible for managing wallet inventories.
 */
public class WalletInventoryController extends LightController {

    /**
     * Opens the wallet inventory for a player.
     *
     * @param player The player for whom the wallet inventory is to be opened.
     * @param wallet The wallet ItemStack.
     */
    public void openInventory(Player player, ItemStack wallet) {
        WalletInventory walletInventory = new WalletInventory(wallet);
        player.openInventory(walletInventory.getInventory());
    }

    /**
     * Updates the contents of the wallet inventory.
     *
     * @param wallet The wallet ItemStack.
     * @param content The new contents to be saved in the wallet inventory.
     */
    public void updateInventory(ItemStack wallet, ItemStack[] content) {
        WalletInventory walletInventory = new WalletInventory(wallet);
        walletInventory.save(content);
    }
}
