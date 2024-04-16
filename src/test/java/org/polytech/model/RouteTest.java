package org.polytech.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RouteTest {
    @Test
    @DisplayName("On teste que l'on calcule bien la distance en partant et en revenant au dépôt")
    void testWeCalculateRouteDistanceProperly() {
        Route route = new Route(new Depot("d1",0, 0, 0, 230), new Truck(200));
        route.addLivraison(new Livraison(new Client("c1", 2, 0, 10, 20, 10, 10), 10));
        route.addLivraison(new Livraison(new Client("c2", 3, 0, 10, 20, 10, 10), 25));
        route.addLivraison(new Livraison(new Client("c3",6, 0, 10, 20, 10, 10), 40));

        double totalDistance = route.distance();
        assertEquals(2+1+3+6, totalDistance);
    }

    @Test
    @DisplayName("On peut échanger 2 clients si ils existent dans la liste")
    void testWeCanExchangeTwoClients() {
        Route route = new Route(new Depot("d1",0, 0, 0, 230), new Truck(200));
        Client c1 = new Client("c1", 2, 0, 5, 19, 10, 2);
        Livraison l1 = new Livraison(c1, 10);
        route.addLivraison(l1);
        Client c2 = new Client("c2", 3, 0, 24, 31, 10, 2);
        Livraison l2 = new Livraison(c2, 25);
        route.addLivraison(l2);
        Client c3 = new Client("c3", 6, 0, 34, 38, 10, 2);
        Livraison l3 = new Livraison(c3, 40);
        route.addLivraison(l3);

        route.tryExchangeIntra(0, 2);

        assertEquals(c3, route.getClients().getFirst());
        assertEquals(c1, route.getClients().getLast());
    }
}