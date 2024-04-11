package org.polytech.model;

import java.util.UUID;

public class Truck {
    private String id;
    private int time = 0;

    private int placeRemaning;

    private int maxCapacity;

    public Truck(String id, int maxQuantity) {
        this.id = id;
        this.placeRemaning = maxQuantity;
        this.maxCapacity=maxQuantity;
    }

    public Truck(int maxQuantity) {
        this.id = UUID.randomUUID().toString();
        this.placeRemaning = maxQuantity;
    }

    public boolean useCapacity(int demand) {
        if(this.hasEnoughCapacity(demand)) {
            this.placeRemaning -= demand;
            return true;
        }
        return false;
    }

    public boolean addCapacity(int capacityAdded)
    {
        if(this.placeRemaning+capacityAdded<=this.maxCapacity) {
            this.placeRemaning += capacityAdded;
            return true;
        }
        return false;
    }

    public void useTime(int time) {
        this.time += time;
    }

    public int getTime() {
        return this.time;
    }

    public int getPlaceRemaning() {
        return placeRemaning;
    }

    public boolean hasEnoughCapacity() {
        return this.placeRemaning >= 0;
    }

    public boolean hasEnoughCapacity(int demand) {
        return this.placeRemaning - demand >= 0;
    }
}
