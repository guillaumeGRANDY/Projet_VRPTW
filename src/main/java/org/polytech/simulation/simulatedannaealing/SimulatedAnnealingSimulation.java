package org.polytech.simulation.simulatedannaealing;

import org.polytech.algorithm.globalsearch.SimulatedAnnealing;
import org.polytech.algorithm.tour.AlgoTourFactory;
import org.polytech.algorithm.tour.AlgorithmType;
import org.polytech.export.CvrpSolutionResult;
import org.polytech.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
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


        BigDecimal fromMu = new BigDecimal("0.1");
        BigDecimal toMu = new BigDecimal(1);
        BigDecimal stepMu = new BigDecimal("0.05");

        int fromN1 = 10;
        int toN1 = 100;
        int stepForN1 = 5;

        for (double initialTemperature = 0.5; initialTemperature <= 10; initialTemperature += 0.5) {
            for (int currentN1 = fromN1; currentN1 <= toN1; currentN1 += stepForN1) {
                for (BigDecimal currentMu = fromMu; currentMu.compareTo(toMu) <= 0; currentMu = currentMu.add(stepMu)) {
                    simulatedAnnealing.setMu(currentMu.doubleValue());

                    tourGeneratedWithSimulatedAnnealings.add(simulatedAnnealing.explore(initial, initialTemperature));
                    initial = AlgoTourFactory.makeTour(new ConstraintTruck(200), // on part d'une nouvelle tournée Random pour chaque couple de paramètre
                            depot,
                            clients,
                            AlgorithmType.RANDOM);
                }
            }
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
