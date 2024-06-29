package com.frahhs.cash.feature.wallet.mcp;

import com.frahhs.cash.feature.wallet.item.Wallet;
import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.feature.LightModel;
import com.frahhs.lightlib.item.ItemManager;
import com.frahhs.lightlib.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class WalletInventory extends LightModel implements InventoryHolder {
    private final WalletInventoryProvider provider;

    private final ItemStack wallet;
    private final Inventory inventory;

    protected WalletInventory(ItemStack wallet) {
        this.wallet = wallet;

        provider = new WalletInventoryProvider();

        inventory = Bukkit.createInventory(this, 18, messages.getMessage("items_name.wallet", false));

        UUID walletUUID = Wallet.getUUID(plugin, wallet);

        if(provider.getWalletInventory(walletUUID) == null) {
            provider.createWallet(walletUUID, ItemUtil.toBase64(inventory.getContents()));
        }

        String wallet64 = provider.getWalletInventory(walletUUID);
        inventory.setContents(ItemUtil.fromBase64(wallet64));
    }

    protected void save(ItemStack[] contents) {
        String inventory64 = ItemUtil.toBase64(contents);
        UUID walletUUID = Wallet.getUUID(plugin, wallet);

        if(provider.getWalletInventory(walletUUID) == null)
            provider.createWallet(walletUUID, inventory64);
        else
            provider.updateWallet(walletUUID, inventory64);
    }

    protected void delete() {
        UUID walletUUID = Wallet.getUUID(plugin, wallet);
        provider.deleteWallet(walletUUID);
    }

    public ItemStack getWallet() {
        return wallet;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
