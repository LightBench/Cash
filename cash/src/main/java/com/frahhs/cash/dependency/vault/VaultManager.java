package com.frahhs.cash.dependency.vault;

import com.frahhs.lightlib.LightPlugin;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class VaultManager {
    private static Economy economy = null;
    private static Permission permission = null;
    private static Chat chat = null;

    public static boolean init(JavaPlugin plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            LightPlugin.getLightLogger().error( "===================================================== ");
            LightPlugin.getLightLogger().error( "Disabled Cash due to no Vault dependency found!       ");
            LightPlugin.getLightLogger().error( "Please download and install Vault from:               ");
            LightPlugin.getLightLogger().error( "https://www.spigotmc.org/resources/vault.34315/       ");
            LightPlugin.getLightLogger().error( "===================================================== ");
            return false;
        }

        if ( !setupEconomy(plugin) ) {
            LightPlugin.getLightLogger().error( "===================================================== ");
            LightPlugin.getLightLogger().error( "Disabled Cash due to no Economy plugin found!         ");
            LightPlugin.getLightLogger().error( "Please download and install any economy plugin,       ");
            LightPlugin.getLightLogger().error( "Like EssentialsX:                                     ");
            LightPlugin.getLightLogger().error( "https://essentialsx.net/downloads.html                ");
            LightPlugin.getLightLogger().error( "===================================================== ");

            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return false;
        }
        //setupPermissions(plugin);
        //setupChat(plugin);
        return true;
    }

    private static boolean setupEconomy(JavaPlugin plugin) {

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    private static boolean setupChat(JavaPlugin plugin) {
        RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);
        assert rsp != null;
        chat = rsp.getProvider();
        return chat != null;
    }

    private static boolean setupPermissions(JavaPlugin plugin) {
        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        assert rsp != null;
        permission = rsp.getProvider();
        return permission != null;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static Permission getPermission() {
        return permission;
    }

    public static Chat getChat() {
        return chat;
    }
}
