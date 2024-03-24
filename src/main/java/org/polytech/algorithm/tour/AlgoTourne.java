package org.polytech.algorithm.tour;

import org.polytech.model.*;

import java.util.*;

public abstract class AlgoTourne {
    protected Stack<Client> clientsToServe = new Stack<>();
    protected Depot depot;
    protected ConstraintTruck constraintTruck;

    public AlgoTourne(ConstraintTruck constraintTruck, Depot depot, List<Client> clients) {
        this.depot = depot;
        this.clientsToServe.addAll(clients);
        this.constraintTruck = constraintTruck;
    }

    protected abstract Route affecterRoute();

    /**
     * Effectue une tournée à partir de la liste des villes courantes
     * @return la tournée à effectuer
     */
    public abstract Tour makeTour();
}
