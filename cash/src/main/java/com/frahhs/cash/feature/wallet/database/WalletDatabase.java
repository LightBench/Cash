package com.frahhs.cash.feature.wallet.database;

import com.frahhs.lightlib.LightPlugin;

import java.sql.Connection;
import java.sql.Statement;

public class WalletDatabase {
    /**
     * Creates the wallet inventories table if it does not exist.
     */
    public static void createTable() {
        Statement stmt;
        Connection connection = LightPlugin.getLightDatabase().getConnection();

        // if table safes not exist create it
        try {
            stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS WalletInventory (" +
                         "walletUUID CHAR(100) PRIMARY KEY,"            +
                         "timestamp DEFAULT CURRENT_TIMESTAMP,"         +
                         "inventory TEXT NOT NULL)"                     ;
            stmt.executeUpdate(sql);
            connection.commit();
            stmt.close();
        } catch ( Exception e ) {
            LightPlugin.getLightLogger().error("Error while creating WalletInventory table, %s", e);
        }
    }
}
