package org.polytech.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TourTest {
    @Test
    void testInterRelocate() {
        Truck truck = new Truck("1", 200);
        Depot depot = new Depot("1", 10, 10, 0, 200);

        Route route = new Route(depot, truck);
        Client client1 = new Client("1", 1, 2, 0, 200, 10, 10);
        route.tryAddNewLivraison(new Livraison(client1, 0));
    }
}