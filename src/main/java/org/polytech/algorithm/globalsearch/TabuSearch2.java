package org.polytech.algorithm.globalsearch;

import javafx.util.Pair;
import org.polytech.algorithm.LocalSearchType;
import org.polytech.model.Tour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabuSearch2 {

    List<Pair<LocalSearchType,List<Integer>>> tabuList;
    int tabuSize;


    public TabuSearch2() {
        this.tabuList =new ArrayList<>();
    }

    public Tour explore(Tour initialSolution, int tabouListSize, int nbIteration)
    {
        //System.out.println("Exploration des solutions");
        this.tabuSize=tabouListSize;
        Tour currentSolution = new Tour(initialSolution);
        Tour bestSolution=new Tour(initialSolution);

        for(int i=0;i<nbIteration;i++)
        {
            currentSolution=selectBetterSibling(currentSolution);

            if(currentSolution.distance()<bestSolution.distance())
            {
                bestSolution=currentSolution;
            }
        }

        //System.out.println("Fin de l'optimisation");
        return bestSolution;
    }

    public void addTabou(Pair<LocalSearchType,List<Integer>> operateurBestTour)
    {
        if(tabuSize!=0) {
            if (this.tabuList.size() >= this.tabuSize) {
                this.tabuList.remove(0);
            }
            this.tabuList.add(operateurBestTour);
        }
    }


    public boolean isAllowed(LocalSearchType searchType,List<Integer> parameters)
    {
        for(Pair<LocalSearchType,List<Integer>> tabu:this.tabuList)
        {
            if(tabu.getKey()==searchType)
            {
                switch(tabu.getKey())
                {
                    case LocalSearchType.ECHANGE_INTRA ->
                    {
                        //si j'applique le même
                        if(tabu.getValue().get(0) == parameters.get(0) && tabu.getValue().get(1) == parameters.get(1) && tabu.getValue().get(2) == parameters.get(2))
                        {
                            return false;
                        }

                        //si j'applique l'inverse
                        if(tabu.getValue().get(0) == parameters.get(0) && tabu.getValue().get(1) == parameters.get(2) && tabu.getValue().get(2) == parameters.get(1))
                        {
                            return false;
                        }
                    }
                    case LocalSearchType.ECHANGE_INTER ->
                    {
                        //si j'applique le même
                        if(tabu.getValue().get(0) == parameters.get(0) && tabu.getValue().get(1) == parameters.get(1) && tabu.getValue().get(2) == parameters.get(2) && tabu.getValue().get(3) == parameters.get(3))
                        {
                            return false;
                        }

                        //si j'applique l'inverse
                        if(tabu.getValue().get(0) == parameters.get(1) && tabu.getValue().get(1) == parameters.get(0) && tabu.getValue().get(2) == parameters.get(3) && tabu.getValue().get(3) == parameters.get(2))
                        {
                            return false;
                        }
                    }
                    case LocalSearchType.RELOCATE_INTRA ->
                    {
                        //si j'applique le même
                        if(tabu.getValue().get(0) == parameters.get(0) && tabu.getValue().get(1) == parameters.get(1) && tabu.getValue().get(2) == parameters.get(2) )
                        {
                            return false;
                        }

                        //si j'applique l'inverse
                        if(tabu.getValue().get(0) == parameters.get(0) && tabu.getValue().get(1) == parameters.get(2) && tabu.getValue().get(2) == parameters.get(1))
                        {
                            return false;
                        }
                    }
                    case LocalSearchType.RELOCATE_INTER ->
                    {
                        //si j'applique le même
                        if(tabu.getValue().get(0) == parameters.get(0) && tabu.getValue().get(1) == parameters.get(1) && tabu.getValue().get(2) == parameters.get(2) && tabu.getValue().get(3) == parameters.get(3))
                        {
                            return false;
                        }

                        //si j'applique l'inverse
                        if(tabu.getValue().get(0) == parameters.get(1) && tabu.getValue().get(1) == parameters.get(0) && tabu.getValue().get(2) == parameters.get(3) && tabu.getValue().get(3) == parameters.get(2))
                        {
                            return false;
                        }
                    }
                    case LocalSearchType.ECHANGE_GROUPE_INTRA ->
                    {
                        if(parameters.get(3)>=parameters.get(1) && parameters.get(3)<=parameters.get(2))
                        {
                            return false;
                        }

                        //si j'applique le même
                        if(tabu.getValue().get(0) == parameters.get(0) && tabu.getValue().get(1) == parameters.get(1) && tabu.getValue().get(2) == parameters.get(2) && tabu.getValue().get(3) == parameters.get(3))
                        {
                            return false;
                        }

                        //si j'applique l'inverse
                        if(tabu.getValue().get(0) == parameters.get(1) && tabu.getValue().get(1) == parameters.get(3) && tabu.getValue().get(2) == parameters.get(3)+parameters.get(2)-parameters.get(1) && tabu.getValue().get(3) == parameters.get(1))
                        {
                            return false;
                        }
                    }
                    case LocalSearchType.ECHANGE_GROUPE_INTER ->
                    {
                        //si j'applique le même
                        if(tabu.getValue().get(0) == parameters.get(0) && tabu.getValue().get(1) == parameters.get(1) && tabu.getValue().get(2) == parameters.get(2)
                        && tabu.getValue().get(3) == parameters.get(3) && tabu.getValue().get(4) == parameters.get(4) && tabu.getValue().get(5) == parameters.get(5))
                        {
                            return false;
                        }

                        //si j'applique l'inverse
                        if(tabu.getValue().get(0) == parameters.get(3) && tabu.getValue().get(1) == parameters.get(4) && tabu.getValue().get(2) == parameters.get(5)
                        && tabu.getValue().get(3) == parameters.get(0) && tabu.getValue().get(4) == parameters.get(1) && tabu.getValue().get(5) == parameters.get(2))
                        {
                            return false;
                        }

                        if(tabu.getValue().get(0) == parameters.get(0) && tabu.getValue().get(1) == parameters.get(4) && tabu.getValue().get(2) == parameters.get(5)
                        && tabu.getValue().get(3) == parameters.get(3) && tabu.getValue().get(4) == parameters.get(1) && tabu.getValue().get(5) == parameters.get(2))
                        {
                            return false;
                        }
                    }
                    case LocalSearchType.RELOCATE_GROUPE_INTER ->
                    {
                        //si j'applique l'inverse
                        if(tabu.getValue().get(0) == parameters.get(3) && tabu.getValue().get(1) == parameters.get(4) && tabu.getValue().get(2) == parameters.get(4)+tabu.getValue().get(2)-tabu.getValue().get(1)
                        && tabu.getValue().get(3) == parameters.get(0) && tabu.getValue().get(4) == parameters.get(1))
                        {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }



    protected Tour selectBetterSibling(Tour initialTour) {
        Tour bestTour=new Tour(initialTour);

        Pair<LocalSearchType,List<Integer>> operateurBestTour=null;

        double initialDistance=initialTour.distance();
        double bestDistance=Double.POSITIVE_INFINITY;

        // System.out.println("Initial");
        // System.out.println(initialTour);

        //Echange intra
        // System.out.println("Echange intra");
        for(int indexRoute=0;indexRoute<initialTour.getRoutes().size();indexRoute++) {
            for (int indexLivraison1 = 0; indexLivraison1 < initialTour.getRoutes().get(indexRoute).getLivraisons().size()-1; indexLivraison1++) {
                for (int indexLivraison2 = indexLivraison1 + 1; indexLivraison2 < initialTour.getRoutes().get(indexRoute).getLivraisons().size(); indexLivraison2++) {

                    Tour siblingTour = new Tour(initialTour);

                    siblingTour.getRoutes().get(indexRoute).tryExchangeIntra(indexLivraison1, indexLivraison2);

                    if (siblingTour.distance() < bestDistance && this.isAllowed(LocalSearchType.ECHANGE_INTRA, Arrays.asList(indexRoute,indexLivraison1,indexLivraison2)) && this.isAllowed(LocalSearchType.ECHANGE_INTRA, Arrays.asList(indexRoute,indexLivraison2,indexLivraison1)))
                    {
                        bestTour=siblingTour;
                        bestDistance=siblingTour.distance();
                        operateurBestTour=new Pair<>(LocalSearchType.ECHANGE_INTRA, Arrays.asList(indexRoute,indexLivraison1,indexLivraison2));
                        // System.out.println("Changement avec un "+LocalSearchType.ECHANGE_INTRA+" => "+indexRoute+" , "+indexLivraison1+" , "+indexLivraison2+" => "+bestTour.distance());
                        // System.out.println(bestTour);
                    }
                }
            }
        }

        // System.out.println(bestTour);

        //Echange inter
        // System.out.println("Echange inter");
        for (int indexRoute1 = 0; indexRoute1 < initialTour.getRoutes().size(); indexRoute1++) {
            for (int indexRoute2 = indexRoute1 + 1; indexRoute2 < initialTour.getRoutes().size(); indexRoute2++) {
                for (int indexLivraisonRoute1 = 0; indexLivraisonRoute1 < initialTour.getRoutes().get(indexRoute1).getLivraisons().size(); indexLivraisonRoute1++) {
                    for (int indexLivraisonRoute2 = 0; indexLivraisonRoute2 < initialTour.getRoutes().get(indexRoute2).getLivraisons().size(); indexLivraisonRoute2++) {

                        Tour siblingTour = new Tour(initialTour);

                        if(siblingTour.tryExchangeInter(indexRoute1, indexLivraisonRoute1, indexRoute2, indexLivraisonRoute2))
                        {
                            if (siblingTour.distance() < bestDistance && this.isAllowed(LocalSearchType.ECHANGE_INTER, Arrays.asList(indexRoute1,indexRoute2,indexLivraisonRoute1,indexLivraisonRoute2)) && this.isAllowed(LocalSearchType.ECHANGE_INTER, Arrays.asList(indexRoute2,indexRoute1,indexLivraisonRoute2,indexLivraisonRoute1)))
                            {
                                bestTour = siblingTour;
                                bestDistance=siblingTour.distance();
                                operateurBestTour=new Pair<>(LocalSearchType.ECHANGE_INTER, Arrays.asList(indexRoute1,indexRoute2,indexLivraisonRoute1,indexLivraisonRoute2));
                                // System.out.println("Changement avec un "+LocalSearchType.ECHANGE_INTER+" => "+indexRoute1+" , "+indexRoute2+" , "+indexLivraisonRoute1+" , "+indexLivraisonRoute2+" => "+bestTour.distance());
                                // System.out.println(bestTour);
                            }
                        }
                    }
                }
            }
        }

        // System.out.println(bestTour);

        //Relocate Intra
        // System.out.println("Relocate intra");
        for(int indexRoute=0;indexRoute<initialTour.getRoutes().size();indexRoute++) {
            for (int indexLivraison1 = 0; indexLivraison1 < initialTour.getRoutes().get(indexRoute).getLivraisons().size(); indexLivraison1++) {
                for (int indexLivraison2 = indexLivraison1 + 1; indexLivraison2 < initialTour.getRoutes().get(indexRoute).getLivraisons().size(); indexLivraison2++) {

                    Tour siblingTour = new Tour(initialTour);

                    siblingTour.getRoutes().get(indexRoute).tryRelocateIntra(indexLivraison1,indexLivraison2);

                    if (siblingTour.distance() < bestDistance && this.isAllowed(LocalSearchType.RELOCATE_INTRA,Arrays.asList(indexRoute, indexLivraison1, indexLivraison2)) && this.isAllowed(LocalSearchType.RELOCATE_INTRA,Arrays.asList(indexRoute, indexLivraison2, indexLivraison1)))
                    {
                        bestTour = siblingTour;
                        bestDistance=siblingTour.distance();
                        operateurBestTour = new Pair<>(LocalSearchType.RELOCATE_INTRA, Arrays.asList(indexRoute, indexLivraison1, indexLivraison2));
                        // System.out.println("Changement avec un "+LocalSearchType.RELOCATE_INTRA+" => "+indexRoute+" , "+indexLivraison1+" , "+indexLivraison2+" => "+bestTour.distance());
                        // System.out.println(bestTour);
                    }
                }
            }
        }

        // System.out.println(bestTour);

        //Relocate Inter
        // System.out.println("Relocate inter");
        for (int indexRoute1 = 0; indexRoute1 < initialTour.getRoutes().size()-1; indexRoute1++) {
            for (int indexRoute2 = indexRoute1 + 1; indexRoute2 < initialTour.getRoutes().size(); indexRoute2++) {
                for (int indexLivraisonRoute1 = 0; indexLivraisonRoute1 < initialTour.getRoutes().get(indexRoute1).getLivraisons().size(); indexLivraisonRoute1++) {
                    for (int indexLivraisonRoute2 = 0; indexLivraisonRoute2 < initialTour.getRoutes().get(indexRoute2).getLivraisons().size(); indexLivraisonRoute2++) {

                        Tour siblingTour = new Tour(initialTour);

                        if(siblingTour.tryRelocateInter(indexRoute1, indexLivraisonRoute1, indexRoute2, indexLivraisonRoute2))
                        {
                            if (siblingTour.distance() < bestDistance && this.isAllowed(LocalSearchType.RELOCATE_INTER, Arrays.asList(indexRoute1, indexRoute2, indexLivraisonRoute1, indexLivraisonRoute2)))
                            {
                                bestTour = siblingTour;
                                bestDistance=siblingTour.distance();
                                operateurBestTour = new Pair<>(LocalSearchType.RELOCATE_INTER, Arrays.asList(indexRoute1, indexRoute2, indexLivraisonRoute1, indexLivraisonRoute2));
                                // System.out.println("Changement avec un " + LocalSearchType.RELOCATE_INTER + " => " + indexRoute1 + " , " + indexRoute2 + " , " + indexLivraisonRoute1 + " , " + indexLivraisonRoute2 + " => " + bestTour.distance());
                                // System.out.println(bestTour);
                            }
                        }
                    }
                }
            }
        }

        // System.out.println(bestTour);

        //merge
        // System.out.println("Merge");
        for (int indexRoute1 = 0; indexRoute1 < initialTour.getRoutes().size()-1; indexRoute1++) {
            for (int indexRoute2 = indexRoute1+1; indexRoute2 < initialTour.getRoutes().size(); indexRoute2++) {
                for (int index = 0; index < initialTour.getRoutes().get(indexRoute2).getLivraisons().size(); index++) {

                    Tour siblingTour = new Tour(initialTour);

                    if(siblingTour.tryMerge(indexRoute1,indexRoute2,index)) {
                        if (siblingTour.distance() < bestDistance)
                        {
                            bestTour = siblingTour;
                            bestDistance=siblingTour.distance();
                            //perateurBestTour=new Pair<>(LocalSearchType.MERGE, Arrays.asList(indexRoute1,indexRoute2,index));
                            // System.out.println("Changement avec un " + LocalSearchType.MERGE + " => " + indexRoute1 + " , " + indexRoute2 + " , " + index + " => " + bestTour.distance());
                            // System.out.println(bestTour);
                        }
                    }
                }
            }
        }

//        // System.out.println(bestTour);
//
//        //Echange groupe intra
//        // System.out.println("Echange goupre intra");
        for (int indexRoute = 0; indexRoute < initialTour.getRoutes().size(); indexRoute++) {
            for (int indexLivraisonOld1 = 0; indexLivraisonOld1 < initialTour.getRoutes().get(indexRoute).getLivraisons().size()-1; indexLivraisonOld1++) {
                for (int indexLivraisonOld2 = indexLivraisonOld1+1; indexLivraisonOld2 < initialTour.getRoutes().get(indexRoute).getLivraisons().size(); indexLivraisonOld2++) {
                    for (int indexLivraisonNew = indexLivraisonOld1+1; indexLivraisonNew < initialTour.getRoutes().get(indexRoute).getLivraisons().size(); indexLivraisonNew++) {

                        Tour siblingTour = new Tour(initialTour);

                        siblingTour.getRoutes().get(indexRoute).tryIntraGroupeExchange(indexLivraisonOld1, indexLivraisonOld2, indexLivraisonNew);

                        if (siblingTour.distance() < bestDistance && this.isAllowed(LocalSearchType.ECHANGE_GROUPE_INTRA,Arrays.asList(indexRoute,indexLivraisonOld1,indexLivraisonOld2,indexLivraisonNew)))
                        {
                            bestTour = siblingTour;
                            bestDistance=siblingTour.distance();
                            operateurBestTour=new Pair<>(LocalSearchType.ECHANGE_GROUPE_INTRA, Arrays.asList(indexRoute,indexLivraisonOld1,indexLivraisonOld2,indexLivraisonNew));
                            // System.out.println("Changement avec un " + LocalSearchType.ECHANGE_GROUPE_INTRA + " => " + indexRoute + " , " + indexLivraisonOld1 + " , " + indexLivraisonOld2 + " , " + indexLivraisonNew + " => " + bestTour.distance());
                            // System.out.println(bestTour);
                        }
                    }
                }
            }
        }
//
//        // System.out.println(bestTour);
//
//        //Echange groupe inter
//        // System.out.println("Echange groupe inter");
        for (int indexRoute1 = 0; indexRoute1 < initialTour.getRoutes().size(); indexRoute1++) {
            for (int indexRoute2 = indexRoute1 + 1; indexRoute2 < initialTour.getRoutes().size(); indexRoute2++) {
                for (int indexLivraison1Route1 = 0; indexLivraison1Route1 < initialTour.getRoutes().get(indexRoute1).getLivraisons().size()-1; indexLivraison1Route1++) {
                    for (int indexLivraison2Route1 = indexLivraison1Route1+1; indexLivraison2Route1 < initialTour.getRoutes().get(indexRoute1).getLivraisons().size(); indexLivraison2Route1++) {
                        for (int indexLivraison1Route2 = 0; indexLivraison1Route2 < initialTour.getRoutes().get(indexRoute2).getLivraisons().size()-1; indexLivraison1Route2++) {
                            for (int indexLivraison2Route2 = indexLivraison1Route2+1; indexLivraison2Route2 < initialTour.getRoutes().get(indexRoute2).getLivraisons().size(); indexLivraison2Route2++) {
                                if(indexLivraison1Route1 !=0 || indexLivraison2Route1!=initialTour.getRoutes().get(indexRoute1).getLivraisons().size()-1 || indexLivraison1Route2!=0 || indexLivraison2Route2!= initialTour.getRoutes().get(indexRoute2).getLivraisons().size()-1) {

                                    Tour siblingTour = new Tour(initialTour);

                                    if (siblingTour.tryInterGroupeExchange(indexRoute1, indexLivraison1Route1, indexLivraison2Route1, indexRoute2, indexLivraison1Route2, indexLivraison2Route2)) {
                                        if (siblingTour.distance() < bestDistance && this.isAllowed(LocalSearchType.ECHANGE_GROUPE_INTER, Arrays.asList(indexRoute1, indexLivraison1Route1, indexLivraison2Route1, indexRoute2, indexLivraison1Route2, indexLivraison2Route2))) {
                                            bestTour = siblingTour;
                                            bestDistance = siblingTour.distance();
                                            operateurBestTour = new Pair<>(LocalSearchType.ECHANGE_GROUPE_INTER, Arrays.asList(indexRoute1, indexLivraison1Route1, indexLivraison2Route1, indexRoute2, indexLivraison1Route2, indexLivraison2Route2));
                                            // System.out.println("Changement avec un " + LocalSearchType.ECHANGE_GROUPE_INTER + " => " + indexRoute1 + " , " + indexRoute2 + " , " + indexLivraison1Route1 + " , " + indexLivraison2Route1 +
                                            //        " , " + indexLivraison1Route2 + " , " + indexLivraison2Route2 + " => " + bestTour.distance());
                                            // System.out.println(bestTour);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
//
//        // System.out.println(bestTour);
//
//        //Relocate groupe inter
//        // System.out.println("Relocate groupe inter");
        for (int indexRoute1 = 0; indexRoute1 < initialTour.getRoutes().size(); indexRoute1++) {
            for (int indexRoute2 = indexRoute1 + 1; indexRoute2 < initialTour.getRoutes().size(); indexRoute2++) {
                for (int indexLivraison1 = 0; indexLivraison1 < initialTour.getRoutes().get(indexRoute1).getLivraisons().size()-1; indexLivraison1++) {
                    for (int indexLivraison2 = indexLivraison1+1; indexLivraison2 < initialTour.getRoutes().get(indexRoute1).getLivraisons().size(); indexLivraison2++) {
                        for (int indexLivraisonDestination = 0; indexLivraisonDestination < initialTour.getRoutes().get(indexRoute2).getLivraisons().size(); indexLivraisonDestination++) {

                            Tour siblingTour = new Tour(initialTour);

                            if(siblingTour.tryRelocateGroupeInter(indexRoute1,indexLivraison1,indexLivraison2,indexRoute2,indexLivraisonDestination)) {
                                if (siblingTour.distance() < bestDistance && this.isAllowed(LocalSearchType.RELOCATE_GROUPE_INTER,Arrays.asList(indexRoute1,indexLivraison1,indexLivraison2,indexRoute2,indexLivraisonDestination)))
                                {
                                    bestTour = siblingTour;
                                    bestDistance=siblingTour.distance();
                                    operateurBestTour = new Pair<>(LocalSearchType.RELOCATE_GROUPE_INTER, Arrays.asList(indexRoute1,indexLivraison1,indexLivraison2,indexRoute2,indexLivraisonDestination));
                                    // System.out.println("Changement avec un " + LocalSearchType.RELOCATE_GROUPE_INTER + " => " + indexRoute1 + " , " + indexRoute2 + " , " + indexLivraison1 + " , " + indexLivraison2 +
//                                        " , " + indexLivraisonDestination + " => " + bestTour.distance());
//                                  // System.out.println(bestTour);
                                }
                            }
                        }
                    }
                }
            }
        }

        // System.out.println(bestTour);

        if(bestDistance>=initialDistance)
        {
            //System.out.println("Aucune meilleur solution n'a été trouvée, remonté et ajout de l'opérateur à la liste de tabou => " +initialDistance+" -> "+bestDistance);
            //System.out.println("");

            //System.out.println("Liste des tabous");
            for(int i=0;i<tabuList.size();i++)
            {
                //System.out.print((i+1)+" ");
                //System.out.println(tabuList.get(i));
            }
            //System.out.println("");

            //System.out.println("par la méthode");
            //System.out.printf(operateurBestTour.toString());

            //System.out.println("");
            //System.out.println("");
            addTabou(operateurBestTour);
        }
        else {
            //System.out.println(initialDistance+" -> "+bestDistance);
        }

        // System.out.println("Retour: "+bestTour.toString());
        return bestTour;
    }
}
