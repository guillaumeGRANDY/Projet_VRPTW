package org.polytech.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
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
        this.livraisons = new ArrayList<>(route.getLivraisons());
        this.truck = route.truck;
    }

    public Depot getBegin() {
        return this.depot;
    }

    public List<Client> getClients() {
        return this.livraisons.stream().map(Livraison::client).collect(Collectors.toList());
    }

    public Truck getTruck()
    {
        return truck;
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
     * Ajoute un client à la tournée
     *
     * @param client
     */
    public void addLivraison(Livraison client) {
        this.livraisons.add(client);
    }

    public void deleteLivraison(Livraison livraison) {
        this.livraisons.remove(this.livraisons.indexOf(livraison));
    }

    /**
     * Transformation "échange entre 2 clients sur l'itinéraire"
     * O(n):
     *
     * @param i1 la livraison 1
     * @param i2 le livraison 2
     */
    public void tryExchangeClientPosition(int i1, int i2) {
        Livraison l1 = livraisons.get(i1);
        Livraison l2 = livraisons.get(i2);

        livraisons.set(i1, l2);
        livraisons.set(i2, l1);

        //livraisons.get(0).setHeureArrive(livraisons.get(0).client().getReadyTime());

        //test de la validité
//        for (int i = 1; i < livraisonsTest.size(); i++) {
//            livraisonsTest.get(i).setHeureArrive(livraisonsTest.get(i-1).heureArrive() + livraisonsTest.get(i-1).client().getService() + 1.2 * livraisonsTest.get(i-1).client().distanceWith(livraisonsTest.get(i).client()));
//            if(livraisonsTest.get(i).heureArrive() > livraisonsTest.get(i).client().getDueTime()) {
//                return;
//            }
//        }
    }

    public void tryIntraRelocate(int oldIndex, int newIndex) {
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
}
