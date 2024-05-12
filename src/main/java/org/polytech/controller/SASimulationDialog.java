package org.polytech.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Window;
import org.polytech.App;
import org.polytech.simulation.simulatedannaealing.SimulatedAnnealingConfig;
import org.polytech.simulation.simulatedannaealing.SimulatedAnnealingParameter;

import java.io.File;
import java.util.Objects;

public class SASimulationDialog extends Dialog<SimulatedAnnealingConfig> {
    @FXML
    private ButtonType validParametersButtonType;

    @FXML
    private TextField initialTemperatureField;
    @FXML
    private TextField iterationsField;
    @FXML
    private TextField coolingRateField;
    @FXML
    private TextField iterationsPerNeighborField;
    @FXML
    private Label repeatedTimesLabel;
    @FXML
    private TextField repeatedTimesField;
    @FXML
    private Label fileChooseLabel;

    private File exportFile;

    private final ObjectProperty<SimulatedAnnealingConfig> simulationAnnealingParameterObjectProperty = new SimpleObjectProperty<>(null);


    public SASimulationDialog() {
    }

    public File getExportFile() {
        return exportFile;
    }

    public void hideRepeatField() {
        this.repeatedTimesLabel.setVisible(false);
        this.repeatedTimesField.setVisible(false);
    }

    public SASimulationDialog(Window owner) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/sa_simulation_dialog.fxml"));
            loader.setController(this);

            DialogPane dialogPane = loader.load();
            dialogPane.lookupButton(dialogPane.getButtonTypes().get(0)).addEventFilter(ActionEvent.ACTION, this::onValidParametersButtonClicked);
            setDialogPane(dialogPane);

        } catch (Exception e) {
            e.printStackTrace();
        }

        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);

        this.setTitle("Paramètres de la simulation");
        this.setHeaderText("Veuillez entrer les paramètres de la simulation");

        setResultConverter(buttonType -> {
            if(!Objects.equals(ButtonBar.ButtonData.OK_DONE, buttonType.getButtonData())) {
                return null;
            }

            return this.simulationAnnealingParameterObjectProperty.getValue();
        });
    }

    @FXML
    private void onValidParametersButtonClicked(ActionEvent actionEvent) {
        try {
            double initialTemperature = Double.parseDouble(this.initialTemperatureField.getText());
            int iterations = Integer.parseInt(this.iterationsField.getText());
            double coolingRate = Double.parseDouble(this.coolingRateField.getText());
            int iterationsPerNeighbor = Integer.parseInt(this.iterationsPerNeighborField.getText());
            int repeatedTimes = this.repeatedTimesField.getText().isEmpty() ? 1 : Integer.parseInt(this.repeatedTimesField.getText());

            SimulatedAnnealingParameter simulatedAnnealingParameter = new SimulatedAnnealingParameter(iterations, iterationsPerNeighbor, coolingRate, initialTemperature);
            this.simulationAnnealingParameterObjectProperty.setValue(new SimulatedAnnealingConfig(simulatedAnnealingParameter, repeatedTimes));
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        actionEvent.consume();
    }

    @FXML
    private void chooseExportFile(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier de clients");
        this.exportFile = fileChooser.showOpenDialog(App.getStage());
        this.fileChooseLabel.setText(this.exportFile.getName());
    }

    @FXML
    private void initialize() {
        System.out.println("SASimulationDialog initialize");
    }
}
