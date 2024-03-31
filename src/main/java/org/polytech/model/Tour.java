package org.polytech.model;

import java.util.ArrayList;
import java.util.List;

public class Tour {
    private List<Route> routes;

    public Tour() {
        this.routes = new ArrayList<>();
    }

    public Tour(List<Route> routes) {
        this.routes = new ArrayList<>(routes);
    }

    public List<Route> getRoutes() {
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
