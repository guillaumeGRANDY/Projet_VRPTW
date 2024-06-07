package org.polytech;

import org.polytech.algorithm.globalsearch.SimulatedAnnealing;
import org.polytech.algorithm.tour.AlgoTourFactory;
import org.polytech.algorithm.tour.AlgorithmType;
import org.polytech.model.ConstraintTruck;
import org.polytech.model.Tour;
import org.polytech.parser.LocationParser;
import org.polytech.simulation.simulatedannaealing.SimulatedAnnealingSimulation;
import org.polytech.simulation.simulatedannaealing.TourGeneratedWithSimulatedAnnealing;
import org.polytech.simulation.simulatedannaealing.export.SimulatedAnnealingResultExportCsv;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class mainTestsSimulatedAnnealing {
    /**
     * Renvoie les clients, le dépôt et la config pour les camions
     * @return un objet LocationParser
     */
    public static LocationParser getLocationParser(String resourceDataFileName) {
        LocationParser locationParser = new LocationParser();
        try {
            locationParser.parseFile(resourceDataFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return locationParser;
    }

    public static void hyperParametersCombinations() {
        LocationParser locationParser = getLocationParser("sample.vrp");
        List<TourGeneratedWithSimulatedAnnealing> exploredTours = SimulatedAnnealingSimulation.explore(locationParser.getDepot(),
                locationParser.getClients());

        String fileName = "sa_result_test_tours_associated_to_fitness.csv";
        String filePath = Paths.get(System.getProperty("user.dir"), fileName).toString();
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        try {
            SimulatedAnnealingResultExportCsv.export(filePath, exploredTours);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void executionTime() {
        List<String> filesTest = List.of("sample.vrp", "data101.vrp");
        for (String fileTest : filesTest) {
            System.out.println("Test avec le fichier " + fileTest);
            System.out.println("----------------------------------");

            LocationParser locationParser = getLocationParser(fileTest);
            Tour initialTour = AlgoTourFactory.makeTour(new ConstraintTruck(locationParser.getMaxQuantity()), locationParser.getDepot(), locationParser.getClients(), AlgorithmType.RANDOM);

            int maxTemperatureChange = 50;
            List<Integer> n2Set = List.of(1000, 10_000, 50_000, 100_000);
            for (Integer n2 : n2Set) {
                System.out.println("Test avec n2 =" + n2);
                SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(maxTemperatureChange, n2, 0.95);

                long startTime = System.currentTimeMillis();
                TourGeneratedWithSimulatedAnnealing tourGeneratedWithSimulatedAnnealing = simulatedAnnealing.explore(initialTour, 3);
                System.out.println("Tour généré avec une distance de " + tourGeneratedWithSimulatedAnnealing.tour().distance());
                long endTime = System.currentTimeMillis();
                System.out.println("Execution time: " + (endTime - startTime) + " ms");
            }

            System.out.println("----------------------------------");
        }
    }

    public static void main(String[] args) {
        executionTime();
    }
}
