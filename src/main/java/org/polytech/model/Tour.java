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

    public Tour(Tour tour)
    {
        this.routes = new ArrayList<>();

        for (Route r : tour.routes) {
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

    public void tryExchangeInter(int ir1, int il1, int ir2, int il2) {
        Route r1 = this.routes.get(ir1);
        Livraison l1 = r1.getLivraisons().get(il1);

        Route r2 = this.routes.get(ir2);
        Livraison l2 = r2.getLivraisons().get(il2);

        if (r1.couldAddNewLivraison(-l1.client().getDemand() + l2.client().getDemand()) && r2.couldAddNewLivraison(l1.client().getDemand() - l2.client().getDemand()))
        {
            r1.removeLivraison(l1);
            r2.removeLivraison(l2);

            r1.tryAddNewLivraison(l2, il1);
            r2.tryAddNewLivraison(l1, il2);
        }
    }

    public void tryRelocateInter(int indexRoute1, int indiceLivraisonRoute1, int indexRoute2, int indiceLivraisonRoute2) {
        if (indexRoute1 < 0 || indexRoute1 >= this.routes.size() || indexRoute2 < 0 || indexRoute2 >= this.routes.size()) {
            throw new IllegalArgumentException("Index de route invalide");
        }

        Route r1 = this.routes.get(indexRoute1);
        Route r2 = this.routes.get(indexRoute2);
        Livraison l = r1.getLivraisons().get(indiceLivraisonRoute1);
        

        if(r2.tryAddNewLivraison(l,indiceLivraisonRoute2)) {
            r1.removeLivraison(indiceLivraisonRoute1);
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

        List<Livraison> subListLivraisonRoute2;
        subListLivraisonRoute2 = new ArrayList<>(r2.getLivraisons().subList(indiceLivraisonRoute21, indiceLivraisonRoute22+1));

        if(r1.couldAddNewLivraisons(subListLivraisonsRoute1, subListLivraisonRoute2) &&
                r2.couldAddNewLivraisons(subListLivraisonRoute2, subListLivraisonsRoute1)
        ) {
            r1.tryAddNewLivraisons(indiceLivraisonRoute11, subListLivraisonRoute2);
            r2.tryAddNewLivraisons(indiceLivraisonRoute21, subListLivraisonsRoute1);

            r1.removeLivraison(subListLivraisonsRoute1);
            r2.removeLivraison(subListLivraisonRoute2);
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

    public int getTotalWeight() {
        int total=0;
        for(Route route: this.routes) {
            total+=route.getWeight();
        }
        return total;
    }
}
