package org.example.model;

public abstract class Location {

    private int x;
    private int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Distance euclidienne avec une autre location
     * @param location l'autre localisation
     * @return
     */
    public double distanceWith(Location location) {
        double distanceX = Math.abs(this.x - location.x) * Math.abs(this.x - location.x);
        double distanceY = Math.abs(this.x - location.x) * Math.abs(this.x - location.x);
        return Math.sqrt(distanceX + distanceY);
    }
}
