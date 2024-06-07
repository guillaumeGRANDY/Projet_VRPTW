package org.polytech.export;

import org.polytech.model.Tour;

import java.util.List;

public record CvrpSolutionResult(
        double fitnessAverage,
        double ecartType,
        double totalTruck,
        double minFitness,
        double maxFitness,
        List<Tour> tours
) {
    public int totalSolutions() {
        return tours.size();
    }
}
