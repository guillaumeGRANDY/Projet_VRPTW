package org.example;

import org.example.algorithm.tour.route.AlgoRoute;
import org.example.algorithm.tour.route.AlgoRouteRandom;
import org.example.model.Client;
import org.example.model.Depot;
import org.example.model.Tour;
import org.example.parser.LocationParser;

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