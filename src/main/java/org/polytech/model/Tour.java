package org.polytech.model;

import java.util.ArrayList;

public class Tour {
    private ArrayList<Route> routes = new ArrayList<>();

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void add(Route route) {
        this.routes.add(route);
    }

    public double totalDistance() {
        double totalDistance = 0;
        for (Route route : this.routes) {
            totalDistance += route.distance();
        }

        return totalDistance;
    }

    @Override
    public String toString() {
        String tour="";

        for (Route route : routes) {
            tour += route.toString()+"\n";
        }

        return tour;
    }
}
