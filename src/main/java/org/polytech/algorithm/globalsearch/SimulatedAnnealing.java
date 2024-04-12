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

    public SimulatedAnnealing(int maxTemperatureChange, double mu) {
        this.maxTemperatureChange = maxTemperatureChange;
        this.random = new Random();
        this.mu = mu;
    }

    public Tour explore(Tour initialSolution, double initialTemperature) {
        Tour minTour = new Tour(initialSolution.getRoutes());
        Tour siblingTour;
        double distance;
        double p;

        double currentTemperature = initialTemperature;

        for (int i = 0; i <= this.maxTemperatureChange; i++) {
            for (int j = 1; j <= this.movesAtTemperatureTk; j++) {
                siblingTour = selectRandomSibling(minTour);
                distance = siblingTour.distance() - minTour.distance();
                if(distance <= 0) {
                    if(siblingTour.distance() < minTour.distance()) {
                        minTour = new Tour(siblingTour.getRoutes());
                        System.out.println(minTour.distance());
                    } else {
                        p = this.random.nextDouble(0, 1);
                        if(p <= Math.exp(-distance / currentTemperature)) {
                            minTour = siblingTour;
                        }
                    }
                }
            }
            currentTemperature *= mu;
        }

        return minTour;
    }

    protected Tour selectRandomSibling(Tour previous) {
        Tour newTour = new Tour(previous.getRoutes());

        LocalSearchType randomSiblingType = LocalSearchType.values()[this.random.nextInt(LocalSearchType.values().length)];
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

                route.tryExchangeClientPosition(indexClient1, indexClient2);
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
                int oldIndexClient2  = this.random.nextInt(numberOfLivraisons);
                while (oldIndexClient1 == oldIndexClient2) {
                    oldIndexClient2 = this.random.nextInt(numberOfLivraisons);
                }

                route.tryIntraRelocate(oldIndexClient1, oldIndexClient2);
            }
            case ECHANGE_INTER -> {
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

                newTour.tryInterExchange(indexRandomRoute1, indexRandomLivraisonRoute1, indexRandomRoute2, indexRandomLivraisonRoute2);
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
                newTour.tryInterRelocate(indexRandomRoute1, indexRandomLivraisonRoute1, indexRandomRoute2, indexRandomLivraisonRoute2);
            }
        }

        return newTour;
    }
}
