package org.polytech.algorithm.tour;

import org.polytech.model.Client;
import org.polytech.model.ConstraintTruck;
import org.polytech.model.Depot;
import org.polytech.model.Tour;

import java.util.List;

public class AlgoTourFactory {
    public static Tour makeTour(ConstraintTruck constraintTruck, Depot depot, List<Client> clients, AlgorithmType algorithmType) {
        AlgoTourne algoTourne;
        switch (algorithmType) {
            case RANDOM -> {
                algoTourne = new AlgoTourneRandom(constraintTruck, depot, clients);
            }
            case CROISSANT_SORT -> {
                algoTourne = new AlgoTourneDemandeCroissante(constraintTruck, depot, clients);
            }
            default -> {
                algoTourne = new AlgoTourneRandom(constraintTruck, depot, clients);
            }
        }
        return algoTourne.makeTour();
    }
}
