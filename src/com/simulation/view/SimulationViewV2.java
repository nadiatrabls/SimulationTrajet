// Version 2 de l'application avec publicités, historique des utilisateurs et rétribution financière

package com.simulation.view;
import com.simulation.view.EnregistrerAnnonceurView;

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
import com.simulation.view.EnregistrerAnnonceurView;

public class SimulationViewV2 extends JFrame {

    private JTextField puissanceField, capaciteField, distanceField, vitesseField, matriculeField;
    private JLabel carLabel, depannageLabel, coutEssenceLabel, nbPleinsLabel, coutDepannageLabel, nbDepannagesLabel, coutTotalLabel, retributionLabel;
    private Timer carTimer, blinkTimer;
    private int carXPosition = 0;
    private JPanel resultPanel;
    private SimulationController controller;
    private JLabel depannageIconLabel;
    private JLabel essenceIconLabel;

    public SimulationViewV2() {
        controller = new SimulationController();
        afficherPubliciteDebut();
        initComponents();
        startCarAnimation();
        calculerRetributionMensuelle();
    }

    private void afficherPubliciteDebut() {
        afficherPublicite("/com/simulation/view/pub_debut.png", "Découvrez notre partenaire auto !");
    }

    private void afficherPubliciteFin() {
        afficherPublicite("/com/simulation/view/pub_fin.png", "Merci d'utiliser notre application. Découvrez nos autres services !");
    }

