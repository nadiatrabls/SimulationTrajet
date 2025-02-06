package com.simulation.controller;

import com.simulation.model.Trajet;
import com.simulation.model.Vehicule;

public class SimulationController {

    public Trajet simulerTrajet(Vehicule vehicule, double distance, double vitesse) {
        if (vitesse <= 0 || vitesse > 130) {
            throw new IllegalArgumentException("La vitesse doit être comprise entre 1 km/h et 130 km/h.");
        }

        double consommationPar100km = calculerConsommation(vitesse, vehicule.getPuissanceCV());
        double consommationTotale = 0;
        int nbDepannages = 0;
        double distanceRestante = distance;

        while (distanceRestante > 0) {
            double autonomieKm = (vehicule.getCarburantRestant() * 100) / consommationPar100km;

            if (autonomieKm >= distanceRestante) {
                double consommationPourTrajet = (distanceRestante / 100) * consommationPar100km;
                vehicule.consommerCarburant(consommationPourTrajet);
                consommationTotale += consommationPourTrajet;
                distanceRestante = 0;
            } else {
                double consommationAvantPanne = vehicule.getCarburantRestant();
                vehicule.consommerCarburant(consommationAvantPanne);
                consommationTotale += consommationAvantPanne;
                distanceRestante -= autonomieKm;

                nbDepannages++;
                vehicule.remplirReservoir();
            }
        }

        double coutEssence = consommationTotale * 1;
        double coutDepannage = nbDepannages * 100;
        double coutTotal = coutEssence + coutDepannage;
        boolean panne = nbDepannages > 0;
        int nbPleins = nbDepannages + 1; // Le premier plein est déjà fait au départ

        return new Trajet(distance, vitesse, vehicule, consommationTotale, nbPleins, panne, coutEssence, coutDepannage, coutTotal);
    }

    private double calculerConsommation(double vitesse, int puissanceCV) {
        double baseConsommation;

        if (vitesse <= 80) {
            baseConsommation = 6;
        } else if (vitesse <= 100) {
            baseConsommation = 7;
        } else if (vitesse <= 120) {
            baseConsommation = 8;
        } else {
            baseConsommation = 9;
        }

        if (puissanceCV > 4) {
            baseConsommation += baseConsommation * 0.0015 * (puissanceCV - 4);
        }

        return baseConsommation;
    }
}