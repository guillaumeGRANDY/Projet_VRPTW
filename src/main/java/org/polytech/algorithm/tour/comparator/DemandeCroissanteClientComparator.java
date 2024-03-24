package org.polytech.algorithm.tour.comparator;

import org.polytech.model.Client;

import java.util.Comparator;

public class DemandeCroissanteClientComparator implements Comparator<Client> {
    @Override
    public int compare(Client c1, Client c2) {
        return Integer.compare(c1.getDemand(), c2.getDemand());
    }
}
