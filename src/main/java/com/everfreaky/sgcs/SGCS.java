package com.everfreaky.sgcs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class SGCS extends Application {
    @Override
    public void start(final Stage stage) throws IOException {
        double width = Screen.getPrimary().getBounds().getWidth();
        double height = Screen.getPrimary().getBounds().getHeight();
        Pane root = new Pane();
        Main app = new Main(width / 2, height);
        SimulationParameters parameters = new SimulationParameters(width / 2, 0, width / 2, height);
        Scene scene = new Scene(root, width, height);
        root.getChildren().add(app.getCanvas());
        root.getChildren().add(parameters);
        app.draw();
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