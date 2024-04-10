package org.polytech.model;

import java.util.ArrayList;
import java.util.List;

public class Tour {
    private List<Route> routes;

    public Tour() {
        this.routes = new ArrayList<>();
    }

    public Tour(List<Route> routes) {
        this.routes = new ArrayList<>();

        for (Route r : routes) {
            this.routes.add(new Route(r));
        }
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void add(Route route) {
        this.routes.add(route);
    }

    public double distance() {
        double totalDistance = 0;
        for (Route route : this.routes) {
            totalDistance += route.distance();
        }

        return totalDistance;
    }

    public void tryInterExchange(int ir1, int il1, int ir2, int il2) {
        Route r1 = this.routes.get(ir1);
        Livraison l1 = r1.getLivraisons().get(il1);
        Route r2 = this.routes.get(ir2);
        Livraison l2 = r2.getLivraisons().get(il2);

        if (r1.getTruck().hasEnoughCapacity(-l1.client().getDemand() + l2.client().getDemand())
                && r2.getTruck().hasEnoughCapacity(-l2.client().getDemand() + l1.client().getDemand())) {

            r1.getLivraisons().set(il1, l2);
            r2.getLivraisons().set(il2, l1);
        }
    }

    @Override
    public String toString() {
        String tour = "";

        for (Route route : routes) {
            tour += route.toString() + "\n";
        }

        return tour;
    }

    public void tryInterRelocate(int indexRoute1, int indiceLivraisonRoute1, int indexRoute2, int indiceLivraisonRoute2) {
        if (indexRoute1 < 0 || indexRoute1 >= this.routes.size() || indexRoute2 < 0 || indexRoute2 >= this.routes.size()) {
            throw new IllegalArgumentException("Index de route invalide");
        }

        Route r1 = this.routes.get(indexRoute1);
        Route r2 = this.routes.get(indexRoute2);

        if(indiceLivraisonRoute1 > r1.getLivraisons().size() -1) return;
        Livraison l1 = r1.getLivraisons().get(indiceLivraisonRoute1);

        if (r2.getTruck().hasEnoughCapacity(l1.client().getDemand())) {
            r2.getLivraisons().add(indiceLivraisonRoute2, l1);
            r1.getLivraisons().remove(l1);
        }
    }
}
