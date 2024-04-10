package org.polytech.algorithm.localsearch;

import org.polytech.model.Route;
import org.polytech.model.Tour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HillClimbingEchangeIntra extends HillClimbing {
    public HillClimbingEchangeIntra(Tour initial) {
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

    /**
     * On explore le trajet en échangeant 2 clients distincts jusqu'à trouver un échange gagnant
     *
     * @param route la route courante
     * @return
     */
    @Override
    protected Route exploreSiblings(Route route) {
        Route bestRoute = new Route(route);
        Route siblingRoute;
        for (int i = 0; i < route.getLivraisons().size(); i++) {
            for (int j = i + 1; j < route.getLivraisons().size(); j++) {
                siblingRoute = new Route(route);
                siblingRoute.tryExchangeClientPosition(i, j);
                if (siblingRoute.distance() < bestRoute.distance()) {
                    bestRoute = siblingRoute;
                }
            }
        }
        return bestRoute;
    }
}
