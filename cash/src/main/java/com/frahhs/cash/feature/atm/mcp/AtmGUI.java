package com.frahhs.cash.feature.atm.mcp;

import com.frahhs.cash.Cash;
import com.frahhs.cash.dependency.vault.VaultManager;
import com.frahhs.cash.feature.money.item.Money;
import com.frahhs.lightlib.LightPlugin;
import net.milkbowl.vault.economy.EconomyResponse;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AtmGUI {

    public static void open(Player player) {
        ItemStack deposit = new ItemStack(Material.DIAMOND_BLOCK);
        ItemMeta depositMeta = deposit.getItemMeta();
        assert depositMeta != null;
        depositMeta.setDisplayName("Deposit");
        deposit.setItemMeta(depositMeta);

        ItemStack withdraw = new ItemStack(Material.DIAMOND_BLOCK);
        ItemMeta withdrawMeta = deposit.getItemMeta();
        assert withdrawMeta != null;
        withdrawMeta.setDisplayName("Withdraw");
        withdraw.setItemMeta(withdrawMeta);

        new AnvilGUI.Builder().onClick(AtmGUI::onClick)
                              .plugin(Cash.getInstance())
                              .interactableSlots(AnvilGUI.Slot.INPUT_RIGHT)
                              .jsonTitle(balanceString(player))
                              .text(" ")
                              .itemOutput(withdraw)
                              .itemLeft(deposit)
                              .open(player);
    }

    private static List<AnvilGUI.ResponseAction> onClick(int slot, AnvilGUI.StateSnapshot stateSnapshot) {
        Player player = stateSnapshot.getPlayer();

        switch (slot) {
            case AnvilGUI.Slot.OUTPUT -> {
                List<AnvilGUI.ResponseAction> res = withdraw(player, stateSnapshot.getText());
                return res;
            }
            case AnvilGUI.Slot.INPUT_LEFT -> {
                List<AnvilGUI.ResponseAction> res = deposit(player, stateSnapshot.getRightItem());
                return res;
            }
            default -> {
                return List.of(updateBalanceResponse(player));
            }
        }
    }

    private static List<AnvilGUI.ResponseAction> withdraw(Player player, String text) {
        Double amount;

        try {
            amount = Double.valueOf(text);
        } catch (NumberFormatException ex) {
            amount = null;
        }

        // Fail
        if(amount == null) {
            return List.of(AnvilGUI.ResponseAction.replaceInputText("Invalid amount typed!"), updateBalanceResponse(player));
        }

        // Negative balace
        if(amount > VaultManager.getEconomy().getBalance(player)) {
            return List.of(AnvilGUI.ResponseAction.replaceInputText("Balance insufficient!"), updateBalanceResponse(player));
        }

        // Success
        amount -= remainingAmount(amount);
        EconomyResponse withdrawRes = VaultManager.getEconomy().withdrawPlayer(player, amount);
        if(withdrawRes.type.equals(EconomyResponse.ResponseType.SUCCESS)) {

            Map<Money, Integer> result = withdrawAlgo(amount);
            for(Money money : result.keySet()) {
                ItemStack itemStack = money.getItemStack();
                itemStack.setAmount(result.get(money));
                player.getInventory().addItem(itemStack);
            }


        } else {
            player.sendMessage("Error during the transaction, try with a lower withdraw!");
        }
        return List.of(AnvilGUI.ResponseAction.replaceInputText(" "), updateBalanceResponse(player));
    }

    private static List<AnvilGUI.ResponseAction> deposit(Player player,ItemStack itemStack) {
        Inventory anvilInventory = player.getOpenInventory().getTopInventory();

        if(!(LightPlugin.getItemsManager().get(itemStack) instanceof Money money)) {
            player.getInventory().addItem(itemStack);
            anvilInventory.setItem(AnvilGUI.Slot.INPUT_RIGHT, new ItemStack(Material.AIR));
            return List.of(AnvilGUI.ResponseAction.replaceInputText("Invalid item!"), updateBalanceResponse(player));
        }

        EconomyResponse depositRes = VaultManager.getEconomy().depositPlayer(player, money.getValue() * itemStack.getAmount());
        if(depositRes.type.equals(EconomyResponse.ResponseType.SUCCESS)) {
            anvilInventory.setItem(AnvilGUI.Slot.INPUT_RIGHT, new ItemStack(Material.AIR));
            return List.of(AnvilGUI.ResponseAction.replaceInputText(" "), updateBalanceResponse(player));
        }

        return List.of(AnvilGUI.ResponseAction.replaceInputText(" "), updateBalanceResponse(player));
    }

    private static void restoreButtons(Inventory anvilInventory) {
        ItemStack deposit = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta depositMeta = deposit.getItemMeta();
        assert depositMeta != null;
        depositMeta.setDisplayName("Deposit");
        deposit.setItemMeta(depositMeta);

        ItemStack withdraw = new ItemStack(Material.DIAMOND_BLOCK);
        ItemMeta withdrawMeta = deposit.getItemMeta();
        assert withdrawMeta != null;
        withdrawMeta.setDisplayName("Withdraw");
        withdraw.setItemMeta(withdrawMeta);

        anvilInventory.setItem(AnvilGUI.Slot.INPUT_LEFT, deposit);
        anvilInventory.setItem(AnvilGUI.Slot.OUTPUT, withdraw);
    }


    private static AnvilGUI.ResponseAction updateBalanceResponse(Player player) {
        return AnvilGUI.ResponseAction.updateJsonTitle(balanceString(player), false);
    }

    private static String balanceString(Player player) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"text\":\"")
                .append("\uF003§f\uD83D\uDC53\uF003\uF004§8Balance: ")
                .append(String.format("%.2f", VaultManager.getEconomy().getBalance(player)))
                .append("$")
                .append("\",\"color\":\"white\"}");


        return builder.toString();
    }

    private static Map<Money, Integer> withdrawAlgo(double amount) {
        Map<Money, Integer> result = new HashMap<>();
        List<Money> moneyList = Money.getMoney();

        // Withdraw algorithm
        for(Money money : moneyList) {
            int amountOfMoney = (int) (amount / money.getValue());
            if(amountOfMoney > 0) {
                result.put(money,  amountOfMoney);
                amount -= amountOfMoney * money.getValue();
            }
        }

        return result;
    }

    private static double remainingAmount(double amount) {
        double withdrawn = 0.0;
        Map<Money, Integer> result = withdrawAlgo(amount);

        for(Money money : result.keySet()) {
            withdrawn += (money.getValue() * result.get(money));
        }

        return amount - withdrawn;
    }
}
