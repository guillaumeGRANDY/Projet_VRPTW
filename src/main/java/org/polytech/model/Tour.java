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
            r1.getTruck().useCapacity(-l1.client().getDemand() + l2.client().getDemand());

            r2.getLivraisons().set(il2, l1);
            r2.getTruck().useCapacity(-l2.client().getDemand() + l1.client().getDemand());
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
            r2.getTruck().useCapacity(l1.client().getDemand());

            r1.getLivraisons().remove(indiceLivraisonRoute1);
            r1.getTruck().addCapacity(l1.client().getDemand());
        }
        if(r1.getLivraisons().isEmpty()) {
            this.routes.remove(indexRoute1);
        }
    }

    public void tryInterGroupeExchange(int indexRoute1, int indiceLivraisonRoute11, int indiceLivraisonRoute12, int indexRoute2, int indiceLivraisonRoute21, int indiceLivraisonRoute22) {
        if (indexRoute1 < 0 || indexRoute1 >= this.routes.size() || indexRoute2 < 0 || indexRoute2 >= this.routes.size()) {
            throw new IllegalArgumentException("Index de route invalide");
        }

        Route r1 = this.routes.get(indexRoute1);
        Route r2 = this.routes.get(indexRoute2);

        List<Livraison> subListLivraisonsRoute1;
        subListLivraisonsRoute1 = new ArrayList<>(r1.getLivraisons().subList(indiceLivraisonRoute11, indiceLivraisonRoute12+1));
        int poidRoute1=0;
        for(Livraison l : subListLivraisonsRoute1) {
            poidRoute1 += l.client().getDemand();
        }

        List<Livraison> subListLivraisonRoute2;
        subListLivraisonRoute2 = new ArrayList<>(r2.getLivraisons().subList(indiceLivraisonRoute21, indiceLivraisonRoute22+1));
        int poidsRoute2 = 0;
        for(Livraison l : subListLivraisonRoute2) {
            poidsRoute2 += l.client().getDemand();
        }

        if(r1.getTruck().hasEnoughCapacity(-poidRoute1 + poidsRoute2) && r2.getTruck().hasEnoughCapacity(-poidsRoute2 + poidRoute1)) {
            // on enleve dans r1
            r1.getLivraisons().removeAll(subListLivraisonsRoute1);
            r2.getLivraisons().removeAll(subListLivraisonRoute2);

            r1.getLivraisons().addAll(indiceLivraisonRoute11, subListLivraisonRoute2);
            r2.getLivraisons().addAll(indiceLivraisonRoute21, subListLivraisonsRoute1);

            r1.getTruck().useCapacity(-poidRoute1 + poidsRoute2);
            r2.getTruck().useCapacity(-poidsRoute2 + poidRoute1);
        }
    }
}
