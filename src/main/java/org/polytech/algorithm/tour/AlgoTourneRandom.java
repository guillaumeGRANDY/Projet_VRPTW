package org.polytech.algorithm.tour;

import org.polytech.algorithm.tour.comparator.HeureDebutClientComparator;
import org.polytech.model.*;

import java.util.*;

public class AlgoTourneRandom extends AlgoTourne {
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

        Location currentPostion = depot;
        while (this.clientsToServe.isEmpty() == false && indiceClient < this.clientsToServe.size()) {

            if (this.clientsToServe.get(indiceClient).getDueTime() >= time + timeByDistance * currentPostion.distanceWith(this.clientsToServe.get(indiceClient))) {
                boolean capacityIsUsed = truck.useCapacity(clientsToServe.get(indiceClient).getDemand());
                if (capacityIsUsed) {
                    currentPostion = this.clientsToServe.get(indiceClient);

                    if (time < this.clientsToServe.get(indiceClient).getReadyTime() + timeByDistance * currentPostion.distanceWith(this.clientsToServe.get(indiceClient))) {
                        //si le client n'est pas encore prêt à être livré, il s'y rend et attend
                        time = this.clientsToServe.get(indiceClient).getReadyTime() + this.clientsToServe.get(indiceClient).getService();
                        route.addLivraison(new Livraison(this.clientsToServe.get(indiceClient), time - this.clientsToServe.get(indiceClient).getService()));
                    }
                    else {
                        time += this.clientsToServe.get(indiceClient).getService() + timeByDistance * currentPostion.distanceWith(this.clientsToServe.get(indiceClient));
                        // le temps auquel on arrive
                        route.addLivraison(new Livraison(this.clientsToServe.get(indiceClient), time - this.clientsToServe.get(indiceClient).getService()));
                    }
                    this.clientsToServe.remove(indiceClient);
                } else {
                    indiceClient++;
                }
            } else {
                indiceClient++;
            }
        }

        return route;
    }
}
