package com.frahhs.cash.feature.atm.item;

import com.frahhs.lightlib.item.LightItem;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class CreditCard extends LightItem {

    @Override
    public @NotNull String getIdentifier() {
        return "credit_card";
    }

    @Override
    public int getCustomModelData() {
        return 8011;
    }

    @Override
    public ShapedRecipe getDefaultShapedRecipe() {
        return null;
    }

    @Override
    public boolean isGivable() {
        return true;
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public @NotNull Material getVanillaMaterial() {
        return Material.BOOK;
    }
}
