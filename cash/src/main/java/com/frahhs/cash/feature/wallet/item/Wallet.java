package com.frahhs.cash.feature.wallet.item;

import com.frahhs.lightlib.item.LightItem;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class Wallet extends LightItem {
    @Override
    public @NotNull String getIdentifier() {
        return "wallet";
    }

    @Override
    public int getCustomModelData() {
        return 8080;
    }

    @Override
    public ShapedRecipe getDefaultShapedRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(getNamespacedKey(), getItemStack());

        shapedRecipe.shape("III", "I I", "III");
        shapedRecipe.setIngredient('I', Material.LEATHER);

        shapedRecipe.getShape();
        shapedRecipe.getIngredientMap();

        return shapedRecipe;
    }

    @Override
    public boolean isGivable() {
        return true;
    }

    @Override
    public @NotNull Material getVanillaMaterial() {
        return Material.STICK;
    }
}
