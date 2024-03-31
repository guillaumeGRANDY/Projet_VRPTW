package org.polytech.algorithm.localsearch;

import org.polytech.model.Route;
import org.polytech.model.Tour;

import java.util.ArrayList;
import java.util.List;

public class HillClimbingIntraRouteReverse extends HillClimbing {
    public HillClimbingIntraRouteReverse(Tour initial) {
        super(initial);
    }

    @Override
    public Tour exploreSiblings(Tour initial) {
        Tour tour = new Tour(initial.getRoutes());

//        Route maximal = tour.getRoutes().getFirst();
//        List<Route> routesWithoutFirst = new ArrayList<>(tour.getRoutes());
//        routesWithoutFirst.removeFirst();
//        for (Route route : routesWithoutFirst) {
//            if(route.distance() > maximal.distance()) {
//                maximal = route;
//            }
//        }
//
//        Route best = exploreSiblings(maximal);
//        tour.getRoutes().set(tour.getRoutes().indexOf(maximal), best);

        for (int i = 0; i < tour.getRoutes().size(); i++) {
            Route best = exploreSiblings(tour.getRoutes().get(i));
            tour.getRoutes().set(i, best);
        }

        return tour;
    }

    public Route exploreSiblings(Route initial) {
        Route best = initial;
        Route sibling;
        for (int i = 0; i < best.getClients().size(); i++) {
            for (int j = i+1; j < best.getClients().size()-1; j++) {
                sibling = new Route(best);
                sibling.exchangeClientPosition(best.getClients().get(i), best.getClients().get(j));
                if(sibling.distance() < best.distance()) {
                    best = sibling;
                }
            }
        }
        return best;
    }
}
