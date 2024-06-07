package org.polytech.vue;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.polytech.model.Client;

public class CircleClient extends Location {
    private Color initialColor;
    private double initialRadius;
    private Client client;

    public CircleClient(Client client, double x, double y, double radius) {
        this(x, y, radius, Color.BLUE);
        this.client = client;
    }

    public CircleClient(Client client, double x, double y, double radius, Color color) {
        this(x, y, radius, color);
        this.client = client;
    }

    public CircleClient(double x, double y, double radius, Color color) {
        super(x, y, radius);
        this.initialRadius = radius;
        this.initialColor = color;
        this.setFill(color);
    }

    @Override
    public void onTooltipShow(MouseEvent e) {
        this.tooltip.setText("Client " + this.client.getId() + " X: " + this.getCenterX() + " Y: " + this.getCenterY());
        this.tooltip.show(this, e.getScreenX(), e.getScreenY());
    }

    private void resetFill() {
        this.setFill(this.initialColor);
    }

    public void select() {
        this.setFill(Color.BLUE);
        this.setRadius(this.getRadius() * 1.25);
    }

    public void deSelect() {
        this.resetFill();
        this.setRadius(this.initialRadius);
    }
}
