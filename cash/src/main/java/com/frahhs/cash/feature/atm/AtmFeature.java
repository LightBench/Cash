package com.frahhs.cash.feature.atm;

import com.frahhs.cash.feature.atm.item.Atm;
import com.frahhs.cash.feature.atm.item.AtmButton;
import com.frahhs.cash.feature.atm.listener.AtmListener;
import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.feature.LightFeature;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AtmFeature extends LightFeature {
    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    @Override
    protected void registerEvents(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new AtmListener(), plugin);
    }

    @Override
    protected void registerBags(JavaPlugin plugin) {

    }

    @Override
    protected void registerItems(JavaPlugin plugin) {
        LightPlugin.getItemsManager().registerItems(new Atm(), plugin);
        LightPlugin.getItemsManager().registerItems(new AtmButton(), plugin);
    }

    @Override
    protected @NotNull String getID() {
        return "atm";
    }
}
