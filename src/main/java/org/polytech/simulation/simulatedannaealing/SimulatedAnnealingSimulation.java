package org.polytech.simulation.simulatedannaealing;

import org.polytech.algorithm.globalsearch.SimulatedAnnealing;
import org.polytech.algorithm.tour.AlgoTourFactory;
import org.polytech.algorithm.tour.AlgorithmType;
import org.polytech.export.CvrpSolutionResult;
import org.polytech.model.*;

import java.util.ArrayList;
import java.util.List;

public class SimulatedAnnealingSimulation {
    private static SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing();

    public static CvrpSolutionResult explore(Depot depot, List<Client> clients, SimulatedAnnealingParameter simulatedAnnealingParameter, int repeatedTimes) {
        simulatedAnnealing = new SimulatedAnnealing(simulatedAnnealingParameter.maxTemperatureChange(),
                simulatedAnnealingParameter.movesAtTemperatureTk(),
                simulatedAnnealingParameter.mu());

        Tour current;
        List<Tour> tours = new ArrayList<>();

        Tour initial = AlgoTourFactory.makeTour(new ConstraintTruck(200),
                depot,
                clients,
                AlgorithmType.RANDOM);

        for (int i = 1; i <= repeatedTimes; i++) {
            current = simulatedAnnealing.explore(initial, simulatedAnnealingParameter.initialTemperature()).tour();
            tours.add(current);
            initial = AlgoTourFactory.makeTour(new ConstraintTruck(200),
                    depot,
                    clients,
                    AlgorithmType.RANDOM);
        }

        return cvrpSolutionResult(tours);
    }

    public static List<TourGeneratedWithSimulatedAnnealing> explore(Depot depot, List<Client> clients) {
        List<TourGeneratedWithSimulatedAnnealing> tourGeneratedWithSimulatedAnnealings = new ArrayList<>();

        // On fait varier la température courante de 0.5 à 10 avec des pas de 0.5
        Tour initial = AlgoTourFactory.makeTour(new ConstraintTruck(200),
                depot,
                clients,
                AlgorithmType.RANDOM);

        int maxTemperatureChange = 100;
        int movesAtTemperatureTk = 10000;
        double mu = 0.9;

        simulatedAnnealing.setMaxTemperatureChange(maxTemperatureChange);
        simulatedAnnealing.setMovesAtTemperatureTk(movesAtTemperatureTk);
        simulatedAnnealing.setMu(mu);

        double initialTemperature = 0.5;
        while (initialTemperature <= 10) {
            tourGeneratedWithSimulatedAnnealings.addAll(exploreWithDynamicMu(initial, initialTemperature, 0.1, 1, 0.05));
            initialTemperature += 0.5;
            initial = AlgoTourFactory.makeTour(new ConstraintTruck(200),
                    depot,
                    clients,
                    AlgorithmType.RANDOM);
        }

        return tourGeneratedWithSimulatedAnnealings;
    }

    private static List<TourGeneratedWithSimulatedAnnealing> exploreWithDynamicMu(Tour initial, double initialTemperature, double from, double to, double step) {
        List<TourGeneratedWithSimulatedAnnealing> tourGeneratedWithSimulatedAnnealings = new ArrayList<>();
        double currentMu = from;
        while (currentMu <= to) {
            simulatedAnnealing.setMu(currentMu);
            tourGeneratedWithSimulatedAnnealings.add(simulatedAnnealing.explore(initial, initialTemperature));
            currentMu += step;
        }

        return tourGeneratedWithSimulatedAnnealings;
    }

    private static CvrpSolutionResult cvrpSolutionResult(List<Tour> tours) {
        double average = tours.stream().map(Tour::distance).mapToDouble(Double::doubleValue).sum() / tours.size();
        double sumEcartMoyenne = 0;
        for (Double fitness : tours.stream().map(Tour::distance).toList()) {
            sumEcartMoyenne += Math.pow(average - fitness, 2);
        }
        double variance = ((double) 1 / tours.size()) * sumEcartMoyenne;
        double ecartType = Math.sqrt(variance);

        List<List<Route>> routes = tours.stream().map(Tour::getRoutes).toList();
        int sumTruck = 0;
        for (List<Route> route : routes) {
            sumTruck += route.size();
        }

        int averageTotalTruckPerTour = sumTruck / routes.size();

        return new CvrpSolutionResult(average, ecartType, averageTotalTruckPerTour, tours.stream().map(Tour::distance).min(Double::compareTo).get(), tours.stream().map(Tour::distance).max(Double::compareTo).get(), tours);
    }
}
