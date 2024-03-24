package org.polytech.algorithm.tour;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.polytech.model.ConstraintTruck;
import org.polytech.model.Tour;
import org.polytech.parser.LocationParser;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AlgoTourneRandomTest {

    @Test
    @DisplayName("Je peux faire une tournée random")
    void makeTour() throws IOException {
        LocationParser locationParser = new LocationParser();
        locationParser.parseFile("data101.vrp");

        AlgoTourneRandom algoTourneAleatoire =
                new AlgoTourneRandom(new ConstraintTruck(locationParser.getMaxQuantity()), locationParser.getDepot(), locationParser.getClients());

        Tour tour = algoTourneAleatoire.makeTour();
        double totalDistance = tour.totalDistance();
        assertNotNull(tour);
        assertTrue(totalDistance > 0);
    }
}