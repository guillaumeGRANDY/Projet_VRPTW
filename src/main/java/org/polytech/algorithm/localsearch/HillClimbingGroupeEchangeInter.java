package org.polytech.algorithm.localsearch;

import org.polytech.model.Route;
import org.polytech.model.Tour;

public class HillClimbingGroupeEchangeInter extends HillClimbing {

    public HillClimbingGroupeEchangeInter(Tour initial) {
        super(initial);
    }

    @Override
    public Tour exploreSiblings(Tour initial) {
        Tour bestTour = new Tour(initial.getRoutes());
        Tour siblingTour;
        for (int indexRoute1 = 0; indexRoute1 < initial.getRoutes().size(); indexRoute1++) {
            for (int indexRoute2 = indexRoute1 + 1; indexRoute2 < initial.getRoutes().size(); indexRoute2++) {
                for (int indiceLivraisonRoute11 = 0; indiceLivraisonRoute11 < initial.getRoutes().get(indexRoute1).getLivraisons().size(); indiceLivraisonRoute11++) {
                    for (int indiceLivraisonRoute12 = indiceLivraisonRoute11+1; indiceLivraisonRoute12 < initial.getRoutes().get(indexRoute1).getLivraisons().size(); indiceLivraisonRoute12++) {
                        for (int indiceLivraisonRoute21 = 0; indiceLivraisonRoute21 < initial.getRoutes().get(indexRoute2).getLivraisons().size(); indiceLivraisonRoute21++) {
                            for (int indiceLivraisonRoute22 = indiceLivraisonRoute21+1; indiceLivraisonRoute22 < initial.getRoutes().get(indexRoute2).getLivraisons().size(); indiceLivraisonRoute22++) {
                                siblingTour = new Tour(initial.getRoutes());
                                siblingTour.tryInterGroupeExchange(indexRoute1, indiceLivraisonRoute11, indiceLivraisonRoute12, indexRoute2, indiceLivraisonRoute21, indiceLivraisonRoute22);
                                if (siblingTour.distance() < bestTour.distance()) {
                                    bestTour = siblingTour;
                                }
                            }
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
