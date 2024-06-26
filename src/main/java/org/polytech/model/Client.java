package org.polytech.model;

import java.util.Objects;

public class Client extends Location{

    /**
     * Identifiant de la livraison
     */
    private String id;

    /**
     * L'heure quand un point de livraison est prêt à être livré
     */
    private int readyTime;

    /**
     * L'heure à laquelle doit finir la livraison
     */
    private int dueTime;
    /**
     * Nombre de paquets à distribuer
     */
    private int demand;
    /**
     * Temps qu'il faut pour livrer
     */
    private int service;


    public Client(String id,int x, int y, int readyTime, int dueTime, int demand, int service) {
        super(x, y);
        this.id = id;
        this.readyTime = readyTime;
        this.dueTime = dueTime;
        this.demand = demand;
        this.service = service;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(int readyTime) {
        this.readyTime = readyTime;
    }

    public int getDueTime() {
        return dueTime;
    }

    public void setDueTime(int dueTime) {
        this.dueTime = dueTime;
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "Client " + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
