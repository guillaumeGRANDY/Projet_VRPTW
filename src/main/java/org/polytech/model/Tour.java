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

    public boolean tryExchangeInter(int ir1, int il1, int ir2, int il2) {
        Route r1 = this.routes.get(ir1);
        Livraison l1 = r1.getLivraisons().get(il1);

        Route r2 = this.routes.get(ir2);
        Livraison l2 = r2.getLivraisons().get(il2);

        if (r1.couldAddNewLivraison(-l1.client().getDemand() + l2.client().getDemand()) && r2.couldAddNewLivraison(l1.client().getDemand() - l2.client().getDemand()))
        {
            r1.removeLivraison(il1);
            r2.removeLivraison(il2);

            r1.tryAddNewLivraison(l2, il1);
            r2.tryAddNewLivraison(l1, il2);
        }
        else
        {
            return false;
        }
        return true;
    }

    public boolean tryRelocateInter(int indexRoute1, int indiceLivraisonRoute1, int indexRoute2, int indiceLivraisonRoute2) {
        if (indexRoute1 < 0 || indexRoute1 >= this.routes.size() || indexRoute2 < 0 || indexRoute2 >= this.routes.size()) {
            throw new IllegalArgumentException("Index de route invalide");
        }

        Route r1 = this.routes.get(indexRoute1);
        Route r2 = this.routes.get(indexRoute2);
        Livraison l = r1.getLivraisons().get(indiceLivraisonRoute1);
        

        if(r2.tryAddNewLivraison(l,indiceLivraisonRoute2)) {
            r1.removeLivraison(indiceLivraisonRoute1);
        }
        else
        {
            return false;
        }

        if(r1.getLivraisons().isEmpty()) {
            this.routes.remove(indexRoute1);
        }

        return true;
    }

    public boolean tryInterGroupeExchange(int indexRoute1, int indiceLivraisonRoute11, int indiceLivraisonRoute12, int indexRoute2, int indiceLivraisonRoute21, int indiceLivraisonRoute22) {
        if (indexRoute1 < 0 || indexRoute1 >= this.routes.size() || indexRoute2 < 0 || indexRoute2 >= this.routes.size()) {
            throw new IllegalArgumentException("Index de route invalide");
        }

        Route r1 = this.routes.get(indexRoute1);
        Route r2 = this.routes.get(indexRoute2);

        if (indiceLivraisonRoute11 < 0 || indiceLivraisonRoute12 >= r1.getLivraisons().size() || indiceLivraisonRoute21 < 0 || indiceLivraisonRoute22 >= r2.getLivraisons().size()) {
            throw new IllegalArgumentException("Index de livraison invalide");
        }

        List<Livraison> subListLivraisonsRoute1 = new ArrayList<>(r1.getLivraisons().subList(indiceLivraisonRoute11, indiceLivraisonRoute12+1)); //le +1 ne pose-t-il pas pb
        List<Livraison> subListLivraisonsRoute2 = new ArrayList<>(r2.getLivraisons().subList(indiceLivraisonRoute21, indiceLivraisonRoute22+1));

        int poidListLivraisons1=0;
        int poidListLivraisons2=0;

        for(Livraison l:subListLivraisonsRoute1)
        {
            poidListLivraisons1+=l.client().getDemand();
        }

        for(Livraison l:subListLivraisonsRoute2)
        {
            poidListLivraisons2+=l.client().getDemand();
        }

        if(r1.getTruck().hasEnoughCapacity(-poidListLivraisons1+poidListLivraisons2) &&
            r2.getTruck().hasEnoughCapacity(poidListLivraisons1-poidListLivraisons2))
        {

            r1.removeLivraison(subListLivraisonsRoute1);
            r2.removeLivraison(subListLivraisonsRoute2);

            r1.tryAddNewLivraisons(indiceLivraisonRoute11, subListLivraisonsRoute2);
            r2.tryAddNewLivraisons(indiceLivraisonRoute21, subListLivraisonsRoute1);
        }
        else {
            return false;
        }

        return true;
    }

    public boolean tryRelocateGroupeInter(int indexRoute1, int indiceLivraisonRoute11, int indiceLivraisonRoute12, int indexRoute2, int indiceLivraisonRoute2) {
        if (indexRoute1 < 0 || indexRoute1 >= this.routes.size() || indexRoute2 < 0 || indexRoute2 >= this.routes.size()) {
            throw new IllegalArgumentException("Index de route invalide");
        }

        Route r1 = this.routes.get(indexRoute1);
        Route r2 = this.routes.get(indexRoute2);

        List<Livraison> subListLivraisons = new ArrayList<>(r1.getLivraisons().subList(indiceLivraisonRoute11, indiceLivraisonRoute12+1));

        int poidListLivraisons=0;

        for(Livraison l:subListLivraisons)
        {
            poidListLivraisons+=l.client().getDemand();
        }

        if(r2.getTruck().hasEnoughCapacity(poidListLivraisons))
        {
            if(r2.tryAddNewLivraisons(indiceLivraisonRoute2,subListLivraisons)) {
                r1.removeLivraison(subListLivraisons);
            }
        }
        else {
            return false;
        }

        if(r1.getLivraisons().isEmpty()) {
            this.routes.remove(indexRoute1);
        }

        return true;
    }

    public boolean tryMerge(int indexRoute1, int indexRoute2, int indice) {
        if (indexRoute1 < 0 || indexRoute1 >= this.routes.size() || indexRoute2 < 0 || indexRoute2 >= this.routes.size()) {
            throw new IllegalArgumentException("Index de route invalide");
        }

        Route r1 = this.routes.get(indexRoute1);
        Route r2 = this.routes.get(indexRoute2);

        int poidListLivraisonsRoute1=0;

        for(Livraison l:r1.getLivraisons())
        {
            poidListLivraisonsRoute1+=l.client().getDemand();
        }


        if(r2.getTruck().hasEnoughCapacity(poidListLivraisonsRoute1))
        {
            if(r2.tryAddNewLivraisons(indice,r1.getLivraisons())) {
                routes.remove(r1);
            }
        }
        else{
            return false;
        }

        if(r1.getLivraisons().isEmpty()) {
            this.routes.remove(indexRoute1);
        }

        return true;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(getClass()!= obj.getClass())
        {
            return false;
        }

        Tour otherTour=(Tour) obj;

        if(otherTour.routes.size()!=routes.size()) {
            return false;
        }
        for(int i=0;i<routes.size();i++)
        {
            if(!routes.get(i).equals(otherTour.routes.get(i))){
                return false;
            }
        }

        return true;
    }
}
