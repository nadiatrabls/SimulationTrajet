package com.simulation.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.PreparedStatement;


public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/simulation_trajet"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; // Mets ici ton mot de passe MySQL

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void enregistrerSimulation(int puissance, double capacite, double distance, double vitesse,
            String matricule, double consommation, double coutTotal, boolean panne) {
String sql = "INSERT INTO simulations (puissance, capacite_reservoir, distance, vitesse, matricule, consommation_total, cout_total, panne) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

try (Connection conn = getConnection();
java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

pstmt.setInt(1, puissance);
pstmt.setDouble(2, capacite);
pstmt.setDouble(3, distance);
pstmt.setDouble(4, vitesse);
pstmt.setString(5, matricule);  // Ajout du matricule
pstmt.setDouble(6, consommation);  // consommation_total
pstmt.setDouble(7, coutTotal);
pstmt.setBoolean(8, panne);

pstmt.executeUpdate();

} catch (SQLException e) {
e.printStackTrace();
JOptionPane.showMessageDialog(null, "Erreur lors de l'enregistrement : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
}
}


public static void enregistrerAnnonceur(String nom, String entreprise, String email, String image) {
    String sql = "INSERT INTO annonceurs (nom, entreprise, email, image) VALUES (?, ?, ?, ?)";

    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, nom);
        pstmt.setString(2, entreprise);
        pstmt.setString(3, email);
        pstmt.setString(4, image); // Stocke le chemin de l’image

        pstmt.executeUpdate();

        JOptionPane.showMessageDialog(null, "Annonceur enregistré avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Erreur lors de l'enregistrement : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
}

