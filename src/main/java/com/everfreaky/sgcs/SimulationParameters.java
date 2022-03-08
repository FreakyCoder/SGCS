package com.everfreaky.sgcs;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SimulationParameters extends VBox {
    private final Label parameterTitle;
    private final Button startButton;
    private final Label countLabel;
    private final TextField countField;
    private final Label commRangeLabel;
    private final TextField commRangeField;
    private boolean isHome = true;
    public SimulationParameters(double x, double y, double width, double height) {
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
    }
    private void startSimulation() {
        this.isHome = false;
        if (!countField.getText().matches("^([1-9][0-9]{0,2}|1000)$")) {
            countField.setText("1");
        }
        if (!commRangeField.getText().matches("^([1-9][0-9]?|100)$")) {
            commRangeField.setText("1");
        }
        setScene();
    }
    private void setScene() {
        this.getChildren().clear();
        if (this.isHome) {
            this.getChildren().addAll(parameterTitle, countLabel, countField, commRangeLabel, commRangeField, startButton);
        }
    }
}
