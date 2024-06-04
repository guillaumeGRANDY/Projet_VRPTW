package org.polytech;

import org.polytech.algorithm.globalsearch.TabuSearch2;
import org.polytech.algorithm.tour.AlgoTourFactory;
import org.polytech.model.*;
import org.polytech.parser.LocationParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.polytech.algorithm.tour.AlgorithmType.RANDOM;

public class mainTestsTabou {

    public static void main(String[] args) {

        LocationParser locationParser = new LocationParser();
        List<Client> clients = new ArrayList<>();
        Depot begin;
        ConstraintTruck constraintTruck;
        Tour tour = null;
        List<Route> routes = new ArrayList<>();

        TabuSearch2 tabuSearch = new TabuSearch2();
        
        try {
            locationParser.parseFile("sample.vrp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        clients=locationParser.getClients();
        begin=locationParser.getDepot();
        constraintTruck=new ConstraintTruck(locationParser.getMaxQuantity());

        tour = AlgoTourFactory.makeTour(constraintTruck,
                begin,
                clients,
                RANDOM);
        routes = tour.getRoutes();

        System.out.println("tabouSize\tnbIter\tresultat");

        int nbIterTable[]= {10,50,100,500,1000,5000,10000,50000};
        int nbTabou[]={1,5,10,20,50,100};

        for(int i=0;i<nbTabou.length;i++)
        {
            for(int j = 0; j< nbIterTable.length; j++)
            {
                System.out.println(nbTabou[i]+"\t" + nbIterTable[j] + "\t" + (int)tabuSearch.explore(tour, nbTabou[i], nbIterTable[j]).distance());
            }
        }
    }
}
