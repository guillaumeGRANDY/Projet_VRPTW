package org.polytech.model;

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
        double distanceX = Math.pow(Math.abs(this.x - location.x), 2);
        double distanceY = Math.pow(Math.abs(this.y - location.y), 2);
        return Math.sqrt(distanceX + distanceY);
    }

    public double timeWith(double distance) {
        return distance * 1.2;
    }
}
