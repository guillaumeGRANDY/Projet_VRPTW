package org.polytech.simulation.simulatedannaealing.export;

import org.polytech.export.CvrpSolutionResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVWriter;
import org.polytech.model.Tour;
import org.polytech.simulation.simulatedannaealing.SimulatedAnnealingParameter;
import org.polytech.simulation.simulatedannaealing.TourGeneratedWithSimulatedAnnealing;

public class SimulatedAnnealingResultExportCsv {
    public static void export(String filePath, SimulatedAnnealingParameter simulatedAnnealingParameter, CvrpSolutionResult cvrpSolutionResult) throws IOException {
        File file = new File(filePath);
        CSVWriter csvWriter = new CSVWriter(new FileWriter(file, true));

        if (file.length() == 0) {
            csvWriter.writeNext(new String[] {
                "Temperature",
                "Cooling Rate",
                "Max Temperature Change",
                "Nb Iteration",
                "Total Solutions",
                "Fitness Average",
                "Ecart Type",
                "Total Truck",
                "Min Fitness",
                "Max Fitness"
            });
        }

        csvWriter.writeNext(new String[] {
            String.valueOf(simulatedAnnealingParameter.initialTemperature()),
            String.valueOf(simulatedAnnealingParameter.mu()),
            String.valueOf(simulatedAnnealingParameter.maxTemperatureChange()),
            String.valueOf(simulatedAnnealingParameter.movesAtTemperatureTk()),
            String.valueOf(cvrpSolutionResult.totalSolutions()),
            String.valueOf(cvrpSolutionResult.fitnessAverage()),
            String.valueOf(cvrpSolutionResult.ecartType()),
            String.valueOf(cvrpSolutionResult.totalTruck()),
            String.valueOf(cvrpSolutionResult.minFitness()),
            String.valueOf(cvrpSolutionResult.maxFitness())
        });
        csvWriter.close();
    }

    public static void export(String filePath, List<TourGeneratedWithSimulatedAnnealing> tourGeneratedWithSimulatedAnnealings) throws IOException {
        File file = new File(filePath);
        CSVWriter csvWriter = new CSVWriter(new FileWriter(file, true));

        if (file.length() == 0) {
            csvWriter.writeNext(new String[] {
                    "Temperature",
                    "Cooling Rate",
                    "Max Temperature Change",
                    "Nb Iteration",
                    "Fitness"
            });
        }

        for (TourGeneratedWithSimulatedAnnealing tourGeneratedWithSimulatedAnnealing : tourGeneratedWithSimulatedAnnealings) {
            csvWriter.writeNext(new String[] {
                    String.valueOf(tourGeneratedWithSimulatedAnnealing.simulatedAnnealingParameter().initialTemperature()),
                    String.valueOf(tourGeneratedWithSimulatedAnnealing.simulatedAnnealingParameter().mu()),
                    String.valueOf(tourGeneratedWithSimulatedAnnealing.simulatedAnnealingParameter().maxTemperatureChange()),
                    String.valueOf(tourGeneratedWithSimulatedAnnealing.simulatedAnnealingParameter().movesAtTemperatureTk()),
                    String.valueOf(tourGeneratedWithSimulatedAnnealing.tour().distance())
            });
        }
        csvWriter.close();
    }
}
