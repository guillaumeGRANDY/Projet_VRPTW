package org.polytech.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Route {
    private Truck truck;
    private Depot depot;
    private List<Livraison> livraisons = new ArrayList<>();

    public Route(Depot depot, Truck truck) {
        this.depot = depot;
        this.truck = truck;
    }

    public Route(Route route) {
        this.depot = route.depot;
        this.livraisons = new ArrayList<>(route.livraisons);
        this.truck = new Truck(route.truck);
    }

    public Depot getBegin() {
        return this.depot;
    }

    public List<Client> getClients() {
        return this.livraisons.stream().map(Livraison::client).collect(Collectors.toList());
    }

    public Truck getTruck() {
        return truck;
    }

    public int getWeight() {
        return this.truck.getMaxCapacity() - this.truck.getPlaceRemaning();
    }

    public List<Livraison> getLivraisons() {
        return this.livraisons;
    }

    /**
     * Calcule la distance totale de la tournée
     * On démarre à un dépôt et on finit à ce même dépôt
     *
     * @return la distance entre le dépot, toutes les localisations de la tournée et le retour au dépôt
     */
    public double distance() {
        if (this.livraisons == null || this.livraisons.isEmpty()) {
            return 0;
        }

        double distance = 0;

        distance += this.depot.distanceWith(this.livraisons.getFirst().client()); //distance entre le dépot et le client 1

        for (int i = 0; i < this.livraisons.size() - 1; i++) {
            distance += this.livraisons.get(i).client().distanceWith(this.livraisons.get(i + 1).client()); //distance entre le client i et le cliet i+1
        }

        distance += this.livraisons.getLast().client().distanceWith(this.depot); //distance entre le dernier client et le depot

        return distance;
    }

    /**
     * Transformation "échange entre 2 clients sur l'itinéraire"
     * O(n):
     *
     * @param i1 la livraison 1
     * @param i2 le livraison 2
     */
    public void tryExchangeIntra(int i1, int i2) {
        Livraison l1 = livraisons.get(i1);
        Livraison l2 = livraisons.get(i2);

        livraisons.set(i1, l2);
        livraisons.set(i2, l1);
    }

    public void tryRelocateIntra(int oldIndex, int newIndex) {
        Livraison livraison = this.livraisons.get(oldIndex);
        this.livraisons.remove(oldIndex);
        this.livraisons.add(newIndex, livraison);
    }

    @Override
    public String toString() {
        StringBuilder route = new StringBuilder("Dépot -> ");

        for (Livraison livraison : livraisons) {
            route.append(livraison.client().getId()).append(" -> ");
        }

        route.append("Dépot");

        return route.toString();
    }

    public void reverseTroncon(int i, int j) {
        List<Livraison> tronconInverse = this.livraisons.subList(i, j + 1);
        Collections.reverse(tronconInverse);
    }

    public boolean couldAddNewLivraison(Livraison livraison) {
        return this.getTruck().hasEnoughCapacity(livraison.client().getDemand());
    }

    public boolean couldAddNewLivraison(int value) {
        return this.getTruck().hasEnoughCapacity(value);
    }

    public boolean couldAddNewLivraisons(List<Livraison> oldLivraisons, List<Livraison> livraisonsToAdd) {
        int oldTotal = 0;
        for (Integer demand : oldLivraisons.stream().map(Livraison::client).map(Client::getDemand).toList()) {
            oldTotal += demand;
        }

        int newTotal = 0;
        for (Integer demand : livraisonsToAdd.stream().map(Livraison::client).map(Client::getDemand).toList()) {
            newTotal += demand;
        }

        return this.truck.hasEnoughCapacity(-oldTotal + newTotal);
    }

    public boolean couldAddNewLivraisons(int beginIndex, List<Livraison> livraisons) {
        int total = 0;
        for (Livraison livraison : livraisons) {
            total += livraison.client().getDemand();
        }
        return this.truck.hasEnoughCapacity(total);
    }

    public boolean tryAddNewLivraison(Livraison livraison)
    {
        if(this.getTruck().useCapacity(livraison.client().getDemand())) {
            this.livraisons.add(livraison);
            return true;
        }
        return false;
    }

    public boolean tryAddNewLivraison(Livraison livraison, int index)
    {
        if(this.getTruck().useCapacity(livraison.client().getDemand())) {
            this.livraisons.add(index,livraison);
            return true;
        }
        return false;
    }

    public boolean tryAddNewLivraisons(int beginIndex, List<Livraison> livraisonsToAdd) {
        if(this.couldAddNewLivraisons(beginIndex, livraisonsToAdd)) {
            this.livraisons.addAll(beginIndex, livraisonsToAdd);
            for (Livraison livraison : livraisonsToAdd) {
                this.getTruck().useCapacity(livraison.client().getDemand());
            }
            return true;
        }
        return false;
    }

    public void removeLivraison(int indexLivraison) {
        this.livraisons.remove(indexLivraison);
    }

    public void removeLivraison(Livraison livraison) {
        this.livraisons.remove(livraison);
        this.truck.addCapacity(livraison.client().getDemand());
    }

    public void removeLivraison(List<Livraison> livraisons) {

        for (Livraison livraison : livraisons) {
            this.removeLivraison(livraison);
        }
    }
}
