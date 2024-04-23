package org.polytech.algorithm.globalsearch;

import javafx.util.Pair;
import org.polytech.algorithm.LocalSearchType;
import org.polytech.algorithm.localsearch.HillClimbing;
import org.polytech.model.Route;
import org.polytech.model.Tour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabuSearch {

    List<Pair<LocalSearchType,List<Integer>>> tabuList;
    int tabuSize;


    public TabuSearch() {
        this.tabuList =new ArrayList<>();
    }

    public Tour explore(Tour initialSolution, int tabouListSize, int nbIteration)
    {
        System.out.println("Exploration des solutions");
        this.tabuSize=tabouListSize;
        Tour minTour = new Tour(initialSolution);

        for(int i=0;i<nbIteration;i++)
        {
            minTour=selectBetterSibling(minTour);
        }

        System.out.println("Fin de l'optimisation");
        return minTour;
    }

    public void addTabou(Pair<LocalSearchType,List<Integer>> operateurBestTour)
    {
        if(this.tabuList.size()>=this.tabuSize) {
            this.tabuList.remove(0);
        }
        this.tabuList.add(operateurBestTour);
    }

    public boolean isAllowed(LocalSearchType searchType,List<Integer> parameters)
    {
        return !this.tabuList.contains(new Pair<>(searchType,parameters));
    }

    protected Tour selectBetterSibling(Tour initialTour) {
        Tour newTour = new Tour(initialTour);
        Tour bestTour=null;

        Pair<LocalSearchType,List<Integer>> operateurBestTour=null;

        double initialDistance=initialTour.distance();
        double bestDistance=Double.POSITIVE_INFINITY;

        //Echange intra
        for(int indexRoute=0;indexRoute<initialTour.getRoutes().size();indexRoute++) {
            for (int indexLivraison1 = 0; indexLivraison1 < initialTour.getRoutes().get(indexRoute).getLivraisons().size(); indexLivraison1++) {
                for (int indexLivraison2 = indexLivraison1 + 1; indexLivraison2 < initialTour.getRoutes().get(indexRoute).getLivraisons().size(); indexLivraison2++) {
                    
                    Route siblingRoute = new Route(initialTour.getRoutes().get(indexRoute));
                    double distanceBefore= siblingRoute.distance();

                    siblingRoute.tryExchangeIntra(indexLivraison1, indexLivraison2);

                    if (initialDistance-distanceBefore+siblingRoute.distance() < bestDistance &&
                    this.isAllowed(LocalSearchType.ECHANGE_INTRA, Arrays.asList(indexRoute,indexLivraison1,indexLivraison2))) {
                        bestTour = new Tour(initialTour);
                        bestTour.getRoutes().set(indexRoute,siblingRoute);
                        bestDistance=initialDistance-distanceBefore+siblingRoute.distance();
                        operateurBestTour=new Pair<>(LocalSearchType.ECHANGE_INTRA, Arrays.asList(indexRoute,indexLivraison1,indexLivraison2));
                    }
                }
            }
        }

        //Echange inter
        for (int indexRoute1 = 0; indexRoute1 < initialTour.getRoutes().size(); indexRoute1++) {
            for (int indexRoute2 = indexRoute1 + 1; indexRoute2 < initialTour.getRoutes().size(); indexRoute2++) {
                for (int indiceLivraisonRoute1 = 0; indiceLivraisonRoute1 < initialTour.getRoutes().get(indexRoute1).getLivraisons().size(); indiceLivraisonRoute1++) {
                    for (int indiceLivraisonRoute2 = 0; indiceLivraisonRoute2 < initialTour.getRoutes().get(indexRoute2).getLivraisons().size(); indiceLivraisonRoute2++) {

                        Tour siblingTour = new Tour(initialTour);

                        siblingTour.tryExchangeInter(indexRoute1, indiceLivraisonRoute1, indexRoute2, indiceLivraisonRoute2);

                        if (siblingTour.distance() < bestDistance &&
                        this.isAllowed(LocalSearchType.ECHANGE_INTER, Arrays.asList(indexRoute1,indexRoute2,indiceLivraisonRoute1,indiceLivraisonRoute2))) {
                            bestTour = siblingTour;
                            bestDistance=siblingTour.distance();
                            operateurBestTour=new Pair<>(LocalSearchType.ECHANGE_INTER, Arrays.asList(indexRoute1,indexRoute2,indiceLivraisonRoute1,indiceLivraisonRoute2));
                        }
                    }
                }
            }
        }

        //Relocate Intra
        for(int indexRoute=0;indexRoute<initialTour.getRoutes().size();indexRoute++) {
            for (int indexLivraison1 = 0; indexLivraison1 < initialTour.getRoutes().get(indexRoute).getLivraisons().size(); indexLivraison1++) {
                for (int indexLivraison2 = indexLivraison1 + 1; indexLivraison2 < initialTour.getRoutes().get(indexRoute).getLivraisons().size(); indexLivraison2++) {

                    Route siblingRoute = new Route(initialTour.getRoutes().get(indexRoute));
                    double distanceBefore= siblingRoute.distance();

                    siblingRoute.tryRelocateIntra(indexLivraison1,indexLivraison2);

                    if (initialDistance-distanceBefore+siblingRoute.distance() < bestDistance &&
                    this.isAllowed(LocalSearchType.RELOCATE_INTRA, Arrays.asList(indexRoute,indexLivraison1,indexLivraison2))) {
                        bestTour = new Tour(initialTour);
                        bestTour.getRoutes().set(indexRoute, siblingRoute);
                        bestDistance = initialDistance - distanceBefore + siblingRoute.distance();
                        operateurBestTour = new Pair<>(LocalSearchType.RELOCATE_INTRA, Arrays.asList(indexRoute, indexLivraison1, indexLivraison2));
                    }
                }
            }
        }

        //Relocate Inter
        for (int indexRoute1 = 0; indexRoute1 < initialTour.getRoutes().size()-1; indexRoute1++) {
            for (int indexRoute2 = indexRoute1 + 1; indexRoute2 < initialTour.getRoutes().size(); indexRoute2++) {
                for (int indiceLivraisonRoute1 = 0; indiceLivraisonRoute1 < initialTour.getRoutes().get(indexRoute1).getLivraisons().size(); indiceLivraisonRoute1++) {
                    for (int indiceLivraisonRoute2 = 0; indiceLivraisonRoute2 < initialTour.getRoutes().get(indexRoute2).getLivraisons().size(); indiceLivraisonRoute2++) {

                        Tour siblingTour = new Tour(initialTour);

                        siblingTour.tryRelocateInter(indexRoute1, indiceLivraisonRoute1, indexRoute2, indiceLivraisonRoute2);

                        if (siblingTour.distance() < bestDistance &&
                                this.isAllowed(LocalSearchType.RELOCATE_INTER, Arrays.asList(indexRoute1, indexRoute2, indiceLivraisonRoute1, indiceLivraisonRoute2))) {
                            bestTour = siblingTour;
                            bestDistance = siblingTour.distance();
                            operateurBestTour = new Pair<>(LocalSearchType.RELOCATE_INTER, Arrays.asList(indexRoute1, indexRoute2, indiceLivraisonRoute1, indiceLivraisonRoute2));
                        }
                    }
                }
            }
        }

        //merge
        for (int indexRoute1 = 0; indexRoute1 < initialTour.getRoutes().size()-1; indexRoute1++) {
            for (int indexRoute2 = indexRoute1+1; indexRoute2 < initialTour.getRoutes().size(); indexRoute2++) {
                for (int indice = 0; indice < initialTour.getRoutes().get(indexRoute2).getLivraisons().size(); indice++) {
                    Tour siblingTour = new Tour(initialTour);

                    siblingTour.tryMerge(indexRoute1,indexRoute2,indice);

                    if (siblingTour.distance() < bestDistance &&
                    this.isAllowed(LocalSearchType.MERGE, Arrays.asList(indexRoute1,indexRoute2,indice))) {
                        bestTour = siblingTour;
                        bestDistance=siblingTour.distance();
                        operateurBestTour=new Pair<>(LocalSearchType.MERGE, Arrays.asList(indexRoute1,indexRoute2,indice));
                    }
                }
            }
        }

        //Echange groupe intra
        for (int indexRoute = 0; indexRoute < initialTour.getRoutes().size(); indexRoute++) {
            for (int indiceLivraisonOld1 = 0; indiceLivraisonOld1 < initialTour.getRoutes().get(indexRoute).getLivraisons().size()-1; indiceLivraisonOld1++) {
                for (int indiceLivraisonOld2 = indiceLivraisonOld1+1; indiceLivraisonOld2 < initialTour.getRoutes().get(indexRoute).getLivraisons().size(); indiceLivraisonOld2++) {
                    for (int indiceLivraisonNew = 0; indiceLivraisonNew < initialTour.getRoutes().get(indexRoute).getLivraisons().size(); indiceLivraisonNew++) {

                        Route siblingRoute = new Route(initialTour.getRoutes().get(indexRoute));
                        double distanceBefore = siblingRoute.distance();

                        siblingRoute.tryIntraGroupeExchange(indiceLivraisonOld1,indiceLivraisonOld2,indiceLivraisonNew);

                        if (initialDistance-distanceBefore+siblingRoute.distance() < bestDistance &&
                                this.isAllowed(LocalSearchType.ECHANGE_GROUPE_INTRA, Arrays.asList(indiceLivraisonOld1,indiceLivraisonOld2,indiceLivraisonNew))) {
                            bestTour = new Tour(initialTour);
                            bestTour.getRoutes().set(indexRoute,siblingRoute);
                            bestDistance=initialDistance-distanceBefore+siblingRoute.distance();
                            operateurBestTour=new Pair<>(LocalSearchType.ECHANGE_GROUPE_INTRA, Arrays.asList(indiceLivraisonOld1,indiceLivraisonOld2,indiceLivraisonNew));
                        }
                    }
                }
            }
        }

        //Echange groupe inter
        for (int indexRoute1 = 0; indexRoute1 < initialTour.getRoutes().size(); indexRoute1++) {
            for (int indexRoute2 = indexRoute1 + 1; indexRoute2 < initialTour.getRoutes().size(); indexRoute2++) {
                for (int indiceLivraison1Route1 = 0; indiceLivraison1Route1 < initialTour.getRoutes().get(indexRoute1).getLivraisons().size()-1; indiceLivraison1Route1++) {
                    for (int indiceLivraison2Route1 = indiceLivraison1Route1; indiceLivraison2Route1 < initialTour.getRoutes().get(indexRoute1).getLivraisons().size(); indiceLivraison2Route1++) {
                        for (int indiceLivraison1Route2 = 0; indiceLivraison1Route2 < initialTour.getRoutes().get(indexRoute2).getLivraisons().size()-1; indiceLivraison1Route2++) {
                            for (int indiceLivraison2Route2 = indiceLivraison1Route2; indiceLivraison2Route2 < initialTour.getRoutes().get(indexRoute2).getLivraisons().size(); indiceLivraison2Route2++) {

                                Tour siblingTour = new Tour(initialTour);

                                siblingTour.tryInterGroupeExchange(indexRoute1, indiceLivraison1Route1, indiceLivraison2Route1, indexRoute2, indiceLivraison1Route2, indiceLivraison2Route2);

                                if (siblingTour.distance() < bestDistance &&
                                        this.isAllowed(LocalSearchType.ECHANGE_GROUPE_INTER, Arrays.asList(indexRoute1, indexRoute2, indiceLivraison1Route1, indiceLivraison2Route1, indiceLivraison1Route2, indiceLivraison2Route2))) {
                                    bestTour = siblingTour;
                                    bestDistance = siblingTour.distance();
                                    operateurBestTour = new Pair<>(LocalSearchType.ECHANGE_GROUPE_INTER, Arrays.asList(indexRoute1, indexRoute2, indiceLivraison1Route1, indiceLivraison2Route1, indiceLivraison1Route2, indiceLivraison2Route2));
                                }
                            }
                        }
                    }
                }
            }
        }

        //Relocate groupe inter
        for (int indexRoute1 = 0; indexRoute1 < initialTour.getRoutes().size(); indexRoute1++) {
            for (int indexRoute2 = indexRoute1 + 1; indexRoute2 < initialTour.getRoutes().size(); indexRoute2++) {
                for (int indiceLivraison1 = 0; indiceLivraison1 < initialTour.getRoutes().get(indexRoute1).getLivraisons().size()-1; indiceLivraison1++) {
                    for (int indiceLivraison2 = indiceLivraison1; indiceLivraison2 < initialTour.getRoutes().get(indexRoute1).getLivraisons().size(); indiceLivraison2++) {
                        for (int indiceLivraisonDestination = 0; indiceLivraisonDestination < initialTour.getRoutes().get(indexRoute2).getLivraisons().size(); indiceLivraisonDestination++) {
                            Tour siblingTour = new Tour(initialTour);

                            siblingTour.tryRelocateGroupeInter(indexRoute1,indiceLivraison1,indiceLivraison2,indexRoute2,indiceLivraisonDestination);

                            if (siblingTour.distance() < bestDistance &&
                                    this.isAllowed(LocalSearchType.RELOCATE_GROUPE_INTER, Arrays.asList(indexRoute1,indiceLivraison1,indiceLivraison2,indexRoute2,indiceLivraisonDestination))) {
                                bestTour = siblingTour;
                                bestDistance = siblingTour.distance();
                                operateurBestTour = new Pair<>(LocalSearchType.RELOCATE_GROUPE_INTER, Arrays.asList(indexRoute1,indiceLivraison1,indiceLivraison2,indexRoute2,indiceLivraisonDestination));
                                System.out.println("bestDistance -> "+bestDistance);
                            }
                        }
                    }
                }
            }
        }

        if(bestDistance>=initialDistance)
        {
            System.out.println("Aucune meilleur solution n'a été trouvée, remonté et ajout de l'opérateur à la liste de tabou "+operateurBestTour.getKey()+": "+operateurBestTour.getValue()+" de "+initialDistance+" -> "+bestDistance);
            addTabou(operateurBestTour);
        }
        else {
            System.out.println(operateurBestTour.getKey()+" "+operateurBestTour.getValue()+" de "+initialDistance+" -> "+bestDistance);
        }

        return bestTour;
    }
}
