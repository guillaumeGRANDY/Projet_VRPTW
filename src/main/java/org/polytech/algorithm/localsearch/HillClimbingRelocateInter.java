package org.polytech.algorithm.localsearch;

import org.polytech.model.Route;
import org.polytech.model.Tour;

public class HillClimbingRelocateInter extends HillClimbing {

    public HillClimbingRelocateInter(Tour initial) {
        super(initial);
    }

    @Override
    public Tour exploreSiblings(Tour initial) {
        Tour bestTour = new Tour(initial.getRoutes());
        Tour siblingTour;
        for (int indexRoute1 = 0; indexRoute1 < initial.getRoutes().size(); indexRoute1++) {
            for (int indexRoute2 = indexRoute1 + 1; indexRoute2 < initial.getRoutes().size(); indexRoute2++) {
                for (int indiceLivraisonRoute1 = 0; indiceLivraisonRoute1 < initial.getRoutes().get(indexRoute1).getLivraisons().size(); indiceLivraisonRoute1++) {
                    for (int indiceLivraisonRoute2 = 0; indiceLivraisonRoute2 < initial.getRoutes().get(indexRoute2).getLivraisons().size(); indiceLivraisonRoute2++) {
                        siblingTour = new Tour(initial.getRoutes());
                        siblingTour.tryRelocateInter(indexRoute1, indiceLivraisonRoute1, indexRoute2, indiceLivraisonRoute2);
                        if (siblingTour.distance() < bestTour.distance()) {
                            return new Tour(siblingTour.getRoutes());
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
