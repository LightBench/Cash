package com.frahhs.cash.feature.money.gui;

import com.frahhs.cash.feature.money.item.Money;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Change class handles the calculation and storage of change in the form of money items.
 */
public class Change {
    private ItemStack[] change = new ItemStack[12];

    /**
     * Gets the array of ItemStacks representing the change.
     *
     * @return The array of ItemStacks representing the change.
     */
    public ItemStack[] getChange() {
        return change;
    }

    /**
     * Sets the change based on the given amount and excludes a specific money value.
     *
     * @param amount  The amount of money to convert into change.
     * @param exclude The money value to exclude from the change calculation.
     */
    public void setChange(double amount, double exclude) {
        change = new ItemStack[12];

        Map<Money, Integer> changeCombinations = getChangeCombinations(amount, exclude);
        int i = 0;
        for (Money money : changeCombinations.keySet()) {
            int amountOfMoney = changeCombinations.get(money);
            while (amountOfMoney > 0) {
                ItemStack itemStack = money.getItemStack();

                if (amountOfMoney > 64) {
                    itemStack.setAmount(64);
                    amountOfMoney -= 64;
                    change[i++] = itemStack;
                } else {
                    itemStack.setAmount(amountOfMoney);
                    amountOfMoney -= amountOfMoney;
                    change[i++] = itemStack;
                }
            }
        }
    }

    /**
     * Sets the change based on the given amount.
     *
     * @param amount The amount of money to convert into change.
     */
    public void setChange(double amount) {
        setChange(amount, 0.0);
    }

    /**
     * Calculates the combinations of money items to represent the given amount, excluding a specific money value.
     *
     * @param amount  The amount of money to convert into change.
     * @param exclude The money value to exclude from the change calculation.
     * @return A map of money items and their respective quantities to represent the given amount.
     */
    public static Map<Money, Integer> getChangeCombinations(double amount, double exclude) {
        List<Money> moneyList = new ArrayList<>(Money.getMoney());
        double exclude2 = exclude;
        moneyList.removeIf(money -> (money.getValue() == exclude2));

        Map<Money, Integer> result = new HashMap<>();
        // Withdraw algorithm
        for (Money money : moneyList) {
            int amountOfMoney = (int) (amount / money.getValue());
            if (amountOfMoney > 0) {
                result.put(money, amountOfMoney);
                amount -= amountOfMoney * money.getValue();
            }
        }

        return result;
    }

    /**
     * Calculates the combinations of money items to represent the given amount.
     *
     * @param amount The amount of money to convert into change.
     * @return A map of money items and their respective quantities to represent the given amount.
     */
    public static Map<Money, Integer> getChangeCombinations(double amount) {
        return getChangeCombinations(amount, 0.0);
    }
}
