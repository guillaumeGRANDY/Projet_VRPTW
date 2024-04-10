package org.polytech.algorithm.localsearch;

import org.polytech.algorithm.LocalSearchType;
import org.polytech.model.Tour;

public class LocalSearchFactory {
    public static Tour makeLocalSearch(Tour initial, LocalSearchType localSearchType) throws HillClimbingException {
        HillClimbing localSearch;
        switch (localSearchType) {
            case INTRA_ROUTE -> {
                localSearch = new HillClimbingEchangeIntra(initial);
            }
            case INTRA_ROUTE_TWO_OPT -> {
                localSearch = new HillClimbingIntra2Opt(initial);
            }
            case INTRA_ROUTE_RELOCATE -> {
                localSearch = new HillClimbingRelocateIntra(initial);
            }
            case INTER_ROUTE_EXCHANGE -> {
                localSearch = new HillClimbingEchangeInter(initial);
            }
            case INTER_RELOCATE -> {
                localSearch = new HillClimbingRelocateInter(initial);
            }
            default -> {
                localSearch = new HillClimbingEchangeIntra(initial);
            }
        }
        return localSearch.localSearch();
    }
}
