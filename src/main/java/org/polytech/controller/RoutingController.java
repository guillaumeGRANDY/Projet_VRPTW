package org.polytech.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.polytech.algorithm.tour.AlgoTourne;
import org.polytech.algorithm.tour.AlgoTourneDemandeCroissante;
import org.polytech.algorithm.tour.AlgoTourneRandom;
import org.polytech.model.*;
import org.polytech.parser.LocationParser;
import org.polytech.vue.CircleDepot;
import org.polytech.vue.Connector;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static org.polytech.controller.AlgorithmType.RANDOM;

public class RoutingController implements Initializable {
    @FXML
    private Button startAlgoButton;
    @FXML
    private ComboBox<AlgorithmType> algorithmChoice;
    @FXML
    private SplitPane map;
    @FXML
    private Label totalDistanceLabel;

    private Group mapGroup;
    private List<Connector> connectors = new ArrayList<>();

    private int scaleFactor = 10;

    private HashMap<Depot, Circle> depotCircles = new HashMap<>();
    private HashMap<Client, Circle> clientsCircle = new HashMap<>();
    private LocationParser locationParser = new LocationParser();
    private List<Client> clients = new ArrayList<>();
    private Depot begin;
    private ConstraintTruck constraintTruck;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.algorithmChoice.setItems(FXCollections.observableList(Arrays.stream(AlgorithmType.values()).toList()));
        this.algorithmChoice.setValue(RANDOM);

        this.mapGroup = new Group();
        this.map.getItems().add(mapGroup);

        try {
            this.locationParser.parseFile("data101.vrp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.clients = this.locationParser.getClients();
        this.begin = this.locationParser.getDepot();
        this.constraintTruck = new ConstraintTruck(this.locationParser.getMaxQuantity());

        initMap();

        this.startAlgoButton.setOnAction(actionEvent -> {
            switch (this.algorithmChoice.getValue()) {
                case RANDOM -> {
                    AlgoTourne algoTourne = new AlgoTourneRandom(this.constraintTruck,
                            this.begin,
                            this.clients);
                    Tour tour = algoTourne.makeTour();

                    this.makeTourne(tour);
                }
                case CROISSANT_SORT -> {
                    AlgoTourne algoTourne = new AlgoTourneDemandeCroissante(this.constraintTruck,
                            this.begin,
                            this.clients);
                    Tour tour = algoTourne.makeTour();

                    this.makeTourne(tour);
                }
            }
        });
    }


    private void resetArrowsMap() {
        this.mapGroup.getChildren().removeAll(this.connectors);
    }

    private void makeTourne(Tour tour) {
        this.resetArrowsMap();

        for (Route route : tour.getRoutes()) {
            Depot beginDepotRoute = route.getBegin();
            Circle beginDepotRouteCircle = this.depotCircles.get(beginDepotRoute);
            if (beginDepotRouteCircle == null) {
                beginDepotRouteCircle = new CircleDepot(beginDepotRoute.getX() * scaleFactor,
                        beginDepotRoute.getY() * scaleFactor,
                        10,
                        new Color(0, 1, 0, 1));
                this.mapGroup.getChildren().add(beginDepotRouteCircle);
                this.depotCircles.put(beginDepotRoute, beginDepotRouteCircle);
            }
            List<Client> clients = route.getClients();

            Connector lineArrowFromBeginToFirstClient = new Connector(beginDepotRouteCircle, clientsCircle.get(clients.getFirst()));
            this.mapGroup.getChildren().add(lineArrowFromBeginToFirstClient);
            this.connectors.add(lineArrowFromBeginToFirstClient);

            for (int i = 1; i < clients.size(); i++) {
                Client ancestor = clients.get(i - 1);
                Client current = clients.get(i);

                Connector lineArrowFromClientToClient = new Connector(clientsCircle.get(ancestor), clientsCircle.get(current));
                this.mapGroup.getChildren().add(lineArrowFromClientToClient);
                this.connectors.add(lineArrowFromClientToClient);
            }
            Connector lineArrowFromLastClientToBegin = new Connector(clientsCircle.get(clients.getLast()), beginDepotRouteCircle);
            this.mapGroup.getChildren().add(lineArrowFromLastClientToBegin);
            this.connectors.add(lineArrowFromLastClientToBegin);
        }

        this.totalDistanceLabel.setText(String.valueOf(tour.totalDistance()));
    }

    private void initMap() {
        Circle beginCircle = new CircleDepot(begin.getX() * scaleFactor,
                begin.getY() * scaleFactor,
                10,
                new Color(0, 1, 0, 1));
        mapGroup.getChildren().add(beginCircle);
        this.depotCircles.put(begin, beginCircle);

        for (Client client : clients) {
            Circle circle = new Circle(client.getX() * scaleFactor, client.getY() * scaleFactor, 3);

            mapGroup.getChildren().add(circle);
            clientsCircle.put(client, circle);
        }
    }
}