package org.polytech.algorithm.tour;

import org.polytech.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class AlgoTourneRandom extends AlgoTourne {
    public AlgoTourneRandom(ConstraintTruck constraintTruck, Depot depot, List<Client> clients) {
        super(constraintTruck, depot, clients);
        Collections.shuffle(this.clientsToServe);
    }

    @Override
    protected Route affecterRoute() {
        Truck truck = new Truck(UUID.randomUUID().toString(), constraintTruck.maxCapacity());
        Route route = new Route(this.depot, truck);
        boolean canAffectNewClient = truck.hasEnoughCapacity(clientsToServe.peek().getDemand());

        while (this.clientsToServe.isEmpty() == false && canAffectNewClient) {
            Client client = this.clientsToServe.peek();
            boolean isAffectedToTheRoute = truck.useCapacity(client.getDemand());
            if (isAffectedToTheRoute) {
                this.clientsToServe.pop();
                route.addClient(client);
            } else {
                canAffectNewClient = false;
            }
        }

        return route;
    }

    @Override
    public Tour makeTour() {
        Tour tour = new Tour();

        while (!this.clientsToServe.isEmpty()) {
            Route route = this.affecterRoute();
            tour.add(route);
        }

        return tour;
    }
}
