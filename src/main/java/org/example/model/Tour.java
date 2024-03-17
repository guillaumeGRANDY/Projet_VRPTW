package org.example.model;

import java.util.ArrayList;

public class Tour {
    private ArrayList<Route> routes = new ArrayList<>();

    public void add(Route route) {
        this.routes.add(route);
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
