package org.polytech.algorithm.globalsearch;

import org.polytech.algorithm.LocalSearchType;
import org.polytech.model.Route;
import org.polytech.model.Tour;

import java.util.Random;

public class SimulatedAnnealing {
    private int maxTemperatureChange;
    private int movesAtTemperatureTk = 10000;
    private double mu;
    private Random random;

    private int weightStart;

    public SimulatedAnnealing(int maxTemperatureChange, double mu) {
        this.maxTemperatureChange = maxTemperatureChange;
        this.random = new Random();
        this.mu = mu;
    }

    public Tour explore(Tour initialSolution, double initialTemperature) {
        System.out.println("Exploration des solutions");
        Tour minTour = new Tour(initialSolution.getRoutes());
        Tour siblingTour;
        Tour currentTour=new Tour(initialSolution.getRoutes());
        double distance;
        double p;
        weightStart= minTour.getTotalWeight();

        double currentTemperature = initialTemperature;

        for (int i = 0; i <= this.maxTemperatureChange; i++) {
            for (int j = 1; j <= this.movesAtTemperatureTk; j++) {
                siblingTour = selectRandomSibling(minTour);
                distance = siblingTour.distance() - currentTour.distance();
                if(distance <= 0) {
                    currentTour = new Tour(siblingTour.getRoutes());
                    if (currentTour.distance() < minTour.distance()) {
                        minTour = new Tour(currentTour.getRoutes());
                        if(weightStart != minTour.getTotalWeight())
                        {
                            weightStart= minTour.getTotalWeight();
                        }
                    }
                }
                else {
                    p = this.random.nextDouble(0, 1);
                    if(p <= Math.exp(-distance / currentTemperature)) {
                        currentTour = siblingTour;
                    }
                }
            }
            currentTemperature *= mu;
        }
        System.out.println("Fin de l'optimisation");
        return minTour;
    }

    protected Tour selectRandomSibling(Tour previous) {
        Tour newTour = new Tour(previous);
        LocalSearchType randomSiblingType= LocalSearchType.values()[this.random.nextInt(LocalSearchType.values().length)];

        switch (randomSiblingType) {
            case ECHANGE_INTRA -> {
                int numberOfRoutes = newTour.getRoutes().size();
                int randomIndexTour = this.random.nextInt(numberOfRoutes);

                Route route = newTour.getRoutes().get(randomIndexTour);
                int numberOfLivraisons = route.getLivraisons().size();
                if(numberOfLivraisons == 1) {
                    return newTour;
                }

                int indexClient1 = this.random.nextInt(numberOfLivraisons);
                int indexClient2 = this.random.nextInt(numberOfLivraisons);
                while (indexClient1 == indexClient2) {
                    indexClient2 = this.random.nextInt(numberOfLivraisons);
                }

                route.tryExchangeIntra(indexClient1, indexClient2);
            }
            case RELOCATE_INTRA -> {
                int numberOfRoutes = newTour.getRoutes().size();
                int randomIndexTour = this.random.nextInt(numberOfRoutes);

                Route route = newTour.getRoutes().get(randomIndexTour);
                int numberOfLivraisons = route.getLivraisons().size();

                if(numberOfLivraisons == 1) {
                    return newTour;
                }

                int oldIndexClient1 = this.random.nextInt(numberOfLivraisons);
                int newIndexClient1  = this.random.nextInt(numberOfLivraisons);
                while (oldIndexClient1 == newIndexClient1) {
                    newIndexClient1 = this.random.nextInt(numberOfLivraisons);
                }

                route.tryRelocateIntra(oldIndexClient1, newIndexClient1);
            }

            case RELOCATE_INTER -> {
                int numberOfRoutes = newTour.getRoutes().size();
                if(numberOfRoutes <= 1) {
                    return newTour;
                }

                int indexRandomRoute1 = this.random.nextInt(numberOfRoutes);
                int indexRandomRoute2 = this.random.nextInt(numberOfRoutes);
                while (indexRandomRoute1 == indexRandomRoute2) {
                    indexRandomRoute2 = this.random.nextInt(numberOfRoutes);
                }

                int totalLivraisonsRoute1 = newTour.getRoutes().get(indexRandomRoute1).getLivraisons().size();
                int indexRandomLivraisonRoute1 = this.random.nextInt(totalLivraisonsRoute1);

                int totalLivraisonsRoute2 = newTour.getRoutes().get(indexRandomRoute2).getLivraisons().size();
                int indexRandomLivraisonRoute2 = this.random.nextInt(totalLivraisonsRoute2);
                newTour.tryRelocateInter(indexRandomRoute1, indexRandomLivraisonRoute1, indexRandomRoute2, indexRandomLivraisonRoute2);

            }
            case ECHANGE_GROUPE_INTER -> {
                int numberOfRoutes = newTour.getRoutes().size();
                if(numberOfRoutes <= 1) {
                    return newTour;
                }

                int indexRandomRoute1 = this.random.nextInt(numberOfRoutes);
                int indexRandomRoute2 = this.random.nextInt(numberOfRoutes);
                while (indexRandomRoute1 == indexRandomRoute2) {
                    indexRandomRoute2 = this.random.nextInt(numberOfRoutes);
                }

                int totalLivraisonsRoute1 = newTour.getRoutes().get(indexRandomRoute1).getLivraisons().size();
                int totalLivraisonsRoute2 = newTour.getRoutes().get(indexRandomRoute2).getLivraisons().size();

                if(totalLivraisonsRoute1 <= 1 || totalLivraisonsRoute2 <= 1)
                {
                    return newTour;
                }

                int indexRandomLivraisonRoute11 = this.random.nextInt(0,totalLivraisonsRoute1-1);
                int indexRandomLivraisonRoute12 = this.random.nextInt(indexRandomLivraisonRoute11+1, totalLivraisonsRoute1);

                int indexRandomLivraisonRoute21 = this.random.nextInt(0,totalLivraisonsRoute2-1);
                int indexRandomLivraisonRoute22 = this.random.nextInt(indexRandomLivraisonRoute21+1,totalLivraisonsRoute2);

                newTour.tryInterGroupeExchange(indexRandomRoute1, indexRandomLivraisonRoute11,indexRandomLivraisonRoute12,indexRandomRoute2,indexRandomLivraisonRoute21,indexRandomLivraisonRoute22);
            }
        }

        return newTour;
    }
}
