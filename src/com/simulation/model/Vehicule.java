package com.simulation.model;

public class Vehicule {
    private int puissanceCV;
    private double capaciteReservoir;
    private double carburantRestant;

    // Constructeur avec capacité automatique en fonction de la puissance
    public Vehicule(int puissanceCV) {
        if (puissanceCV < 4 || puissanceCV > 1500) {
            throw new IllegalArgumentException("La puissance doit être comprise entre 4 et 1500 chevaux.");
        }

        this.puissanceCV = puissanceCV;
        this.capaciteReservoir = (puissanceCV <= 6) ? 40 : 60;  // Capacité automatique
        this.carburantRestant = capaciteReservoir;  // Le réservoir est plein au départ
    }

    // Nouveau constructeur avec capacité définie manuellement
    public Vehicule(int puissanceCV, double capaciteReservoir) {
        if (puissanceCV < 4 || puissanceCV > 1500) {
            throw new IllegalArgumentException("La puissance doit être comprise entre 4 et 1500 chevaux.");
        }
        if (capaciteReservoir < 5 || capaciteReservoir > 150) {
            throw new IllegalArgumentException("La capacité du réservoir doit être comprise entre 5L et 150L.");
        }

        this.puissanceCV = puissanceCV;
        this.capaciteReservoir = capaciteReservoir;
        this.carburantRestant = capaciteReservoir;  // Le réservoir est plein au départ
    }

    // Getters
    public int getPuissanceCV() {
        return puissanceCV;
    }

    public double getCapaciteReservoir() {
        return capaciteReservoir;
    }

    public double getCarburantRestant() {
        return carburantRestant;
    }

    // Consommer le carburant, sans descendre en dessous de zéro
    public void consommerCarburant(double consommation) {
        carburantRestant -= consommation;
        if (carburantRestant < 0) {
            carburantRestant = 0;
        }
    }

    // Refaire le plein du réservoir
    public void remplirReservoir() {
        carburantRestant = capaciteReservoir;
    }

    // Vérifie si le véhicule a besoin de dépannage (réservoir vide)
    public boolean besoinDepannage() {
        return carburantRestant == 0;
    }
} 
