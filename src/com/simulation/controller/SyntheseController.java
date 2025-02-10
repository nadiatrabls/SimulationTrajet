package com.simulation.controller;

import com.simulation.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SyntheseController {

    public String genererSynthese() {
        StringBuilder synthese = new StringBuilder();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT COUNT(id) AS total_simulations, " +
                         "SUM(cout_total) AS total_cout, " +
                         "SUM(consommation_total) AS total_carburant, " +
                         "SUM(CASE WHEN panne = 1 THEN 1 ELSE 0 END) AS total_pannes " +
                         "FROM simulations";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int totalSimulations = rs.getInt("total_simulations");
                double totalCout = rs.getDouble("total_cout");
                double totalCarburant = rs.getDouble("total_carburant");
                int totalPannes = rs.getInt("total_pannes");

                synthese.append("📊 **Synthèse des simulations** 📊\n\n")
                        .append("🔹 **Nombre total de simulations** : ").append(totalSimulations).append("\n")
                        .append("💰 **Coût total des trajets** : ").append(String.format("%.2f €", totalCout)).append("\n")
                        .append("⛽ **Total carburant consommé** : ").append(String.format("%.2f L", totalCarburant)).append("\n")
                        .append("🚧 **Nombre total de pannes** : ").append(totalPannes).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Erreur lors de la récupération des statistiques.";
        }
        
        return synthese.toString();
    }
}
