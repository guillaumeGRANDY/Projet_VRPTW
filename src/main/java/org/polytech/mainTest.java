package org.polytech;

import org.polytech.algorithm.globalsearch.SimulatedAnnealing;
import org.polytech.algorithm.globalsearch.TabuSearch2;
import org.polytech.algorithm.tour.AlgoTourFactory;
import org.polytech.model.ConstraintTruck;
import org.polytech.model.Tour;
import org.polytech.parser.LocationParser;

import java.io.IOException;
import java.util.*;

import static org.polytech.algorithm.tour.AlgorithmType.RANDOM;

public class mainTest {
    public static void main(String[] args)
    {
        LocationParser locationParser = new LocationParser();
        try {
            locationParser.parseFile("sample.vrp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ConstraintTruck constraintTruck=new ConstraintTruck(locationParser.getMaxQuantity());

        Tour tour;

        HashMap<Integer,Integer> result=new HashMap<>();

        int nbIteration=100;

        TabuSearch2 tabuSearch = new TabuSearch2();

        long startTime = System.nanoTime();

        for(int i=0;i<nbIteration;i++)
        {
            tour= AlgoTourFactory.makeTour(constraintTruck,
                    locationParser.getDepot(),
                    locationParser.getClients(),
                    RANDOM);

            tour = tabuSearch.explore(tour, 20, 10000);

            if(result.containsKey((int)tour.distance()))
            {
                result.replace((int)tour.distance(),result.get((int)tour.distance())+1);
            }
            else {
                result.put((int)tour.distance(),1);
            }
            System.out.println("Etape "+(i+1)+"/"+nbIteration+": résultat => "+tour.distance());
        }
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        ArrayList<Integer> values= new ArrayList<>(result.keySet());
        Collections.sort(values);



        System.out.println("Temps d'execution secondes: " + timeElapsed / 1000000000);
        System.out.println(result);
        for(Integer i:values)
        {
            System.out.println(i+"\t"+result.get(i));
        }
        System.out.println();
    }
}
