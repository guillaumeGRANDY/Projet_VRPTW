package org.polytech;

import org.polytech.parser.LocationParser;
import org.polytech.simulation.simulatedannaealing.SimulatedAnnealingSimulation;
import org.polytech.simulation.simulatedannaealing.TourGeneratedWithSimulatedAnnealing;
import org.polytech.simulation.simulatedannaealing.export.SimulatedAnnealingResultExportCsv;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class mainTestsSimulatedAnnealing {
    public static void main(String[] args) {
        LocationParser locationParser = new LocationParser();
        try {
            locationParser.parseFile("sample.vrp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
}
