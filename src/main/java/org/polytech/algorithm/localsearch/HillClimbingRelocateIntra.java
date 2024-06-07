package org.polytech.algorithm.localsearch;

import org.polytech.model.Route;
import org.polytech.model.Tour;

public class HillClimbingRelocateIntra extends HillClimbing {
    public HillClimbingRelocateIntra(Tour initial) {
        super(initial);
    }

    @Override
    public Tour exploreSiblings(Tour initial) {
        Tour best = new Tour(initial.getRoutes());

        for(int i=0; i < initial.getRoutes().size(); i++) {
            Route route = exploreSiblings(initial.getRoutes().get(i));
            best.getRoutes().set(i, route);
        }
        return best;
    }

    @Override
    protected Route exploreSiblings(Route route) {
        Route bestRoute = new Route(route);
        Route siblingRoute;
        for (int indiceLivraisonFrom = 0; indiceLivraisonFrom < route.getLivraisons().size(); indiceLivraisonFrom++) {
            for (int indiceLivraisonTo = 0; indiceLivraisonTo < route.getLivraisons().size(); indiceLivraisonTo++) {
                siblingRoute = new Route(route);
                siblingRoute.tryRelocateIntra(indiceLivraisonFrom, indiceLivraisonTo);
                if (siblingRoute.distance() < bestRoute.distance()) {
                    bestRoute = siblingRoute;
                }
            }
        }
        return bestRoute;
    }
}
