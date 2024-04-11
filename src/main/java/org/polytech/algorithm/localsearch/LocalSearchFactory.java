package org.polytech.algorithm.localsearch;

import org.polytech.algorithm.LocalSearchType;
import org.polytech.model.Tour;

public class LocalSearchFactory {
    public static Tour makeLocalSearch(Tour initial, LocalSearchType localSearchType) throws HillClimbingException {
        HillClimbing localSearch;
        switch (localSearchType) {
            case ECHANGE_INTRA -> {
                localSearch = new HillClimbingEchangeIntra(initial);
            }
            case ECHANGE_INTER -> {
                localSearch = new HillClimbingEchangeInter(initial);
            }
            case RELOCATE_INTRA -> {
                localSearch = new HillClimbingRelocateIntra(initial);
            }
            case RELOCATE_INTER -> {
                localSearch = new HillClimbingRelocateInter(initial);
            }
            case ECHANGE_GROUPE_INTER -> {
                localSearch = new HillClimbingGroupeEchangeInter(initial);
            }
            default -> {
                localSearch = new HillClimbingEchangeIntra(initial);
            }
        }
        return localSearch.localSearch();
    }
}
