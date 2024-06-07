package org.polytech.vue;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CircleDepot extends Location {
    public CircleDepot(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
    }

    @Override
    public void onTooltipShow(MouseEvent e) {
        this.tooltip.show(this, e.getScreenX(), e.getScreenY());
    }

    public CircleDepot(double centerX, double centerY, double radius, Color color) {
        super(centerX, centerY, radius);
        this.setFill(color);
    }
}
