package com.frahhs.cash;

import com.frahhs.cash.command.CashCommand;
import com.frahhs.cash.feature.atm.AtmFeature;
import com.frahhs.cash.feature.money.MoneyFeature;
import com.frahhs.cash.feature.wallet.WalletFeature;
import com.frahhs.lightlib.LightPlugin;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public final class Cash extends LightPlugin {
    @Override
    public void onLightLoad() {
        LightPlugin.getOptions().setPermissionPrefix("cash");
        LightPlugin.getOptions().setSpigotMarketID("34315");
        LightPlugin.getOptions().setUpdateCheck(true);
        LightPlugin.getOptions().setGithubContentsUrl("https://api.github.com/repos/FrahHS/Robbing/contents/robbing/src/main/resources/lang");
        LightPlugin.getOptions().setGithubUrlTemplate("https://raw.githubusercontent.com/FrahHS/Robbing/main/robbing/src/main/resources/lang/");
    }

    //TODO: add a gui to make money "change"
    @Override
    public void onLightEnabled() {
        LightPlugin.getMessagesProvider().setAutoUpdate(false);

        LightPlugin.getFeatureManager().registerFeatures(new AtmFeature(), this);
        LightPlugin.getFeatureManager().registerFeatures(new MoneyFeature(), this);
        LightPlugin.getFeatureManager().registerFeatures(new WalletFeature(), this);

        registerCommands();
    }

    @Override
    public void onLightDisabled() {

    }

    private void registerCommands() {
        // Command settings
        getCommandManager().enableUnstableAPI("help");

        // Commands
        getCommandManager().registerCommand(new CashCommand());

        // Command completions
        getCommandManager().getCommandCompletions().registerCompletion("CustomItems", c -> {
            List<String> rbItems = new ArrayList<>();
            getItemsManager().getRegisteredItems().forEach((item) -> {
                if(item.isGivable()) {
                    rbItems.add(item.getIdentifier());
                }
            });
            return ImmutableList.copyOf(rbItems);
        });
    }
}
