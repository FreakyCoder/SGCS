package com.everfreaky.sgcs;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SGCS extends Application {
    @Override
    public void start(final Stage stage) {
        // get screen dimensions
        final Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        // set stage size to screen size
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        // initialize main pane
        Pane root = new Pane();
        // get the simulation instance
        Simulation simulation = Simulation.getInstance();
        // get the simulation control instance
        SimulationControl parameters = SimulationControl.getInstance();
        // initialize scene
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        // add the simulation canvas and the control UI to the scene
        root.getChildren().add(simulation.getCanvas());
        root.getChildren().add(parameters);
        // set stage parameters
        stage.setTitle("SGCS");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        // show the UI
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}