package org.polytech.model;

import java.util.UUID;

public class Truck {
    private String id;

    private int currentStock;

    public Truck(String id, int maxQuantity) {
        this.id = id;
        this.currentStock = maxQuantity;
    }

    public Truck(int maxQuantity) {
        this.id = UUID.randomUUID().toString();
        this.currentStock = maxQuantity;
    }

    public boolean useCapacity(int demand) {
        if(this.hasEnoughCapacity(demand)) {
            this.currentStock -= demand;
            return true;
        }
        return false;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public boolean hasEnoughCapacity() {
        return this.currentStock >= 0;
    }

    public boolean hasEnoughCapacity(int demand) {
        return this.currentStock - demand >= 0;
    }
}
