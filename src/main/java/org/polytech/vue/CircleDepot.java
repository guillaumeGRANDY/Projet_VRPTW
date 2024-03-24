package org.polytech.vue;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CircleDepot extends Circle {
    public CircleDepot(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
    }

    public CircleDepot(double centerX, double centerY, double radius, Color color) {
        super(centerX, centerY, radius);
        this.setFill(color);
    }
}
