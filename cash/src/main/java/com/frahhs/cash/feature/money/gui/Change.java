package com.frahhs.cash.feature.money.gui;

import com.frahhs.cash.feature.money.item.Money;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Change {
    private ItemStack[] change = new ItemStack[12];

    public ItemStack[] getChange() {
        return change;
    }

    public void setChange(double amount, double exclude) {
        change = new ItemStack[12];

        Map<Money, Integer> changeCombinations = getChangeCombinations(amount, exclude);
        int i = 0;
        for(Money money : changeCombinations.keySet()) {
            int amountOfMoney = changeCombinations.get(money);
            while(amountOfMoney > 0) {
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

    public void setChange(double amount) {
        setChange(amount, 0.0);
    }

    public static Map<Money, Integer> getChangeCombinations(double amount, double exclude) {
        List<Money> moneyList = new ArrayList<>(Money.getMoney());
        double exclude2 = exclude;
        moneyList.removeIf(money -> (money.getValue() == exclude2));

        Map<Money, Integer> result = new HashMap<>();
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

    public static Map<Money, Integer> getChangeCombinations(double amount) {
        return getChangeCombinations(amount, 0.0);
    }
}
