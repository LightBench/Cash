package com.frahhs.cash.feature.atm.gui;

import com.frahhs.cash.Cash;
import com.frahhs.cash.dependency.vault.VaultManager;
import com.frahhs.cash.feature.atm.item.AtmButton;
import com.frahhs.cash.feature.money.item.Money;
import com.frahhs.lightlib.LightPlugin;
import com.frahhs.lightlib.item.ItemManager;
import net.milkbowl.vault.economy.EconomyResponse;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The AtmGUI class handles the graphical user interface for ATM transactions within the plugin.
 * It allows players to deposit and withdraw money using the Vault economy system.
 */
public class AtmGUI {

    /**
     * Opens the ATM GUI for the specified player.
     *
     * @param player The player to open the ATM GUI for.
     */
    public static void open(Player player) {
        ItemManager itemManager = LightPlugin.getItemsManager();
        ItemStack deposit = itemManager.get(AtmButton.class).getItemStack();
        ItemStack withdraw = itemManager.get(AtmButton.class).getItemStack();

        new AnvilGUI.Builder().onClick(AtmGUI::onClick)
                .plugin(Cash.getInstance())
                .interactableSlots(AnvilGUI.Slot.INPUT_RIGHT)
                .jsonTitle(balanceString(player))
                .text(" ")
                .itemOutput(withdraw)
                .itemLeft(deposit)
                .open(player);
    }

    /**
     * Handles clicks within the ATM GUI.
     *
     * @param slot The slot that was clicked.
     * @param stateSnapshot The state of the GUI at the time of the click.
     * @return A list of response actions to be taken.
     */
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

    /**
     * Processes a withdrawal request from the player.
     *
     * @param player The player making the withdrawal.
     * @param text The amount to withdraw, as a string.
     * @return A list of response actions to be taken.
     */
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

        // Negative balance
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

    /**
     * Processes a deposit request from the player.
     *
     * @param player The player making the deposit.
     * @param itemStack The item stack representing the money being deposited.
     * @return A list of response actions to be taken.
     */
    private static List<AnvilGUI.ResponseAction> deposit(Player player, ItemStack itemStack) {
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

    /**
     * Restores the deposit and withdraw buttons in the ATM GUI.
     *
     * @param anvilInventory The inventory of the anvil GUI.
     */
    private static void restoreButtons(Inventory anvilInventory) {
        ItemManager itemManager = LightPlugin.getItemsManager();
        ItemStack deposit = itemManager.get(AtmButton.class).getItemStack();
        ItemStack withdraw = itemManager.get(AtmButton.class).getItemStack();

        anvilInventory.setItem(AnvilGUI.Slot.INPUT_LEFT, deposit);
        anvilInventory.setItem(AnvilGUI.Slot.OUTPUT, withdraw);
    }

    /**
     * Creates an action to update the balance displayed in the ATM GUI.
     *
     * @param player The player whose balance is being updated.
     * @return The response action to update the balance.
     */
    private static AnvilGUI.ResponseAction updateBalanceResponse(Player player) {
        return AnvilGUI.ResponseAction.updateJsonTitle(balanceString(player), false);
    }

    /**
     * Creates a JSON string representing the player's balance for display in the ATM GUI.
     *
     * @param player The player whose balance is being displayed.
     * @return The JSON string representing the player's balance.
     */
    private static String balanceString(Player player) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"text\":\"")
                .append("\uF003§f\uD83D\uDC53\uF003\uF004§8Balance: ")
                .append(String.format("%.2f", VaultManager.getEconomy().getBalance(player)))
                .append("$")
                .append("\",\"color\":\"white\"}");

        return builder.toString();
    }

    /**
     * Algorithm to determine the distribution of money denominations for a withdrawal.
     *
     * @param amount The amount to withdraw.
     * @return A map of money denominations and their respective quantities.
     */
    private static Map<Money, Integer> withdrawAlgo(double amount) {
        Map<Money, Integer> result = new HashMap<>();
        List<Money> moneyList = Money.getMoney();

        // Withdraw algorithm
        for(Money money : moneyList) {
            int amountOfMoney = (int) (amount / money.getValue());
            if(amountOfMoney > 0) {
                result.put(money, amountOfMoney);
                amount -= amountOfMoney * money.getValue();
            }
        }

        return result;
    }

    /**
     * Calculates the remaining amount after a withdrawal.
     *
     * @param amount The amount to withdraw.
     * @return The remaining amount after the withdrawal.
     */
    private static double remainingAmount(double amount) {
        double withdrawn = 0.0;
        Map<Money, Integer> result = withdrawAlgo(amount);

        for(Money money : result.keySet()) {
            withdrawn += (money.getValue() * result.get(money));
        }

        return amount - withdrawn;
    }
}
