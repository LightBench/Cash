package com.frahhs.cash.feature.wallet.mcp;

import com.frahhs.lightlib.LightProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * WalletInventoryProvider class that extends LightProvider and manages the WalletInventory table.
 */
public class WalletInventoryProvider extends LightProvider {
    /**
     * Creates a new wallet entry in the WalletInventory table.
     *
     * @param walletUUID The unique identifier for the wallet.
     * @param inventory The inventory data to be stored in the wallet.
     * @return true if the wallet entry was created successfully, false otherwise.
     */
    protected boolean createWallet(UUID walletUUID, String inventory) {
        String sql = "INSERT INTO WalletInventory (walletUUID, inventory) VALUES (?, ?)";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, walletUUID.toString());
            pstmt.setString(2, inventory);
            pstmt.executeUpdate();
            dbConnection.commit();
            return true;
        } catch (SQLException e) {
            logger.error("Failed to create wallet entry for UUID: " + walletUUID, e);
            return false;
        }
    }

    /**
     * Retrieves the inventory data for a given walletUUID from the WalletInventory table.
     *
     * @param walletUUID The unique identifier for the wallet.
     * @return The inventory data as a String if found, null otherwise.
     */
    protected String getWalletInventory(UUID walletUUID) {
        String sql = "SELECT inventory FROM WalletInventory WHERE walletUUID = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, walletUUID.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("inventory");
            }
        } catch (SQLException e) {
            logger.error("Failed to retrieve wallet inventory for UUID: " + walletUUID, e);
        }
        return null;
    }

    /**
     * Updates the inventory data for a given walletUUID in the WalletInventory table.
     *
     * @param walletUUID The unique identifier for the wallet.
     * @param newInventory The new inventory data to be stored in the wallet.
     * @return true if the wallet entry was updated successfully, false otherwise.
     */
    protected boolean updateWallet(UUID walletUUID, String newInventory) {
        String sql = "UPDATE WalletInventory SET inventory = ? WHERE walletUUID = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, newInventory);
            pstmt.setString(2, walletUUID.toString());
            int affectedRows = pstmt.executeUpdate();
            dbConnection.commit();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Failed to update wallet inventory for UUID: " + walletUUID, e);
            return false;
        }
    }

    /**
     * Deletes a wallet entry from the WalletInventory table.
     *
     * @param walletUUID The unique identifier for the wallet.
     * @return true if the wallet entry was deleted successfully, false otherwise.
     */
    protected boolean deleteWallet(UUID walletUUID) {
        String sql = "DELETE FROM WalletInventory WHERE walletUUID = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, walletUUID.toString());
            int affectedRows = pstmt.executeUpdate();
            dbConnection.commit();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Failed to delete wallet entry for UUID: " + walletUUID, e);
            return false;
        }
    }
}

