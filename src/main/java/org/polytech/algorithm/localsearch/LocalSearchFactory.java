package org.polytech.algorithm.localsearch;

import org.polytech.algorithm.LocalSearchType;
import org.polytech.model.Tour;

public class LocalSearchFactory {
    public static Tour makeLocalSearch(Tour initial, LocalSearchType localSearchType) throws HillClimbingException {
        HillClimbing localSearch;
        switch (localSearchType) {
            case INTRA_ROUTE_REVERSE_CLIENT -> {
                localSearch = new HillClimbingIntraRouteReverse(initial);
            }
            case INTRA_ROUTE_TWO_OPT -> {
                localSearch = new HillClimbingIntra2Opt(initial);
            }
            default -> {
                localSearch = new HillClimbingIntraRouteReverse(initial);
            }
        }
        return localSearch.localSearch();
    }
}
