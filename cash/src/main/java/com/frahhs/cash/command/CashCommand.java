package com.frahhs.cash.command;


import com.acf.BaseCommand;
import com.acf.CommandHelp;
import com.acf.annotation.*;
import com.acf.bukkit.contexts.OnlinePlayer;
import com.frahhs.cash.Cash;
import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.item.ItemManager;
import com.frahhs.lightlib.item.LightItem;
import com.frahhs.lightlib.provider.MessagesProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.common.value.qual.IntRange;

@CommandAlias("cash")
@Description("Plugin main command")
public class CashCommand extends BaseCommand {
    MessagesProvider messages;

    public CashCommand() {
        messages = LightPlugin.getMessagesProvider();
    }

    @Default
    @CommandPermission("cash.help")
    @Description("Show all the commands.")
    public void onBase(Player player, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("reload")
    @CommandPermission("cash.reload")
    @Description("Reload the configuration of the plugin.")
    public void onReload(Player player) {
        Cash.getInstance().onReload();
        String message = messages.getMessage("commands.reload");
        player.sendMessage(message);
    }

    @Subcommand("give")
    @CommandPermission("cash.give")
    @CommandCompletion("* @CustomItems 1|64")
    @Description("give a Custom item to a player.")
    public void onGive(CommandSender sender, OnlinePlayer player, @Single String item_name, @IntRange(from=1, to=64) @Default("1") int amount) {
        ItemManager itemManager = LightPlugin.getItemsManager();

        String message;
        LightItem customItem;

        try {
            customItem = itemManager.get(item_name);
        } catch (IllegalArgumentException e) {
            customItem = null;
        }

        item_name = item_name.substring(0, 1).toUpperCase() + item_name.substring(1).toLowerCase();
        if(customItem == null || !customItem.isGivable()) {
            message = messages.getMessage("commands.item_not_found");
            message = message.replace("{item}", item_name);
            sender.sendMessage(message);
            return;
        }

        ItemStack item = customItem.getItemStack();
        item.setAmount(amount);
        player.getPlayer().getInventory().addItem(item);

        message = messages.getMessage("commands.given");
        message = message.replace("{player}", player.getPlayer().getDisplayName());
        message = message.replace("{item}", item_name);
        message = message.replace("{amount}", Integer.toString(amount));
        sender.sendMessage(message);
    }

    @CatchUnknown
    public void onUnknown(CommandSender sender) {
        String message = messages.getMessage("commands.unknown");
        sender.sendMessage(message);
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
