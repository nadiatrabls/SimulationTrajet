package com.simulation.controller;

import com.simulation.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AnnonceurController {

	public static boolean enregistrerAnnonceur(String nom, String entreprise, String email, String imagePath) {
	    String sql = "INSERT INTO annonceurs (nom, entreprise, email, image) VALUES (?, ?, ?, ?)";

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, nom);
	        pstmt.setString(2, entreprise);
	        pstmt.setString(3, email);
	        pstmt.setString(4, imagePath);

	        int rowsAffected = pstmt.executeUpdate();
	        return rowsAffected > 0; // Retourne true si une ligne a été insérée
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false; // Retourne false en cas d'échec
	    }
	}

}
