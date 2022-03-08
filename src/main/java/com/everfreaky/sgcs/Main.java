package com.everfreaky.sgcs;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class Main {
    private Canvas canvas;
    private GraphicsContext ctx;
    public Main(double width, double height) {
        this.canvas = new Canvas(width, height);
        canvas.setStyle("-fx-border-color: black");
        this.ctx = this.canvas.getGraphicsContext2D();
    }

    public void draw() {
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
