module Projet.VRPTW {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.polytech.controller to javafx.fxml;
    exports org.polytech;
    opens org.polytech.algorithm.tour to javafx.fxml;
}