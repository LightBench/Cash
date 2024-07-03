package com.frahhs.cash.feature.money.item;

import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.item.LightItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Money extends LightItem {
    private final String identifier;
    private final String name;
    private final int customModelData;
    private final boolean isGivvable;
    private final Material vanillaMaterial;
    private final double value;

    public Money(@NotNull String identifier, @NotNull String name, int customModelData, boolean isGivvable, @NotNull Material vanillaMaterial, double value) {
        this.identifier = identifier;
        this.name = name;
        this.customModelData = customModelData;

        this.isGivvable = isGivvable;
        this.vanillaMaterial = vanillaMaterial;
        this.value = value;

        this.namespacedKey = new NamespacedKey(this.plugin, identifier);
        this.item = new ItemStack(this.getVanillaMaterial(), 1);
        ItemMeta meta = this.item.getItemMeta();

        assert meta != null;

        meta.setDisplayName(ChatColor.WHITE + this.getName());
        if (this.getLore() != null) {
            meta.setLore(this.getLore());
        }

        meta.setCustomModelData(this.getCustomModelData());
        this.item.setItemMeta(meta);
    }

    public double getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public int getCustomModelData() {
        return this.customModelData;
    }

    @Override
    public ShapedRecipe getDefaultShapedRecipe() {
        return null;
    }

    @Override
    public boolean isGivable() {
        return this.isGivvable;
    }

    @Override
    public boolean isUnique() {
        return false;
    }

    @Override
    public @NotNull Material getVanillaMaterial() {
        return this.vanillaMaterial;
    }

    public static List<Money> getMoney() {
        List<Money> money = new ArrayList<>();
        for(LightItem item : LightPlugin.getItemsManager().getRegisteredItems()) {
            if(item instanceof Money) {
                money.add((Money) item);
            }
        }

        return money.stream().sorted(Comparator.comparing(Money::getValue).reversed()).toList();
    }
}
