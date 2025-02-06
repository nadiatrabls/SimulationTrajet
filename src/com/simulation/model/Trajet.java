package com.simulation.model;

public class Trajet {
    private double distance;
    private double vitesse;
    private Vehicule vehicule;
    private double consommationTotale;
    private int nbPleins;
    private boolean panne;
    private double coutEssence;
    private double coutDepannage;
    private double coutTotal;

    // Constructeur pour utiliser les calculs depuis le SimulationController
    public Trajet(double distance, double vitesse, Vehicule vehicule, 
                  double consommationTotale, int nbPleins, boolean panne, 
                  double coutEssence, double coutDepannage, double coutTotal) {
        this.distance = distance;
        this.vitesse = vitesse;
        this.vehicule = vehicule;
        this.consommationTotale = consommationTotale;
        this.nbPleins = nbPleins;
        this.panne = panne;
        this.coutEssence = coutEssence;
        this.coutDepannage = coutDepannage;
        this.coutTotal = coutTotal;
    }

    // Constructeur si les calculs doivent être faits dans cette classe
    public Trajet(double distance, double vitesse, Vehicule vehicule) {
        this.distance = distance;
        this.vitesse = vitesse;
        this.vehicule = vehicule;
        calculerCout(vehicule);
    }

    private void calculerCout(Vehicule vehicule) {
        double consommationPar100Km = calculerConsommationPar100Km(vitesse, vehicule.getPuissanceCV());
        double distanceRestante = distance;
        consommationTotale = 0;
        int nbDepannages = 0;

        while (distanceRestante > 0) {
            // Calcul de l'autonomie restante en kilomètres avec le carburant actuel
            double autonomieKm = (vehicule.getCarburantRestant() * 100) / consommationPar100Km;

            if (autonomieKm >= distanceRestante) {
                // La voiture peut finir le trajet sans panne
                double consommationPourTrajet = (distanceRestante / 100) * consommationPar100Km;
                vehicule.consommerCarburant(consommationPourTrajet);
                consommationTotale += consommationPourTrajet;
                distanceRestante = 0;
            } else {
                // La voiture tombe en panne avant la fin
                double consommationAvantPanne = vehicule.getCarburantRestant();
                vehicule.consommerCarburant(consommationAvantPanne);
                consommationTotale += consommationAvantPanne;
                distanceRestante -= autonomieKm;

                // Dépannage : on fait un plein et on ajoute le coût
                nbDepannages++;
                vehicule.remplirReservoir();  // Remplir le réservoir après la panne
            }
        }

        // Calcul des coûts
        coutEssence = consommationTotale * 1;  // 1€ par litre
        coutDepannage = nbDepannages * 100;    // 100€ par panne
        coutTotal = coutEssence + coutDepannage;
        
        panne = nbDepannages > 0;  // S'il y a au moins une panne
        nbPleins = nbDepannages + 1;  // Un plein initial + un plein après chaque panne
    }


    private double calculerConsommationPar100Km(double vitesse, int puissanceCV) {
        double baseConsommation;
        if (vitesse <= 80) {
            baseConsommation = 6;
        } else if (vitesse <= 100) {
            baseConsommation = 7;
        } else if (vitesse <= 120) {
            baseConsommation = 8;
        } else if (vitesse <= 130) {
            baseConsommation = 9;
        } else {
            throw new IllegalArgumentException("La vitesse maximale est de 130 km/h.");
        }

        // Application du coefficient de 0,15% par cheval au-delà de 4 chevaux
        if (puissanceCV > 4) {
            baseConsommation += baseConsommation * 0.0015 * (puissanceCV - 4);
        }

        return baseConsommation;
    }

    // Getters
    public double getDistance() {
        return distance;
    }

    public double getVitesse() {
        return vitesse;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public double getConsommationTotale() {
        return consommationTotale;
    }

    public int getNbPleins() {
        return nbPleins;
    }

    public boolean isPanne() {
        return panne;
    }

    public double getCoutEssence() {
        return coutEssence;
    }

    public double getCoutDepannage() {
        return coutDepannage;
    }

    public double getCoutTotal() {
        return coutTotal;
    }
} 
