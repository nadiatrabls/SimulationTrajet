package com.simulation.view;

import com.simulation.controller.SyntheseController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SyntheseView extends JFrame {

    private JTextArea syntheseTextArea;
    private SyntheseController controller;

    public SyntheseView() {
        controller = new SyntheseController();
        initComponents();
    }

    private void initComponents() {
        setTitle("Synthèse des Simulations");
        setSize(600, 400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titleLabel = new JLabel("Synthèse des Simulations", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        syntheseTextArea = new JTextArea();
        syntheseTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        syntheseTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(syntheseTextArea);
        add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("🔄 Actualiser");
        refreshButton.addActionListener(this::rafraichirSynthese);
        add(refreshButton, BorderLayout.SOUTH);

        rafraichirSynthese(null); // Charger les données au lancement
    }

    private void rafraichirSynthese(ActionEvent e) {
        String synthese = controller.genererSynthese();
        syntheseTextArea.setText(synthese);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SyntheseView().setVisible(true));
    }
}
