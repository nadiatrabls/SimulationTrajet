package com.simulation.view;

import com.simulation.controller.SimulationController;
import com.simulation.database.DatabaseConnection;
import com.simulation.model.Trajet;
import com.simulation.model.Vehicule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;

public class SimulationView extends JFrame {

    private JTextField puissanceField;
    private JTextField capaciteField;
    private JTextField distanceField;
    private JTextField vitesseField;
    private JTextField matriculeField;
    private JLabel resultLabel;
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
    private SimulationController controller;

    public SimulationView() {
        controller = new SimulationController();
        initComponents();
        startCarAnimation();
    }

    private void initComponents() {
        setTitle("Simulation de Trajet en Voiture");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        URL imageUrl = getClass().getResource("/com/simulation/view/utilisateur.png");
        if (imageUrl == null) {
            JOptionPane.showMessageDialog(this, "L'image utilisateur.png n'a pas été trouvée.", "Erreur de chargement", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        ImageIcon backgroundImage = new ImageIcon(imageUrl);
        setContentPane(new JLabel(backgroundImage));
        setLayout(null);

        JLabel titleLabel = new JLabel("Simulation de trajet en voiture", SwingConstants.CENTER);
        titleLabel.setBounds(150, 20, 500, 50);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Serif", Font.PLAIN, 36));
        add(titleLabel);

        JLabel labelPuissance = createLabel("Puissance du Véhicule (CV)", 100, 100);
        puissanceField = createTextField(100, 130);

        JLabel labelCapacite = createLabel("Capacité du Réservoir (L)", 100, 170);
        capaciteField = createTextField(100, 200);

        JLabel labelMatricule = createLabel("Matricule (XX-YYY-XX)", 100, 240);
        matriculeField = createTextField(100, 270);

        JLabel labelDistance = createLabel("Distance (KM)", 500, 100);
        distanceField = createTextField(500, 130);

        JLabel labelVitesse = createLabel("Vitesse (KM/H)", 500, 170);
        vitesseField = createTextField(500, 200);

        URL carImageUrl = getClass().getResource("/com/simulation/view/car.png");
        if (carImageUrl == null) {
            JOptionPane.showMessageDialog(this, "L'image car.png n'a pas été trouvée.", "Erreur de chargement", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        ImageIcon carIcon = new ImageIcon(carImageUrl);
        Image carImage = carIcon.getImage().getScaledInstance(60, 30, Image.SCALE_SMOOTH);
        carLabel = new JLabel(new ImageIcon(carImage));
        carLabel.setBounds(20, 60, 60, 30);
        add(carLabel);

        carTimer = new Timer(20, e -> moveCar());

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
        resultPanel.setLayout(new GridLayout(3, 2, 10, 10));
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
        coutTotalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resultPanel.add(coutTotalLabel);

        JButton retourButton = new JButton("RETOUR");
        retourButton.setBounds(200, 540, 150, 40);
        retourButton.setBackground(new Color(255, 228, 225));
        retourButton.setForeground(new Color(67, 47, 36));
        retourButton.setFont(new Font("Arial", Font.BOLD, 16));
        retourButton.addActionListener(e -> resetForm());
        add(retourButton);

        JButton nouvelleSimulationButton = new JButton("NOUVELLE SIMULATION");
        nouvelleSimulationButton.setBounds(400, 540, 250, 40);
        nouvelleSimulationButton.setBackground(new Color(187, 140, 104));
        nouvelleSimulationButton.setForeground(Color.WHITE);
        nouvelleSimulationButton.setFont(new Font("Arial", Font.BOLD, 16));
        nouvelleSimulationButton.addActionListener(e -> resetForm());
        add(nouvelleSimulationButton);
    }

    private Icon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    private void startCarAnimation() {
        carXPosition = 0;
        carTimer.start();
    }

    private JLabel createLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 200, 25);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        add(label);
        return label;
    }

    private JTextField createTextField(int x, int y) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, 200, 25);
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

            if (vitesse > 130) {
                throw new IllegalArgumentException("La vitesse maximale autorisée est de 130 km/h.");
            }

            if (!matricule.matches("[A-Z]{2}-\\d{3}-[A-Z]{2}")) {
                throw new IllegalArgumentException("Le matricule doit être au format XX-YYY-XX.");
            }

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

            DatabaseConnection.enregistrerSimulation(puissance, capacite, distance, vitesse, matricule,
                    trajet.getConsommationTotale(), trajet.getCoutTotal(), trajet.isPanne());

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs correctement.", "Erreur", JOptionPane.ERROR_MESSAGE);
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

    private void moveCar() {
        if (carXPosition < getWidth()) {
            carXPosition += 5;
            carLabel.setLocation(carXPosition, carLabel.getY());
        } else {
            carXPosition = -carLabel.getWidth();
        }
    }

    private void resetForm() {
        puissanceField.setText("");
        capaciteField.setText("");
        distanceField.setText("");
        vitesseField.setText("");
        matriculeField.setText("");
        depannageLabel.setText("");
        coutEssenceLabel.setText("Coût d'Essence : XX€");
        nbPleinsLabel.setText("NB de Pleins: XX");
        coutDepannageLabel.setText("Coût de Dépannage : XX€");
        nbDepannagesLabel.setText("NB de Dépannages: XX");
        coutTotalLabel.setText("Coût Total: XX€");
        if (blinkTimer != null && blinkTimer.isRunning()) {
            blinkTimer.stop();
        }
        startCarAnimation();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimulationView().setVisible(true));
    }
}
