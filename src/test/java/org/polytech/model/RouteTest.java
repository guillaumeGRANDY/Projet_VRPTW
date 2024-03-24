package org.polytech.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RouteTest {
    @Test
    @DisplayName("On teste que l'on calcule bien la distance en partant et en revenant au dépôt")
    void testWeCalculateRouteDistanceProperly() {
        Route route = new Route(new Depot("d1",0, 0, 0, 230), new Truck(200));
        route.addClient(new Client("c1", 2, 0, 10, 20, 10, 10));
        route.addClient(new Client("c2", 3, 0, 10, 20, 10, 10));
        route.addClient(new Client("c2",6, 0, 10, 20, 10, 10));

        double totalDistance = route.distance();
        assertEquals(2+1+3+6, totalDistance);
    }
}