package org.polytech;

import org.polytech.algorithm.tour.route.AlgoRoute;
import org.polytech.algorithm.tour.route.AlgoRouteRandom;
import org.polytech.model.Client;
import org.polytech.model.Depot;
import org.polytech.model.Tour;
import org.polytech.parser.LocationParser;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            LocationParser locationParser = new LocationParser("data101.vrp");
            ArrayList<Client> clients = locationParser.getClients();
            Depot depot=locationParser.getDepot();
            int maxQuantity=locationParser.getMaxQuantity();
            System.out.println(clients.size());

            AlgoRoute algoRoute = new AlgoRouteRandom(depot, clients);
            Tour tour=algoRoute.makeTour(maxQuantity);
            System.out.println(tour.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}