package com.simulation.database;

import java.sql.Connection;  // Import correct

public class TestMySQLConnection {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("✅ Connexion à MySQL réussie !");
        } catch (Exception e) {
            System.out.println("❌ Échec de connexion : " + e.getMessage());
        }
    }
}
