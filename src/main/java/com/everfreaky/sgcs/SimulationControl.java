package com.everfreaky.sgcs;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;

public class SimulationControl extends VBox {
    // JavaFX label for pane title
    private final Label parameterTitle;
    // JavaFX button to start the simulation
    private final Button startButton;
    // JavaFX label for robot count text field
    private final Label countLabel;
    // JavaFX text field for robot count
    private final TextField countField;
    // JavaFX label for communication range text field
    private final Label commRangeLabel;
    // JavaFX text field for the communication range
    private final TextField commRangeField;
    // JavaFX label for the failure chance field
    private final Label failureChanceLabel;
    // JavaFX text field for the failure chance
    private final TextField failureChanceField;
    // JavaFX label for the title of the simulation
    private final Label simulationTitle;
    // JavaFX label for the simulation parameters title
    private final Label simulationParametersLabel;
    // JavaFX label for the simulation parameters
    private final Label simulationParameters;
    // JavaFX button to pause and play the simulation
    private final Button pausePlayButton;
    // has the simulation begun
    private boolean isHome = true;
    // singleton instance
    public static final SimulationControl instance = new SimulationControl(Screen.getPrimary().getBounds().getWidth() / 2, 0, Screen.getPrimary().getBounds().getWidth() / 2, Screen.getPrimary().getBounds().getHeight());
    // prevent initialization
    private SimulationControl(double x, double y, double width, double height) {
        // set UI parameters
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setPrefSize(width, height);
        this.setStyle("-fx-border-color: black");
        this.setAlignment(Pos.CENTER);
        this.setSpacing(height / 100);
        // get loop instance
        Loop loop = Loop.getInstance();
        // set the pane title and font
        parameterTitle = new Label("SGCS Simulation Parameters");
        parameterTitle.setFont(new Font("Arial", 48));
        // robot count parameter label and text field
        countLabel = new Label("Robot count(1-1000): ");
        countLabel.setFont(new Font("Arial", 36));
        countField = new TextField();
        countField.setMaxWidth(600);
        countField.setText("1");
        // verify value on text change
        countField.textProperty().addListener((ObservableValue< ? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("^([0-9]{0,3}|1000)$")) {
                Platform.runLater(() -> {
                    countField.setText(oldValue);
                    countField.positionCaret(countField.getLength());
                });
            }
        });
        // verify format on loss of focus
        countField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue && !countField.getText().matches("^([1-9][0-9]{0,2}|1000)$")) {
                countField.setText("1");
            }
        });
        // communication range label and text field
        commRangeLabel = new Label("Communication range(1-100): ");
        commRangeLabel.setFont(new Font("Arial", 36));
        commRangeField = new TextField();
        commRangeField.setMaxWidth(600);
        commRangeField.setText("1");
        // verify value on text change
        commRangeField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("^([0-9]{0,2}|100)$")) {
                Platform.runLater(() -> {
                    commRangeField.setText(oldValue);
                    commRangeField.positionCaret(commRangeField.getLength());
                });
            }
        });
        // verify value on loss of focus
        commRangeField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue && !commRangeField.getText().matches("^([1-9][0-9]?|100)$")) {
                commRangeField.setText("1");
            }
        });
        // failure chance lable and text field
        failureChanceLabel = new Label("Failure chance(0 - 1): ");
        failureChanceLabel.setFont(new Font("Arial", 36));
        failureChanceField = new TextField();
        failureChanceField.setMaxWidth(600);
        failureChanceField.setText("0");
        // verify value on text change
        failureChanceField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("^((0([.,][0-9]{0,15})?)?)|1$")) {
                Platform.runLater(() -> {
                    failureChanceField.setText(oldValue);
                    failureChanceField.positionCaret(failureChanceField.getLength());
                });
            }
        });
        // verify value on loss of focus
        failureChanceField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue && !failureChanceField.getText().matches("^(0[,.][0-9]{0,15})|1$")) {
                failureChanceField.setText("0");
            }
        });
        // button to start the simulation
        startButton = new Button("Start Simulation");
        startButton.setFont(new Font("Arial", 36));
        startButton.setDefaultButton(true);
        // start the simulation on click
        startButton.setOnAction((ActionEvent e) -> startSimulation());
        setScene();
        // running simulation UI
        simulationTitle = new Label("SGCS Simulation");
        simulationTitle.setFont(new Font("Arial", 48));
        simulationParametersLabel = new Label("Simulation parameters:");
        simulationParametersLabel.setFont(new Font("Arial", 36));
        simulationParameters = new Label("No simulation parameters entered.");
        simulationParameters.setFont(new Font("Arial", 24));
        // pause-play button
        pausePlayButton = new Button("Pause");
        pausePlayButton.setFont(new Font("Arial", 36));
        pausePlayButton.setDefaultButton(true);
        pausePlayButton.setOnAction((ActionEvent e) -> {
            if (loop.isPlaying()) {
                loop.pause();
                pausePlayButton.setText("Play");
            } else {
                loop.play();
                pausePlayButton.setText("Pause");
            }
        });
    }
    // get the singleton instance
    public static SimulationControl getInstance() {
        return instance;
    }
    private void startSimulation() {
        // get the animation loop and simulation instances
        Loop loop = Loop.getInstance();
        Simulation sim = Simulation.getInstance();
        this.isHome = false;
        // verify the set parameters and set them to default values if they are incorrect
        if (!countField.getText().matches("^([1-9][0-9]{0,2}|1000)$")) {
            countField.setText("1");
        }
        if (!commRangeField.getText().matches("^([1-9][0-9]?|100)$")) {
            commRangeField.setText("1");
        }
        if (!failureChanceField.getText().matches("^(0[,.][0-9]{0,15})|1$")) {
            failureChanceField.setText("0");
        }
        // set the simulation parameters
        sim.setParameters(Integer.parseInt(countField.getText()), Integer.parseInt(commRangeField.getText()), Double.parseDouble(failureChanceField.getText()));
        // start the animation loop
        loop.start();
        loop.play();
        setScene();
    }
    // switch between home and running simulation UI
    private void setScene() {
        // get the simulation instance
        Simulation sim = Simulation.getInstance();
        this.getChildren().clear();
        // if setting parameters
        if (this.isHome) {
            this.getChildren().addAll(parameterTitle, countLabel, countField, commRangeLabel, commRangeField, failureChanceLabel, failureChanceField, startButton);
        } else {
            simulationParameters.setText(String.format("Robot count: %d\nCommunication range: %d\nFailure chance: %f", sim.getCount(), sim.getCommRange(), sim.getFailureChance()));
            this.getChildren().addAll(simulationTitle, simulationParametersLabel, simulationParameters, pausePlayButton);
        }
    }
}
