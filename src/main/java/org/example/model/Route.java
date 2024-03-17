package org.example.model;

import java.util.ArrayList;

public class Route {
    private Truck truck;
    private Depot depot;
    private ArrayList<Client> clients = new ArrayList<>();

    public Route(Depot depot) {
        this.depot = depot;
    }

    /**
     * Calcule la distance totale de la tournée
     * On démarre à un dépôt et on finit à ce même dépôt
     *
     * @return la distance entre le dépot, toutes les localisations de la tournée et le retour au dépôt
     */
    public double distance() {
        if (clients == null || clients.isEmpty()) {
            return 0;
        }

        double distance = 0;

        distance += this.depot.distanceWith(clients.get(0)); //distance entre le dépot et le client 1

        for (int i = 0; i < clients.size() - 1; i++) {
            distance += this.clients.get(i).distanceWith(clients.get(i + 1)); //distance entre le client i et le cliet i+1
        }

        distance += this.clients.getLast().distanceWith(this.depot); //distance entre le dernier client et le depot

        return distance;
    }

    /**
     * Ajoute un client à la tournée
     *
     * @param client
     */
    public void addClient(Client client) {
        this.clients.add(client);
    }

    public void deleteClient(Client client)
    {
        this.clients.remove(this.clients.indexOf(client));
    }

    public void exchangeClientPosition(Client c1, Client c2)
    {
        Client tmp=this.clients.get(this.clients.indexOf(c1));
        c1=c2;
        c2=tmp;
    }

    @Override
    public String toString() {
        StringBuilder route= new StringBuilder("Dépot -> ");

        for(Client client: clients)
        {
            route.append(client.getId()).append(" -> ");
        }

        route.append("Dépot");

        return route.toString();
    }
}
