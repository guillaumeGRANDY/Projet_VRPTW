package org.polytech.model;

/**
 * idName x y readyTime dueTime
 */
public class Depot extends Location {
    private String id;

    /**
     * Date d'ouverture d'un dépôt
     */
    private int readyTime;
    /**
     * Date de fermeture d'un dépôt
     */
    private int dueTime;


    public Depot(String id, int x, int y, int readyTime, int dueTime) {
        super(x,y);
        this.id = id;
        this.readyTime = readyTime;
        this.dueTime = dueTime;
    }

    @Override
    public String toString() {
        return "Depot{" +
                "id='" + id + '\'' +
                ", x=" + super.getX() +
                ", y=" + super.getY() +
                ", readyTime=" + readyTime +
                ", dueTime=" + dueTime +
                '}';
    }
}
