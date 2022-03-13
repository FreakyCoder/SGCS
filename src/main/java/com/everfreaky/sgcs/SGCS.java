package com.everfreaky.sgcs;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class SGCS extends Application {
    @Override
    public void start(final Stage stage) throws IOException {
        final Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        Pane root = new Pane();
        Simulation simulation = new Simulation(screenBounds.getWidth() / 2, screenBounds.getHeight());
        Loop loop = new Loop(simulation);
        SimulationControl parameters = new SimulationControl(screenBounds.getWidth() / 2, 0, screenBounds.getWidth() / 2, screenBounds.getHeight(), simulation, loop);
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        root.getChildren().add(simulation.getCanvas());
        root.getChildren().add(parameters);
        stage.setTitle("SGCS");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
        //col2.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public static void main(String[] args) {
        launch();
    }
}