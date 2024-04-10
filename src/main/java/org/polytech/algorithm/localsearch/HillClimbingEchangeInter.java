package org.polytech.algorithm.localsearch;

import org.polytech.model.Route;
import org.polytech.model.Tour;

public class HillClimbingEchangeInter extends HillClimbing {

    public HillClimbingEchangeInter(Tour initial) {
        super(initial);
    }

    @Override
    public Tour exploreSiblings(Tour initial) {
        Tour bestTour = new Tour(initial.getRoutes());
        Tour siblingTour;
        for (int indexRoute1 = 0; indexRoute1 < bestTour.getRoutes().size(); indexRoute1++) {
            for (int indexRoute2 = indexRoute1 + 1; indexRoute2 < bestTour.getRoutes().size(); indexRoute2++) {
                for (int indiceLivraisonRoute1 = 0; indiceLivraisonRoute1 < bestTour.getRoutes().get(indexRoute1).getLivraisons().size(); indiceLivraisonRoute1++) {
                    for (int indiceLivraisonRoute2 = 0; indiceLivraisonRoute2 < bestTour.getRoutes().get(indexRoute2).getLivraisons().size(); indiceLivraisonRoute2++) {
                        siblingTour = new Tour(initial.getRoutes());
                        siblingTour.tryInterExchange(indexRoute1, indiceLivraisonRoute1, indexRoute2, indiceLivraisonRoute2);
                        if (siblingTour.distance() < bestTour.distance()) {
                            bestTour = siblingTour;
                        }
                    }
                }
            }
        }
        return bestTour;
    }

    @Override
    protected Route exploreSiblings(Route route) {
        return null;
    }
}
