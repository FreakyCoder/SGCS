package com.everfreaky.sgcs;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class SimulationControl extends VBox {
    private final Simulation sim;
    private final Loop loop;
    private final Label parameterTitle;
    private final Button startButton;
    private final Label countLabel;
    private final TextField countField;
    private final Label commRangeLabel;
    private final TextField commRangeField;
    private final Label failureChanceLabel;
    private final TextField failureChanceField;
    private final Label simulationTitle;
    private final Label simulationParametersLabel;
    private final Label simulationParameters;
    private final Button pausePlayButton;
    private boolean isHome = true;
    public SimulationControl(double x, double y, double width, double height, Simulation sim, Loop loop) {
        this.sim = sim;
        this.loop = loop;
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setPrefSize(width, height);
        this.setStyle("-fx-border-color: black");
        this.setAlignment(Pos.CENTER);
        this.setSpacing(height / 100);
        parameterTitle = new Label("SGCS Simulation Parameters");
        parameterTitle.setFont(new Font("Arial", 48));
        countLabel = new Label("Robot count(1-1000): ");
        countLabel.setFont(new Font("Arial", 36));
        countField = new TextField();
        countField.setMaxWidth(600);
        countField.setText("1");
        countField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^([0-9]{0,3}|1000)$")) {
                    Platform.runLater(() -> {
                        countField.setText(oldValue);
                        countField.positionCaret(countField.getLength());
                    });
                }
            }
        });
        countField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue && !countField.getText().matches("^([1-9][0-9]{0,2}|1000)$")) {
                    countField.setText("1");
                }
            }
        });
        commRangeLabel = new Label("Communication range(1-100): ");
        commRangeLabel.setFont(new Font("Arial", 36));
        commRangeField = new TextField();
        commRangeField.setMaxWidth(600);
        commRangeField.setText("1");
        commRangeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^([0-9]{0,2}|100)$")) {
                    Platform.runLater(() -> {
                       commRangeField.setText(oldValue);
                       commRangeField.positionCaret(commRangeField.getLength());
                    });
                }
            }
        });
        commRangeField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue && !commRangeField.getText().matches("^([1-9][0-9]?|100)$")) {
                    commRangeField.setText("1");
                }
            }
        });
        failureChanceLabel = new Label("Failure chance(0 - 1): ");
        failureChanceLabel.setFont(new Font("Arial", 36));
        failureChanceField = new TextField();
        failureChanceField.setMaxWidth(600);
        failureChanceField.setText("0");
        failureChanceField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^((0([.,][0-9]{0,15})?)?)|1$")) {
                    Platform.runLater(() -> {
                        failureChanceField.setText(oldValue);
                        failureChanceField.positionCaret(failureChanceField.getLength());
                    });
                }
            }
        });
        failureChanceField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!newPropertyValue && !failureChanceField.getText().matches("^(0[,.][0-9]{0,15})|1$")) {
                    failureChanceField.setText("0");
                }
            }
        });
        startButton = new Button("Start Simulation");
        startButton.setFont(new Font("Arial", 36));
        startButton.setDefaultButton(true);
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startSimulation();
            }
        });
        setScene();
        simulationTitle = new Label("SGCS Simulation");
        simulationTitle.setFont(new Font("Arial", 48));
        simulationParametersLabel = new Label("Simulation parameters:");
        simulationParametersLabel.setFont(new Font("Arial", 36));
        simulationParameters = new Label("No simulation parameters entered.");
        simulationParameters.setFont(new Font("Arial", 24));
        pausePlayButton = new Button("Pause");
        pausePlayButton.setFont(new Font("Arial", 36));
        pausePlayButton.setDefaultButton(true);
        pausePlayButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (loop.isPlaying()) {
                    loop.pause();
                    pausePlayButton.setText("Play");
                } else {
                    loop.play();
                    pausePlayButton.setText("Pause");
                }
            }
        });
    }
    private void startSimulation() {
        this.isHome = false;
        if (!countField.getText().matches("^([1-9][0-9]{0,2}|1000)$")) {
            countField.setText("1");
        }
        if (!commRangeField.getText().matches("^([1-9][0-9]?|100)$")) {
            commRangeField.setText("1");
        }
        if (!failureChanceField.getText().matches("^(0[,.][0-9]{0,15})|1$")) {
            failureChanceField.setText("0");
        }
        this.sim.setParameters(Integer.parseInt(countField.getText()), Integer.parseInt(commRangeField.getText()), Double.parseDouble(failureChanceField.getText()));
        loop.start();
        loop.play();
        setScene();
    }
    private void setScene() {
        this.getChildren().clear();
        if (this.isHome) {
            this.getChildren().addAll(parameterTitle, countLabel, countField, commRangeLabel, commRangeField, failureChanceLabel, failureChanceField, startButton);
        } else {
            simulationParameters.setText(String.format("Robot count: %d\nCommunication range: %d\nFailure chance: %f", sim.getCount(), sim.getCommRange(), sim.getFailureChance()));
            this.getChildren().addAll(simulationTitle, simulationParametersLabel, simulationParameters, pausePlayButton);
        }
    }
}
