package org.polytech.algorithm.tour;

import org.polytech.algorithm.tour.comparator.DemandeCroissanteClientComparator;
import org.polytech.model.*;

import java.util.List;

public class AlgoTourneDemandeCroissante extends AlgoTourne {
    public AlgoTourneDemandeCroissante(ConstraintTruck constraintTruck, Depot depot, List<Client> clients) {
        super(constraintTruck, depot, clients);
        this.clientsToServe.sort(new DemandeCroissanteClientComparator());
    }

    @Override
    public Route affecterRoute() {
        Truck truck = new Truck(constraintTruck.maxCapacity());
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
