package org.polytech;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class App extends Application {
    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        URL fxmlFile = new File("src/main/resources/fxml/routing.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(fxmlFile);
        Scene scene = new Scene(root);
        primaryStage.setTitle("PolyLivraison");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(540);
        primaryStage.setMinWidth(540);
        App.stage = primaryStage;
        stage.show();
    }
}
