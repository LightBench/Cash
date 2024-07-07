package com.frahhs.cash.feature.wallet.mcp;

import com.frahhs.cash.feature.wallet.item.Wallet;
import com.frahhs.lightlib.feature.LightModel;
import com.frahhs.lightlib.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * WalletInventory manages the virtual wallet's inventory.
 */
public class WalletInventory extends LightModel implements InventoryHolder {
    private final WalletInventoryProvider provider;
    private final ItemStack wallet;
    private final Inventory inventory;

    /**
     * Constructor to initialize the WalletInventory with a wallet ItemStack.
     *
     * @param wallet The wallet ItemStack.
     */
    protected WalletInventory(ItemStack wallet) {
        this.wallet = wallet;
        this.provider = new WalletInventoryProvider();
        this.inventory = Bukkit.createInventory(this, 18, messages.getMessage("items_name.wallet", false));

        UUID walletUUID = Wallet.getUUID(plugin, wallet);

        // Check if the wallet's inventory is already stored, otherwise create a new one.
        if (provider.getWalletInventory(walletUUID) == null) {
            provider.createWallet(walletUUID, ItemUtil.toBase64(inventory.getContents()));
        }

        // Load the wallet's inventory from the provider.
        String wallet64 = provider.getWalletInventory(walletUUID);
        inventory.setContents(ItemUtil.fromBase64(wallet64));
    }

    /**
     * Saves the contents of the inventory to the provider.
     *
     * @param contents The contents of the inventory.
     */
    protected void save(ItemStack[] contents) {
        String inventory64 = ItemUtil.toBase64(contents);
        UUID walletUUID = Wallet.getUUID(plugin, wallet);

        if (provider.getWalletInventory(walletUUID) == null) {
            provider.createWallet(walletUUID, inventory64);
        } else {
            provider.updateWallet(walletUUID, inventory64);
        }
    }

    /**
     * Deletes the wallet's inventory from the provider.
     */
    protected void delete() {
        UUID walletUUID = Wallet.getUUID(plugin, wallet);
        provider.deleteWallet(walletUUID);
    }

    /**
     * Gets the wallet ItemStack.
     *
     * @return The wallet ItemStack.
     */
    public ItemStack getWallet() {
        return wallet;
    }

    /**
     * Gets the inventory associated with this wallet.
     *
     * @return The inventory.
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
