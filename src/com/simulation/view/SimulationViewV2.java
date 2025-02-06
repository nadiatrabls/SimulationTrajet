// Version 2 de l'application avec publicités, historique des utilisateurs et rétribution financière

package com.simulation.view;

import com.simulation.controller.SimulationController;
import com.simulation.database.DatabaseConnection;
import com.simulation.model.Trajet;
import com.simulation.model.Vehicule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class SimulationViewV2 extends JFrame {

    private JTextField puissanceField;
    private JTextField capaciteField;
    private JTextField distanceField;
    private JTextField vitesseField;
    private JTextField matriculeField;
    private JLabel carLabel;
    private Timer carTimer;
    private int carXPosition = 0;
    private JPanel resultPanel;
    private JLabel depannageLabel;
    private Timer blinkTimer;
    private JLabel coutEssenceLabel;
    private JLabel nbPleinsLabel;
    private JLabel coutDepannageLabel;
    private JLabel nbDepannagesLabel;
    private JLabel coutTotalLabel;
    private JLabel retributionLabel;
    private SimulationController controller;

    public SimulationViewV2() {
        controller = new SimulationController();
        afficherPubliciteDebut();
        initComponents();
        startCarAnimation();
        calculerRetributionMensuelle();
    }

    private void afficherPubliciteDebut() {
        URL pubImageUrl = getClass().getResource("/com/simulation/view/pub_debut.png");
        if (pubImageUrl != null) {
            ImageIcon pubImage = new ImageIcon(pubImageUrl);
            Image resizedImage = pubImage.getImage().getScaledInstance(400, 200, Image.SCALE_SMOOTH);
            JOptionPane.showMessageDialog(this, "Découvrez notre partenaire auto !", "Publicité", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(resizedImage));
        } else {
            JOptionPane.showMessageDialog(this, "Découvrez notre partenaire auto !", "Publicité", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void afficherPubliciteFin() {
        URL pubImageUrl = getClass().getResource("/com/simulation/view/pub_fin.png");
        if (pubImageUrl != null) {
            ImageIcon pubImage = new ImageIcon(pubImageUrl);
            Image resizedImage = pubImage.getImage().getScaledInstance(400, 200, Image.SCALE_SMOOTH);
            JOptionPane.showMessageDialog(this, "Merci d'utiliser notre application. Découvrez nos autres services !", "Publicité", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(resizedImage));
        } else {
            JOptionPane.showMessageDialog(this, "Merci d'utiliser notre application. Découvrez nos autres services !", "Publicité", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void initComponents() {
        setTitle("Simulation de Trajet en Voiture - Version 2");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        URL backgroundUrl = getClass().getResource("/com/simulation/view/utilisateur.png");
        if (backgroundUrl != null) {
            ImageIcon backgroundImage = new ImageIcon(backgroundUrl);
            setContentPane(new JLabel(backgroundImage));
            setLayout(null);
        }

        JLabel titleLabel = new JLabel("Simulation de trajet en voiture", SwingConstants.CENTER);
        titleLabel.setBounds(150, 20, 500, 50);
        titleLabel.setFont(new Font("Serif", Font.PLAIN, 36));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel);

        puissanceField = createTextField("Puissance du Véhicule (CV)", 100, 100);
        capaciteField = createTextField("Capacité du Réservoir (L)", 100, 170);
        matriculeField = createTextField("Matricule (XX-YYY-XX)", 100, 240);
        distanceField = createTextField("Distance (KM)", 500, 100);
        vitesseField = createTextField("Vitesse (KM/H)", 500, 170);

        URL carImageUrl = getClass().getResource("/com/simulation/view/car.png");
        if (carImageUrl != null) {
            ImageIcon carIcon = new ImageIcon(carImageUrl);
            Image carImage = carIcon.getImage().getScaledInstance(60, 30, Image.SCALE_SMOOTH);
            carLabel = new JLabel(new ImageIcon(carImage));
            carLabel.setBounds(20, 60, 60, 30);
            add(carLabel);
        }

        JButton simulateButton = new JButton("SIMULER");
        simulateButton.setBounds(325, 310, 150, 40);
        simulateButton.setBackground(new Color(187, 140, 104));
        simulateButton.setForeground(Color.WHITE);
        simulateButton.setFont(new Font("Arial", Font.BOLD, 18));
        simulateButton.addActionListener(this::simulerTrajet);
        add(simulateButton);

        resultPanel = new JPanel();
        resultPanel.setBounds(100, 370, 600, 150);
        resultPanel.setBackground(new Color(210, 180, 160));
        resultPanel.setLayout(new GridLayout(4, 2, 10, 10));
        add(resultPanel);

        coutDepannageLabel = new JLabel("Coût de Dépannage : XX€", JLabel.CENTER);
        coutDepannageLabel.setIcon(resizeIcon(new ImageIcon(getClass().getResource("/com/simulation/view/tow_truck.png")), 30, 30));
        resultPanel.add(coutDepannageLabel);

        coutEssenceLabel = new JLabel("Coût d'Essence : XX€", JLabel.CENTER);
        coutEssenceLabel.setIcon(resizeIcon(new ImageIcon(getClass().getResource("/com/simulation/view/fuel.png")), 30, 30));
        resultPanel.add(coutEssenceLabel);

        nbDepannagesLabel = new JLabel("NB de Dépannages: XX", JLabel.CENTER);
        resultPanel.add(nbDepannagesLabel);

        nbPleinsLabel = new JLabel("NB de Pleins: XX", JLabel.CENTER);
        resultPanel.add(nbPleinsLabel);

        depannageLabel = new JLabel("", SwingConstants.CENTER);
        depannageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        depannageLabel.setOpaque(true);
        resultPanel.add(depannageLabel);

        coutTotalLabel = new JLabel("Coût Total: XX€", JLabel.CENTER);
        resultPanel.add(coutTotalLabel);

        retributionLabel = new JLabel("Rétribution Mensuelle : XX€", JLabel.CENTER);
        resultPanel.add(retributionLabel);

        JButton quitterButton = new JButton("QUITTER");
        quitterButton.setBounds(325, 540, 150, 40);
        quitterButton.setBackground(new Color(187, 140, 104));
        quitterButton.setForeground(Color.WHITE);
        quitterButton.setFont(new Font("Arial", Font.BOLD, 18));
        quitterButton.addActionListener(e -> {
            afficherPubliciteFin();
            System.exit(0);
        });
        add(quitterButton);
    }

    private Icon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    private JTextField createTextField(String label, int x, int y) {
        JLabel jLabel = new JLabel(label);
        jLabel.setBounds(x, y, 200, 25);
        jLabel.setForeground(Color.WHITE);
        add(jLabel);

        JTextField textField = new JTextField();
        textField.setBounds(x, y + 30, 200, 25);
        add(textField);

        return textField;
    }

    private void simulerTrajet(ActionEvent e) {
        try {
            int puissance = Integer.parseInt(puissanceField.getText());
            double capacite = Double.parseDouble(capaciteField.getText());
            double distance = Double.parseDouble(distanceField.getText());
            double vitesse = Double.parseDouble(vitesseField.getText());
            String matricule = matriculeField.getText().toUpperCase().trim();

            Vehicule vehicule = new Vehicule(puissance, capacite);
            Trajet trajet = controller.simulerTrajet(vehicule, distance, vitesse);

            coutEssenceLabel.setText(String.format("Coût d'Essence : %.2f€", trajet.getCoutEssence()));
            nbPleinsLabel.setText(String.format("NB de Pleins: %d", trajet.getNbPleins()));
            coutDepannageLabel.setText(String.format("Coût de Dépannage : %.2f€", trajet.getCoutDepannage()));
            nbDepannagesLabel.setText(String.format("NB de Dépannages: %d", trajet.getNbPleins() - 1));
            coutTotalLabel.setText(String.format("Coût Total: %.2f€", trajet.getCoutTotal()));

            if (trajet.isPanne()) {
                depannageLabel.setText("DÉPANNAGE NÉCESSAIRE");
                depannageLabel.setBackground(Color.RED);
                depannageLabel.setForeground(Color.WHITE);
                startBlinking();
                carTimer.stop();
            } else {
                depannageLabel.setText("PAS BESOIN D'UN DÉPANNAGE");
                depannageLabel.setBackground(Color.GREEN);
                depannageLabel.setForeground(Color.WHITE);
                if (blinkTimer != null && blinkTimer.isRunning()) {
                    blinkTimer.stop();
                }
                startCarAnimation();
            }

            enregistrerHistoriqueUtilisateur(matricule);
            calculerRetributionMensuelle();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enregistrerHistoriqueUtilisateur(String matricule) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO historique_utilisateurs (matricule, date_utilisation) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, matricule);
            pstmt.setObject(2, LocalDateTime.now());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement de l'historique : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculerRetributionMensuelle() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT COUNT(DISTINCT matricule) AS nb_utilisateurs FROM historique_utilisateurs WHERE MONTH(date_utilisation) = MONTH(CURDATE()) AND YEAR(date_utilisation) = YEAR(CURDATE())";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int nbUtilisateurs = rs.getInt("nb_utilisateurs");
                double retribution = nbUtilisateurs * 1.0;  // 1€ par utilisateur
                retributionLabel.setText(String.format("Rétribution Mensuelle : %.2f€", retribution));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du calcul de la rétribution : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startBlinking() {
        blinkTimer = new Timer(500, e -> {
            depannageLabel.setVisible(!depannageLabel.isVisible());
            coutDepannageLabel.setVisible(!coutDepannageLabel.isVisible());
            coutEssenceLabel.setVisible(!coutEssenceLabel.isVisible());
        });
        blinkTimer.start();
    }

    private void startCarAnimation() {
        if (carLabel != null) {
            carXPosition = 0;
            carTimer = new Timer(20, e -> moveCar());
            carTimer.start();
        }
    }

    private void moveCar() {
        if (carLabel != null) {
            if (carXPosition < getWidth()) {
                carXPosition += 5;
                carLabel.setLocation(carXPosition, carLabel.getY());
            } else {
                carXPosition = -carLabel.getWidth();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimulationViewV2().setVisible(true));
    }
}
