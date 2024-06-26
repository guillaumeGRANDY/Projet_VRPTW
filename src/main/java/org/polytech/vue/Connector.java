package org.polytech.vue;

import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import org.polytech.model.Client;
import org.polytech.model.Location;

public class Connector extends Path {
    private Tooltip tooltip;
    private static final double defaultArrowHeadSize = 10.0;
    private final double startPointX;
    private final double startPointY;
    private final double endPointX;
    private final double endPointY;

    public Connector(Circle beginCircle, Circle endCircle, double arrowHeadSize){
        super();
        double startX = beginCircle.getCenterX();
        double startY = beginCircle.getCenterY();
        double startRadius = beginCircle.getRadius();
        double endX = endCircle.getCenterX();
        double endY = endCircle.getCenterY();
        double endRadius = endCircle.getRadius();
        strokeProperty().bind(fillProperty());
        setFill(Color.BLACK);

        // Calculate the angle between the two circles
        double angle = Math.atan2(endY - startY, endX - startX);

        // Calculate the new start point on the edge of the start circle
        this.startPointX = startX + startRadius * Math.cos(angle);
        this.startPointY = startY + startRadius * Math.sin(angle);

        // Calculate the new end point on the edge of the end circle
        this.endPointX = endX - endRadius * Math.cos(angle);
        this.endPointY = endY - endRadius * Math.sin(angle);

        // Line
        getElements().add(new MoveTo(startPointX, startPointY));
        getElements().add(new LineTo(endPointX, endPointY));

        // ArrowHead
        angle -= Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        // point1
        double x1 = (-1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endPointX;
        double y1 = (-1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endPointY;

        // point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endPointX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endPointY;

        getElements().add(new LineTo(x1, y1));
        getElements().add(new LineTo(x2, y2));
        getElements().add(new LineTo(endPointX, endPointY));
    }

    public Connector(Circle beginCircle, Circle endCircle){
        this(beginCircle, endCircle, defaultArrowHeadSize);
    }

    public Connector(Location first, Location next, Circle begin, Circle end) {
        this(begin, end, defaultArrowHeadSize);

        this.tooltip = new Tooltip(String.valueOf(first.distanceWith(next)));

        this.setOnMouseClicked(this::onTooltipShow);
        this.tooltip.setAutoHide(true);
    }

    public void onTooltipShow(MouseEvent e) {
        this.tooltip.show(this, e.getScreenX(), e.getScreenY());
    }
}
