package org.polytech.algorithm.tour.route;

import org.polytech.model.*;

import java.util.ArrayList;

public class AlgoRouteRandom extends AlgoRoute {
    public AlgoRouteRandom(Depot depot, ArrayList<Client> clients) {
        super(depot, clients);
    }

    public Tour makeTour(int maxQuantity) {
        Tour tour = new Tour();
        int indiceClients = 0;

        while (indiceClients < this.clients.size()) {
            Route route = new Route(this.depot);
            int quantityRest = maxQuantity;

            while (indiceClients < this.clients.size() && quantityRest > clients.get(indiceClients).getDemand()) {
                quantityRest -= clients.get(indiceClients).getDemand();
                route.addClient(clients.get(indiceClients));
                indiceClients++;
            }
            tour.add(route);
        }
        return tour;
    }
}
