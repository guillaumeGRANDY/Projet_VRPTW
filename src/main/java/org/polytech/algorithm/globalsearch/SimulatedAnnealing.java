package org.polytech.algorithm.globalsearch;

import org.polytech.algorithm.LocalSearchType;
import org.polytech.model.Route;
import org.polytech.model.Tour;

import java.util.Random;

public class SimulatedAnnealing{
    private int maxTemperatureChange;
    private int movesAtTemperatureTk = 500000;
    private double mu;

    private Random random;

    public SimulatedAnnealing(int maxTemperatureChange, double mu) {
        this.maxTemperatureChange = maxTemperatureChange;
        this.mu = mu;
        this.random = new Random();
    }

    public Tour explore(Tour initialSolution, double initialTemperature) {
        System.out.println("Exploration des solutions");
        Tour minTour = new Tour(initialSolution);
        Tour siblingTour;
        Tour currentTour=new Tour(initialSolution);
        double distance;
        double p;

        double currentTemperature = initialTemperature;
        int nbTemp = (int)(Math.log(Math.log(0.8) / Math.log(0.01))/Math.log(mu));
        for (int i = 0; i <= nbTemp; i++) {
            for (int j = 1; j <= this.movesAtTemperatureTk; j++) {
                siblingTour = selectRandomSibling(minTour);
                distance = siblingTour.distance() - currentTour.distance();

                if(distance <= 0) {
                    currentTour = new Tour(siblingTour.getRoutes());
                    if (currentTour.distance() < minTour.distance()) {
                        minTour = currentTour;
                    }
                }
                else {
                    p = this.random.nextDouble(0, 1);
                    if(p <= Math.exp(-distance / currentTemperature)) {
                        minTour = siblingTour;
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

        int numberOfRoutes = newTour.getRoutes().size();

        switch (randomSiblingType) {
            case ECHANGE_INTRA -> {
                Route route = newTour.getRoutes().get(this.random.nextInt(numberOfRoutes));

                int numberOfLivraisons = route.getLivraisons().size();
                if (numberOfLivraisons == 1) {
                    return newTour;
                }

                int index1 = this.random.nextInt(numberOfLivraisons);
                int index2 = this.random.nextInt(numberOfLivraisons);
                while (index1 == index2) {
                    index2 = this.random.nextInt(numberOfLivraisons);
                }

                route.tryExchangeIntra(index1, index2);
            }
            case ECHANGE_INTER -> {
                if(numberOfRoutes <= 1) {
                    return newTour;
                }

                int indexRoute1 = this.random.nextInt(numberOfRoutes);
                int indexRoute2 = this.random.nextInt(numberOfRoutes);
                while (indexRoute1 == indexRoute2) {
                    indexRoute2 = this.random.nextInt(numberOfRoutes);
                }

                int totalLivraisonsRoute1 = newTour.getRoutes().get(indexRoute1).getLivraisons().size();
                int totalLivraisonsRoute2 = newTour.getRoutes().get(indexRoute2).getLivraisons().size();
                if(totalLivraisonsRoute1 == 1 || totalLivraisonsRoute2 == 1) {
                    return newTour;
                }

                int indexLivraisonRoute1 = this.random.nextInt(totalLivraisonsRoute1);
                int indexLivraisonRoute2 = this.random.nextInt(totalLivraisonsRoute2);

                newTour.tryExchangeInter(indexRoute1, indexLivraisonRoute1, indexRoute2, indexLivraisonRoute2);
            }
            case RELOCATE_INTRA -> {
                Route route = newTour.getRoutes().get(this.random.nextInt(numberOfRoutes));
                int numberOfLivraisons = route.getLivraisons().size();

                if (numberOfLivraisons == 1) {
                    return newTour;
                }

                int oldIndexClient1 = this.random.nextInt(numberOfLivraisons);
                int newIndexClient1 = this.random.nextInt(numberOfLivraisons);
                while (oldIndexClient1 == newIndexClient1) {
                    newIndexClient1 = this.random.nextInt(numberOfLivraisons);
                }

                route.tryRelocateIntra(oldIndexClient1, newIndexClient1);
            }

            case RELOCATE_INTER -> {
                if (numberOfRoutes <= 1) {
                    return newTour;
                }

                int indexRoute1 = this.random.nextInt(numberOfRoutes);
                int indexRoute2 = this.random.nextInt(numberOfRoutes);
                while (indexRoute1 == indexRoute2) {
                    indexRoute2 = this.random.nextInt(numberOfRoutes);
                }


                int totalLivraisonsRoute1 = newTour.getRoutes().get(indexRoute1).getLivraisons().size();
                int totalLivraisonsRoute2 = newTour.getRoutes().get(indexRoute2).getLivraisons().size();
                if(totalLivraisonsRoute1 <= 1 || totalLivraisonsRoute2 <= 1) {
                    return newTour;
                }

                int indexLivraisonRoute1 = this.random.nextInt(totalLivraisonsRoute1);
                int indexLivraisonRoute2 = this.random.nextInt(totalLivraisonsRoute2);

                newTour.tryRelocateInter(indexRoute1, indexLivraisonRoute1, indexRoute2, indexLivraisonRoute2);

            }

            case ECHANGE_GROUPE_INTER -> {
                if(numberOfRoutes <= 1) {
                    return newTour;
                }

                int indexRoute1 = this.random.nextInt(numberOfRoutes);
                int indexRoute2 = this.random.nextInt(numberOfRoutes);
                while (indexRoute1 == indexRoute2) {
                    indexRoute2 = this.random.nextInt(numberOfRoutes);
                }

                int totalLivraisonsRoute1 = newTour.getRoutes().get(indexRoute1).getLivraisons().size();
                int totalLivraisonsRoute2 = newTour.getRoutes().get(indexRoute2).getLivraisons().size();

                if(totalLivraisonsRoute1 <= 1 || totalLivraisonsRoute2 <= 1) {
                    return newTour;
                }

                int indexLivraison1Route1 = this.random.nextInt(0,totalLivraisonsRoute1-1);
                int indexLivraison2Route1 = this.random.nextInt(indexLivraison1Route1+1, totalLivraisonsRoute1);

                int indexLivraison1Route2 = this.random.nextInt(0,totalLivraisonsRoute2-1);
                int indexLivraison2Route2 = this.random.nextInt(indexLivraison1Route2+1,totalLivraisonsRoute2);

                newTour.tryInterGroupeExchange(indexRoute1, indexLivraison1Route1,indexLivraison2Route1,indexRoute2,indexLivraison1Route2,indexLivraison2Route2);
            }
            case ECHANGE_GROUPE_INTRA -> {
                Route route = newTour.getRoutes().get(this.random.nextInt(numberOfRoutes));
                int numberOfLivraisons = route.getLivraisons().size();

                if (numberOfLivraisons == 1) {
                    return newTour;
                }

                int indiceOld1=this.random.nextInt(numberOfLivraisons-1);
                int indiceOld2=this.random.nextInt(indiceOld1,numberOfLivraisons);

                int indiceNew=this.random.nextInt(numberOfLivraisons);

                route.tryIntraGroupeExchange(indiceOld1,indiceOld2,indiceNew);
            }
            case RELOCATE_GROUPE_INTER -> {
                if(numberOfRoutes <= 1) {
                    return newTour;
                }

                int indexRoute1 = this.random.nextInt(numberOfRoutes);
                int indexRoute2 = this.random.nextInt(numberOfRoutes);
                while (indexRoute1 == indexRoute2) {
                    indexRoute2 = this.random.nextInt(numberOfRoutes);
                }

                int totalLivraisonsRoute1 = newTour.getRoutes().get(indexRoute1).getLivraisons().size();
                int totalLivraisonsRoute2 = newTour.getRoutes().get(indexRoute2).getLivraisons().size();

                if(totalLivraisonsRoute1 <= 1 || totalLivraisonsRoute2 <= 1) {
                    return newTour;
                }

                int indiceOld1=this.random.nextInt(totalLivraisonsRoute1-1);
                int indiceOld2=this.random.nextInt(indiceOld1,totalLivraisonsRoute1);
                int indiceNew=this.random.nextInt(totalLivraisonsRoute2);

                newTour.tryRelocateGroupeInter(indexRoute1, indiceOld1, indiceOld2, indexRoute2, indiceNew);
            }
            case MERGE -> {
                if(numberOfRoutes <= 1) {
                    return newTour;
                }

                int indexRoute1 = this.random.nextInt(numberOfRoutes);
                int indexRoute2 = this.random.nextInt(numberOfRoutes);
                while (indexRoute1 == indexRoute2) {
                    indexRoute2 = this.random.nextInt(numberOfRoutes);
                }

                int index=this.random.nextInt(newTour.getRoutes().get(indexRoute2).getLivraisons().size());

                newTour.tryMerge(indexRoute1, indexRoute2, index);
            }
        }

        if(newTour.getTotalWeight()!=previous.getTotalWeight())
        {
            System.out.println("Problème pour "+randomSiblingType+": "+previous.getTotalWeight()+" -> "+newTour.getTotalWeight());
        }

        return newTour;
    }
}
