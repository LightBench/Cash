package com.frahhs.cash.feature.atm.item;

import com.frahhs.lightlib.item.LightItem;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class AtmButton extends LightItem {
    @Override
    public @NotNull String getIdentifier() {
        return "atm_button";
    }

    @Override
    public int getCustomModelData() {
        return 8083;
    }

    @Override
    public ShapedRecipe getDefaultShapedRecipe() {
        return null;
    }

    @Override
    public boolean isGivable() {
        return false;
    }

    @Override
    public boolean isUnique() {
        return false;
    }

    @Override
    public @NotNull Material getVanillaMaterial() {
        return Material.STICK;
    }
}
