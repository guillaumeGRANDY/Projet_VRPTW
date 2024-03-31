package org.polytech.vue;

import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public abstract class Location extends Circle {
    protected Tooltip tooltip;

    public Location(double x, double y, double radius) {
        super(x, y, radius);
        this.tooltip = new Tooltip("X: " + x + " Y: " + y);

        this.setOnMouseClicked(this::onTooltipShow);
        this.tooltip.setAutoHide(true);
        this.setCursor(javafx.scene.Cursor.HAND);
    }

    public abstract void onTooltipShow(MouseEvent e);
}
