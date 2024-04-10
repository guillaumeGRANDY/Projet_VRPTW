package org.polytech.algorithm.localsearch;

import org.polytech.model.Route;
import org.polytech.model.Tour;

public abstract class HillClimbing {
    protected final Tour initial;

    public HillClimbing(Tour initial) {
        this.initial = new Tour(initial.getRoutes());
    }

    public Tour localSearch() throws HillClimbingException {
        if(initial == null || initial.getRoutes().isEmpty()) {
            throw new HillClimbingException("La tournée est vide, impossible à optimiser");
        }
        Tour current = new Tour(initial.getRoutes());
        Tour sibling = null;

        boolean isExplorationFinish = false;

        while (!isExplorationFinish) {
            sibling = exploreSiblings(current);

            if(sibling.distance() < current.distance()) {
                current = sibling;
            } else {
                isExplorationFinish = true;
            }
        }

        return current;
    }

    /**
     * Explore les voisins d'une solution initiale
     * @param initial la solution initiale à améliorer
     * @return la tournée améliorée
     */
    public abstract Tour exploreSiblings(Tour initial);

    protected abstract Route exploreSiblings(Route route);
}
