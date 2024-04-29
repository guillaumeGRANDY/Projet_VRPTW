package org.polytech.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import org.polytech.App;
import org.polytech.algorithm.LocalSearchType;
import org.polytech.algorithm.globalsearch.SimulatedAnnealing;
import org.polytech.algorithm.globalsearch.TabuSearch;
import org.polytech.algorithm.globalsearch.TabuSearch2;
import org.polytech.algorithm.localsearch.HillClimbingEchangeInter;
import org.polytech.algorithm.localsearch.HillClimbingException;
import org.polytech.algorithm.localsearch.LocalSearchFactory;
import org.polytech.algorithm.tour.*;
import org.polytech.model.*;
import org.polytech.parser.LocationParser;
import org.polytech.vue.CircleClient;
import org.polytech.vue.CircleDepot;
import org.polytech.vue.Connector;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static org.polytech.algorithm.tour.AlgorithmType.RANDOM;

public class RoutingController implements Initializable {
    @FXML
    private Label totalDemand;
    @FXML
    private Label fitness;
    @FXML
    private Button startRecuitSimule;
    @FXML
    private Button startTabuSearch;
    @FXML
    private Button startLocalSearchButton;
    @FXML
    private Button startInitialAlgoButton;
    @FXML
    private ComboBox<AlgorithmType> initialAlgorithmComboBox;
    @FXML
    private ComboBox<LocalSearchType> localSearchTypeComboBox;
    @FXML
    private SplitPane map;
    @FXML
    private TableView<Client> clientsTable;
    @FXML
    private TableView<Route> routesTable;

    private Group mapGroup;
    private final List<Connector> connectors = new ArrayList<>();
    private int scaleFactor = 10;

    private final HashMap<Depot, Circle> depotCircles = new HashMap<>();
    private final HashMap<Client, CircleClient> clientsCircle = new HashMap<>();
    private HashMap<Route, Color> routeColor = new HashMap<>();
    private final HashMap<Route, List<Connector>> routeConnectors = new HashMap<>();

    private LocationParser locationParser = new LocationParser();
    private List<Client> clients = new ArrayList<>();
    private Depot begin;
    private ConstraintTruck constraintTruck;

    private Tour tour = null;
    private List<Route> routes = new ArrayList<>();

    private TabuSearch2 tabuSearch = new TabuSearch2();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initialAlgorithmComboBox.setItems(FXCollections.observableList(Arrays.stream(AlgorithmType.values()).toList()));
        this.initialAlgorithmComboBox.setValue(RANDOM);

        this.localSearchTypeComboBox.setItems(FXCollections.observableList(Arrays.stream(LocalSearchType.values()).toList()));
        this.localSearchTypeComboBox.setValue(LocalSearchType.ECHANGE_INTRA);

        this.mapGroup = new Group();
        this.map.getItems().add(mapGroup);
        initRoutesTableView();
        this.initClientsTableView();

        this.initFileTest();

        this.startInitialAlgoButton.setOnAction(actionEvent -> {
            this.tour = AlgoTourFactory.makeTour(this.constraintTruck,
                    this.begin,
                    this.clients,
                    this.initialAlgorithmComboBox.getValue());
            this.routes = this.tour.getRoutes();
            this.makeTourne();
        });

        this.startLocalSearchButton.setOnAction(actionEvent -> {
            if (this.tour == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Aucun tour n'a été généré");
                alert.setContentText("Veuillez générer un tour avant de lancer la recherche locale");
                alert.showAndWait();
                return;
            }

            LocalSearchType localSearchType = this.localSearchTypeComboBox.getValue();
            try {
                this.tour = LocalSearchFactory.makeLocalSearch(this.tour, localSearchType);
            } catch (HillClimbingException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur lors de la recherche locale");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
            this.routes = this.tour.getRoutes();
            this.makeTourne();
        });

        this.startRecuitSimule.setOnAction(actionEvent -> {
            if (this.tour == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Aucun tour n'a été généré");
                alert.setContentText("Veuillez générer un tour avant de lancer la recherche locale");
                alert.showAndWait();
                return;
            }

            SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(1000, 0.96);
            this.tour = simulatedAnnealing.explore(this.tour, 0.5);
            this.routes = tour.getRoutes();
            this.makeTourne();
        });

        this.startTabuSearch.setOnAction(actionEvent -> {
            if (this.tour == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Aucun tour n'a été généré");
                alert.setContentText("Veuillez générer un tour avant de lancer la recherche locale");
                alert.showAndWait();
                return;
            }


            this.tour = tabuSearch.explore(this.tour, 2,1);
            this.routes = tour.getRoutes();
            this.makeTourne();
        });
    }

