package com.frahhs.cash.feature.wallet;

import com.frahhs.cash.feature.wallet.database.WalletDatabase;
import com.frahhs.cash.feature.wallet.item.Wallet;
import com.frahhs.cash.feature.wallet.item.WalletInventoryListener;
import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.feature.LightFeature;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.sql.Statement;

public class WalletFeature extends LightFeature {
    @Override
    protected void onEnable() {
        // Create table to store wallet inventories
        WalletDatabase.createTable();
    }

    @Override
    protected void onDisable() {

    }

    @Override
    protected void registerEvents(JavaPlugin javaPlugin) {
        Bukkit.getPluginManager().registerEvents(new WalletInventoryListener(), javaPlugin);
    }

    @Override
    protected void registerBags(JavaPlugin javaPlugin) {

    }

    @Override
    protected void registerItems(JavaPlugin javaPlugin) {
        LightPlugin.getItemsManager().registerItems(new Wallet(), javaPlugin);
    }

    @Override
    protected @NotNull String getID() {
        return "wallet";
    }
}