    private void afficherPublicite(String imagePath, String message) {
        URL pubImageUrl = getClass().getResource(imagePath);
        if (pubImageUrl != null) {
            ImageIcon pubImage = new ImageIcon(pubImageUrl);
            Image resizedImage = pubImage.getImage().getScaledInstance(400, 200, Image.SCALE_SMOOTH);
            JOptionPane.showMessageDialog(this, message, "Publicité", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(resizedImage));
        } else {
            JOptionPane.showMessageDialog(this, message, "Publicité", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void ouvrirEnregistrerAnnonceur() {
        SwingUtilities.invokeLater(() -> new EnregistrerAnnonceurView().setVisible(true));
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
     // Chargement de l'icône de dépanneuse
        URL depannageImageUrl = getClass().getResource("/com/simulation/view/tow_truck.png");
        if (depannageImageUrl != null) {
            ImageIcon depannageIcon = new ImageIcon(new ImageIcon(depannageImageUrl).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
            depannageIconLabel = new JLabel(depannageIcon);
            depannageIconLabel.setBounds(250, 530, 40, 40); 
            add(depannageIconLabel);
        }

        // Chargement de l'icône de pompe à essence
        URL essenceImageUrl = getClass().getResource("/com/simulation/view/fuel.png");
        if (essenceImageUrl != null) {
            ImageIcon essenceIcon = new ImageIcon(new ImageIcon(essenceImageUrl).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
            essenceIconLabel = new JLabel(essenceIcon);
            essenceIconLabel.setBounds(500, 530, 40, 40);
            add(essenceIconLabel);
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

        chargerImageVoiture();

        JButton simulateButton = new JButton("SIMULER");
        simulateButton.setBounds(325, 310, 150, 40);
        simulateButton.setBackground(new Color(187, 140, 104));
        simulateButton.setForeground(Color.WHITE);
        simulateButton.setFont(new Font("Arial", Font.BOLD, 18));
        simulateButton.addActionListener(this::simulerTrajet);
        add(simulateButton);

        creerResultPanel();

        JButton quitterButton = new JButton("QUITTER");
        quitterButton.setBounds(325, 540, 150, 40);
        quitterButton.setBackground(new Color(187, 140, 104));
        quitterButton.setForeground(Color.WHITE);
        quitterButton.setFont(new Font("Arial", Font.BOLD, 18));
        quitterButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment quitter ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                afficherPubliciteFin();
                System.exit(0);
            }
        });
        
        add(quitterButton);
        JButton enregistrerAnnonceurButton = new JButton("Enregistrer Annonceur");
        enregistrerAnnonceurButton.setBounds(325, 700, 200, 40);
        enregistrerAnnonceurButton.setBackground(new Color(60, 120, 200));
        enregistrerAnnonceurButton.setForeground(Color.WHITE);
        enregistrerAnnonceurButton.setFont(new Font("Arial", Font.BOLD, 14));
        enregistrerAnnonceurButton.addActionListener(e -> ouvrirEnregistrerAnnonceur());
        add(enregistrerAnnonceurButton);

        JButton syntheseButton = new JButton("📊 Voir Synthèse");
        syntheseButton.setBounds(550, 700, 200, 40);
        syntheseButton.setBackground(new Color(50, 150, 250));
        syntheseButton.setForeground(Color.WHITE);
        syntheseButton.setFont(new Font("Arial", Font.BOLD, 16));
        syntheseButton.addActionListener(e -> new SyntheseView().setVisible(true));
        add(syntheseButton);

    }

    private void chargerImageVoiture() {
        URL carImageUrl = getClass().getResource("/com/simulation/view/car.png");
        if (carImageUrl != null) {
            ImageIcon carIcon = new ImageIcon(carImageUrl);
            Image carImage = carIcon.getImage().getScaledInstance(60, 30, Image.SCALE_SMOOTH);
            carLabel = new JLabel(new ImageIcon(carImage));
            carLabel.setBounds(20, 60, 60, 30);
            add(carLabel);
        }
    }

    private void creerResultPanel() {
        resultPanel = new JPanel();
        resultPanel.setBounds(100, 370, 600, 150);
        resultPanel.setBackground(new Color(210, 180, 160));
        resultPanel.setLayout(new GridLayout(4, 2, 10, 10));

        coutDepannageLabel = new JLabel("Coût de Dépannage : XX€", JLabel.CENTER);
        coutEssenceLabel = new JLabel("Coût d'Essence : XX€", JLabel.CENTER);
        nbDepannagesLabel = new JLabel("NB de Dépannages: XX", JLabel.CENTER);
        nbPleinsLabel = new JLabel("NB de Pleins: XX", JLabel.CENTER);
        depannageLabel = new JLabel("", SwingConstants.CENTER);
        coutTotalLabel = new JLabel("Coût Total: XX€", JLabel.CENTER);
        retributionLabel = new JLabel("Rétribution Mensuelle : XX€", JLabel.CENTER);

        resultPanel.add(coutDepannageLabel);
        resultPanel.add(coutEssenceLabel);
        resultPanel.add(nbDepannagesLabel);
        resultPanel.add(nbPleinsLabel);
        resultPanel.add(depannageLabel);
        resultPanel.add(coutTotalLabel);
        resultPanel.add(retributionLabel);

        add(resultPanel);
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
    private void startBlinking() {
        blinkTimer = new Timer(500, e -> {
            depannageLabel.setVisible(!depannageLabel.isVisible());
            coutDepannageLabel.setVisible(!coutDepannageLabel.isVisible());
            coutEssenceLabel.setVisible(!coutEssenceLabel.isVisible());

            // Faire clignoter les icônes d'image
            if (depannageIconLabel != null) {
                depannageIconLabel.setVisible(!depannageIconLabel.isVisible());
            }
            if (essenceIconLabel != null) {
                essenceIconLabel.setVisible(!essenceIconLabel.isVisible());
            }
        });
        blinkTimer.start();
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

    private void simulerTrajet(ActionEvent e) {
        try {
            int puissance = Integer.parseInt(puissanceField.getText());
            double capacite = Double.parseDouble(capaciteField.getText());
            double distance = Double.parseDouble(distanceField.getText());
            double vitesse = Double.parseDouble(vitesseField.getText());
            String matricule = matriculeField.getText().toUpperCase().trim();

            Vehicule vehicule = new Vehicule(puissance, capacite);
            Trajet trajet = controller.simulerTrajet(vehicule, distance, vitesse);

            // ✅ Calcul du carburant et des pleins
            double carburantDisponible = capacite; // Réservoir plein au départ
            double carburantUtilisé = trajet.getConsommationTotale();
            int nbPleins = (carburantUtilisé > carburantDisponible) ? (int) Math.ceil(carburantUtilisé / capacite) : 0;

            // ✅ Mise à jour des résultats
            coutEssenceLabel.setText(String.format("Coût d'Essence : %.2f€", trajet.getCoutEssence()));
            nbPleinsLabel.setText(String.format("NB de Pleins: %d", nbPleins));
            coutDepannageLabel.setText(String.format("Coût de Dépannage : %.2f€", trajet.getCoutDepannage()));
            nbDepannagesLabel.setText(String.format("NB de Dépannages: %d", trajet.getNbPleins() - 1));
            coutTotalLabel.setText(String.format("Coût Total: %.2f€", trajet.getCoutTotal()));

            // ✅ Gestion du dépannage
            if (trajet.isPanne()) {
                depannageLabel.setText("DÉPANNAGE NÉCESSAIRE");
                depannageLabel.setForeground(Color.WHITE);
                depannageLabel.setBackground(Color.RED);
                depannageLabel.setOpaque(true);

                startBlinking(); // 🔥 Active le clignotement
                carTimer.stop();
            } else {
                depannageLabel.setText("PAS BESOIN D'UN DÉPANNAGE");
                depannageLabel.setForeground(Color.WHITE);
                depannageLabel.setBackground(Color.GREEN);
                depannageLabel.setOpaque(true);

                if (blinkTimer != null && blinkTimer.isRunning()) {
                    blinkTimer.stop();
                }

                // Rendre les icônes visibles en permanence si tout va bien
                //if (depannageIconLabel != null) depannageIconLabel.setVisible(true);
                //if (essenceIconLabel != null) essenceIconLabel.setVisible(true);

                startCarAnimation();
            }



            enregistrerHistoriqueUtilisateur(matricule);
            calculerRetributionMensuelle();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculerRetributionMensuelle() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT COUNT(DISTINCT matricule) AS nb_utilisateurs " +
                    "FROM historique_utilisateurs " +
                    "WHERE DATE_FORMAT(date_utilisation, '%Y-%m') = DATE_FORMAT(NOW(), '%Y-%m')";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int nbUtilisateurs = rs.getInt("nb_utilisateurs");
                double retribution = nbUtilisateurs * 1.0;
                retributionLabel.setText(String.format("Rétribution Mensuelle : %.2f€", retribution));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du calcul de la rétribution : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimulationViewV2().setVisible(true));
    }
}
