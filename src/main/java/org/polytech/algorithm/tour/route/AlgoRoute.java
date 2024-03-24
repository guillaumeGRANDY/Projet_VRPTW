package org.polytech.algorithm.tour.route;

import org.polytech.model.*;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AlgoRoute {
    protected ArrayList<Client> clients;
    protected Depot depot;
    protected HashMap<Truck, Boolean> availablesTruck;

    public AlgoRoute(Depot depot, ArrayList<Client> clients) {
        this.depot = depot;
        this.clients = clients;
    }

    /**
     * Effectue une tournée à partir de la liste des villes courantes
     * @return la tournée à effectuer
     */
    public abstract Tour makeTour(int maxQuantity);
}
