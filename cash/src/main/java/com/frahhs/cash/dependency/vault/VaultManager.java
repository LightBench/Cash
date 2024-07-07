package com.frahhs.cash.dependency.vault;

import com.frahhs.lightlib.LightPlugin;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The VaultManager class handles the integration with the Vault plugin, which provides economy,
 * permission, and chat functionalities for Bukkit plugins. This class ensures that the necessary
 * Vault services are available and sets them up for use within the plugin.
 */
public class VaultManager {
    private static Economy economy = null;
    private static Permission permission = null;
    private static Chat chat = null;

    /**
     * Initializes the Vault integration. Checks for the Vault plugin and sets up the economy service.
     *
     * @param plugin The JavaPlugin instance.
     * @return true if Vault and the economy service are successfully initialized, false otherwise.
     */
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

    /**
     * Sets up the economy service using Vault.
     *
     * @param plugin The JavaPlugin instance.
     * @return true if the economy service is successfully set up, false otherwise.
     */
    private static boolean setupEconomy(JavaPlugin plugin) {
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    /**
     * Sets up the chat service using Vault.
     *
     * @param plugin The JavaPlugin instance.
     * @return true if the chat service is successfully set up, false otherwise.
     */
    private static boolean setupChat(JavaPlugin plugin) {
        RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);
        assert rsp != null;
        chat = rsp.getProvider();
        return chat != null;
    }

    /**
     * Sets up the permission service using Vault.
     *
     * @param plugin The JavaPlugin instance.
     * @return true if the permission service is successfully set up, false otherwise.
     */
    private static boolean setupPermissions(JavaPlugin plugin) {
        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        assert rsp != null;
        permission = rsp.getProvider();
        return permission != null;
    }

    /**
     * Gets the economy service.
     *
     * @return The economy service, or null if it is not set up.
     */
    public static Economy getEconomy() {
        return economy;
    }

    /**
     * Gets the permission service.
     *
     * @return The permission service, or null if it is not set up.
     */
    public static Permission getPermission() {
        return permission;
    }

    /**
     * Gets the chat service.
     *
     * @return The chat service, or null if it is not set up.
     */
    public static Chat getChat() {
        return chat;
    }
}
