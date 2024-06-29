package com.frahhs.cash.feature.wallet.item;

import com.frahhs.cash.feature.wallet.database.WalletDatabase;
import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.item.LightItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

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
    public boolean isUnique() {
        return true;
    }

    @Override
    public @NotNull Material getVanillaMaterial() {
        return Material.STICK;
    }

    public static UUID getUUID(JavaPlugin plugin, ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, "uuid");
        String uuid = container.get(namespacedKey, PersistentDataType.STRING);
        return UUID.fromString(uuid);
    }
}
