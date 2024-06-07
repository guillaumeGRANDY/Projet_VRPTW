package org.polytech.algorithm.localsearch;

import org.polytech.model.Route;
import org.polytech.model.Tour;

public class HillClimbingIntra2Opt extends HillClimbing {
    public HillClimbingIntra2Opt(Tour initial) {
        super(initial);
    }

    @Override
    public Tour exploreSiblings(Tour initial) {
        Tour tour = new Tour(initial.getRoutes());

        for (int i = 0; i < tour.getRoutes().size(); i++) {
            Route best = exploreSiblings(tour.getRoutes().get(i));
            tour.getRoutes().set(i, best);
        }

        return tour;
    }

    public Route exploreSiblings(Route initial) {
        Route best = new Route(initial);
        boolean trouve = true;
        while (trouve) {
            trouve = false;
            for (int i = 0; i < best.getClients().size(); i++) {
                for (int j = i+1; j < best.getClients().size()-1; j++) {
                    Route sibling = new Route(best);
                    sibling.reverseTroncon(i, j);
                    if(sibling.distance() < best.distance()) {
                        best = sibling;
                        trouve = true;
                    }
                }
            }
        }
        return best;
    }
}