    private void initClientsTableView() {
        this.clientsTable.setItems(FXCollections.observableList(this.clients));

        TableColumn<Client, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));

        TableColumn<Client, Integer> xColumn = new TableColumn<>("X");
        xColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getX()).asObject());

        TableColumn<Client, Integer> yColumn = new TableColumn<>("Y");
        yColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getY()).asObject());

        TableColumn<Client, Integer> readyTimeColumn = new TableColumn<>("Heure de début");
        readyTimeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getReadyTime()).asObject());

        TableColumn<Client, Integer> dueTimeColumn = new TableColumn<>("Heure de fin");
        dueTimeColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDueTime()).asObject());

        TableColumn<Client, Integer> demandColumn = new TableColumn<>("Demande");
        demandColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDemand()).asObject());

        TableColumn<Client, Integer> serviceColumn = new TableColumn<>("Temps de service");
        serviceColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getService()).asObject());

        this.clientsTable.getColumns().addAll(idColumn, xColumn, yColumn, readyTimeColumn, dueTimeColumn, demandColumn, serviceColumn);
        this.clientsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            CircleClient circleClientSelected = this.clientsCircle.get(newValue);
            CircleClient circleClientOldSelected = this.clientsCircle.get(oldValue);
            if (newValue != null) {
                circleClientSelected.select();
                if(oldValue != null) circleClientOldSelected.deSelect();
            }
        });
    }

    private void initRoutesTableView() {
        this.routesTable.setItems(FXCollections.observableList(this.routes));

        TableColumn<Route, String> beginColumn = new TableColumn<>("Départ");
        beginColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBegin().toString()));
        this.routesTable.getColumns().add(beginColumn);

        TableColumn<Route, String> clientsColumn = new TableColumn<>("Clients");
        clientsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
        this.routesTable.getColumns().add(clientsColumn);

        TableColumn<Route, Integer> quantityColumn = new TableColumn<>("Nombre clients");
        quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getClients().size()).asObject());
        this.routesTable.getColumns().add(quantityColumn);

        TableColumn<Route, Double> distanceColumn = new TableColumn<>("Distance");
        distanceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().distance()).asObject());
        this.routesTable.getColumns().add(distanceColumn);

        TableColumn<Route, Integer> truckSpaceColumn = new TableColumn<>("Capacité courante camion");
        truckSpaceColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTruck().getPlaceRemaning()).asObject());
        this.routesTable.getColumns().add(truckSpaceColumn);

        this.routesTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                this.routeColor.forEach((route, color) -> {
                    if (route.equals(newValue)) {
                        this.routeConnectors.get(route).forEach(connector -> {
                            connector.setStrokeWidth(4);
                            connector.setFill(Color.BLACK);
                        });
                    } else {
                        this.routeConnectors.get(route).forEach(connector -> {
                            connector.setFill(color);
                            connector.setStrokeWidth(1);
                        });
                    }
                });
            }
        });
    }

    private void resetArrowsMap() {
        this.mapGroup.getChildren().removeAll(this.connectors);
    }

    private void makeTourne() {
        this.resetArrowsMap();
        this.routeColor = new HashMap<>();

        Color color = new Color(0, 0, 1, 1);
        for (Route route : this.tour.getRoutes()) {
            this.routeColor.put(route, color);

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

            Connector lineArrowFromBeginToFirstClient = new Connector(begin, clients.getFirst(), beginDepotRouteCircle, clientsCircle.get(clients.getFirst()));
            lineArrowFromBeginToFirstClient.setFill(color);
            this.mapGroup.getChildren().add(lineArrowFromBeginToFirstClient);
            this.connectors.add(lineArrowFromBeginToFirstClient);
            this.routeConnectors.put(route, new ArrayList<>(List.of(lineArrowFromBeginToFirstClient)));

            for (int i = 1; i < clients.size(); i++) {
                Client ancestor = clients.get(i - 1);
                Client current = clients.get(i);

                Connector lineArrowFromClientToClient = new Connector(ancestor, current, clientsCircle.get(ancestor), clientsCircle.get(current));
                lineArrowFromClientToClient.setFill(color);
                this.mapGroup.getChildren().add(lineArrowFromClientToClient);
                this.connectors.add(lineArrowFromClientToClient);
                this.routeConnectors.get(route).add(lineArrowFromClientToClient);
            }
            Connector lineArrowFromLastClientToBegin = new Connector(clients.getLast(), begin, clientsCircle.get(clients.getLast()), beginDepotRouteCircle);
            lineArrowFromLastClientToBegin.setFill(color);
            this.mapGroup.getChildren().add(lineArrowFromLastClientToBegin);
            this.connectors.add(lineArrowFromLastClientToBegin);
            this.routeConnectors.get(route).add(lineArrowFromLastClientToBegin);

            color = new Color(Math.random(), Math.random(), Math.random(), 1);
        }
        this.routesTable.setItems(FXCollections.observableList(this.tour.getRoutes()));
        this.fitness.setText("Fitness: " + this.tour.distance());
    }

    private void initClientsCircle() {
        for (Client client : clients) {
            CircleClient circleClient = new CircleClient(client, client.getX() * scaleFactor, client.getY() * scaleFactor, 5, new Color(1, 0, 0, 1));
            circleClient.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
                if (clientsTable.getSelectionModel().getSelectedItem() != client) {
                    clientsTable.getSelectionModel().select(client);
                    clientsTable.scrollTo(client);
                } else {
                    clientsTable.getSelectionModel().clearSelection();
                }
            });

            mapGroup.getChildren().add(circleClient);
            clientsCircle.put(client, circleClient);
        }
    }

    private void setClients(List<Client> clients) {
        this.clients = new ArrayList<>(clients);
        this.clientsTable.setItems(FXCollections.observableList(this.clients));
        this.initClientsCircle();
    }

    private void setBegin(Depot depot) {
        this.begin = depot;
        Circle beginCircle = new CircleDepot(begin.getX() * scaleFactor,
                begin.getY() * scaleFactor,
                10,
                new Color(0, 1, 0, 1));
        mapGroup.getChildren().add(beginCircle);
        this.depotCircles.put(begin, beginCircle);
    }

    @FXML
    protected void importVrpFileFromFolder() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier de clients");
        File choosedFile = fileChooser.showOpenDialog(App.getStage());
        if (choosedFile == null) return;

        try {
            this.locationParser.parseFile(choosedFile);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de la lecture du fichier");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        this.mapGroup.getChildren().clear();
        this.setClients(this.locationParser.getClients());

        this.depotCircles.clear();
        this.setBegin(this.locationParser.getDepot());

        this.constraintTruck = new ConstraintTruck(this.locationParser.getMaxQuantity());

        this.routeColor.clear();
        this.routeConnectors.clear();
        this.routes.clear();
        this.tour = null;
    }

    private void initFileTest() {
        try {
            this.locationParser.parseFile("sample.vrp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.setClients(this.locationParser.getClients());
        this.setBegin(this.locationParser.getDepot());
        this.constraintTruck = new ConstraintTruck(this.locationParser.getMaxQuantity());
        int totalDemand = 0;
        for (Client client : this.locationParser.getClients()) {
            totalDemand += client.getDemand();
        }
        this.totalDemand.setText(String.valueOf(totalDemand));
    }
}