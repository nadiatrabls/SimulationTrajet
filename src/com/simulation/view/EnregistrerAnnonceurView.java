package com.simulation.view;

import com.simulation.controller.AnnonceurController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

public class EnregistrerAnnonceurView extends JFrame {

    private JTextField nomField;
    private JTextField entrepriseField;
    private JTextField emailField;
    private JLabel imageLabel;
    private File selectedImage;
    private AnnonceurController controller;

    public EnregistrerAnnonceurView() {
        controller = new AnnonceurController();
        initComponents();
    }

    private void initComponents() {
        setTitle("Enregistrer un Annonceur");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));
        
        JLabel nomLabel = new JLabel("Nom de l'Annonceur :");
        nomField = new JTextField();

        JLabel entrepriseLabel = new JLabel("Entreprise :");
        entrepriseField = new JTextField();

        JLabel emailLabel = new JLabel("Email :");
        emailField = new JTextField();

        JButton uploadButton = new JButton("Télécharger Image");
        imageLabel = new JLabel("Aucune image sélectionnée", SwingConstants.CENTER);
        uploadButton.addActionListener(this::uploadImage);

        JButton enregistrerButton = new JButton("Enregistrer");
        enregistrerButton.addActionListener(this::enregistrerAnnonceur);

        add(nomLabel);
        add(nomField);
        add(entrepriseLabel);
        add(entrepriseField);
        add(emailLabel);
        add(emailField);
        add(uploadButton);
        add(imageLabel);
        add(enregistrerButton);
    }

    private void uploadImage(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png"));
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedImage = fileChooser.getSelectedFile();
            imageLabel.setText(selectedImage.getName());
        }
    }

    private void enregistrerAnnonceur(ActionEvent e) {
        String nom = nomField.getText().trim();
        String entreprise = entrepriseField.getText().trim();
        String email = emailField.getText().trim();
        String imagePath = (selectedImage != null) ? selectedImage.getAbsolutePath() : "";

        if (nom.isEmpty() || entreprise.isEmpty() || email.isEmpty() || imagePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = AnnonceurController.enregistrerAnnonceur(nom, entreprise, email, imagePath);

        if (success) {
            JOptionPane.showMessageDialog(this, "Annonceur enregistré avec succès !");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

    }
}
