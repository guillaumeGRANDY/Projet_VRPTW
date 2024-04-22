package org.polytech.algorithm.tour;

import org.polytech.algorithm.tour.comparator.HeureDebutClientComparator;
import org.polytech.model.*;

import java.util.*;

public class AlgoTourneRandom extends AlgoTourne {
    private Random random = new Random();

    public AlgoTourneRandom(ConstraintTruck constraintTruck, Depot depot, List<Client> clients) {
        super(constraintTruck, depot, clients);
        this.clientsToServe.sort(new HeureDebutClientComparator());
    }

    public Tour makeTour() {
        Tour tour = new Tour();

        while (!this.clientsToServe.isEmpty()) {
            Route route = this.affecterRoute();
            tour.add(route);
        }
        return tour;
    }

    @Override
    protected Route affecterRoute() {
        Truck truck = new Truck(UUID.randomUUID().toString(), constraintTruck.maxCapacity());
        Route route = new Route(this.depot, truck);
        double time = 0;
        int indiceClient = 0;
        int timeByDistance=0;
        int visitedClients = 0;

        Location currentPostion = depot;
        while (this.clientsToServe.isEmpty() == false && route.getTruck().getPlaceRemaning() > 0 && visitedClients < this.clientsToServe.size()){
            indiceClient = random.nextInt(this.clientsToServe.size());
            boolean capacityIsUsed = route.tryAddNewLivraison(new Livraison(this.clientsToServe.get(indiceClient), this.clientsToServe.get(indiceClient).getReadyTime() + timeByDistance * currentPostion.distanceWith(this.clientsToServe.get(indiceClient))));
            if (capacityIsUsed) {
                currentPostion = this.clientsToServe.remove(indiceClient);
            }
            visitedClients++;
        }
        return route;
    }
}
