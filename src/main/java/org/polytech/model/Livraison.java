package org.polytech.model;

public class Livraison {
    private Client client;
    private double heureArrive;

    public Livraison(Client client, double heureArrive) {
        this.client = client;
        this.heureArrive = heureArrive;
    }

    public Client client() {
        return client;
    }

    public double heureArrive() {
        return heureArrive;
    }

    public void setHeureArrive(double heureArrive) {
        this.heureArrive = heureArrive;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(getClass()!= obj.getClass())
        {
            return false;
        }

        return ((Livraison) obj).client.equals(client);
    }
}
