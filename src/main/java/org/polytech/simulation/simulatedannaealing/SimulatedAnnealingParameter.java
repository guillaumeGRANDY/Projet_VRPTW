package org.polytech.simulation.simulatedannaealing;

public record SimulatedAnnealingParameter(
        int maxTemperatureChange,
        int movesAtTemperatureTk,
        double mu,
        double initialTemperature
) {
}
