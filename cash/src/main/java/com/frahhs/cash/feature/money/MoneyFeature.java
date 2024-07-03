package com.frahhs.cash.feature.money;

import com.frahhs.cash.feature.money.item.Money;
import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.feature.LightFeature;
import com.frahhs.lightlib.item.LightItem;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MoneyFeature extends LightFeature {
    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    @Override
    protected void registerEvents(JavaPlugin javaPlugin) {

    }

    @Override
    protected void registerBags(JavaPlugin javaPlugin) {

    }

    @Override
    protected void registerItems(JavaPlugin javaPlugin) {
        for(String key : javaPlugin.getConfig().getKeys(true)) {
            if(key.startsWith("money.items")) {
                if(key.split("\\.").length == 3) {
                    String identifier = key.split("\\.")[key.split("\\.").length -1];
                    String name = LightPlugin.getConfigProvider().getString(key + ".name");
                    double value = LightPlugin.getConfigProvider().getDouble(key + ".value");
                    int customModelData = LightPlugin.getConfigProvider().getInt(key + ".custom-model-data");

                    assert name != null;

                    Money money = new Money(
                            identifier,
                            name,
                            customModelData,
                            true,
                            Material.PAPER,
                            value
                    );
                    LightPlugin.getItemsManager().registerItems(money ,javaPlugin);
                }
            }
        }
    }

    @Override
    protected @NotNull String getID() {
        return "money";
    }
}
