package com.everfreaky.sgcs;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Objects;

public class Pheromone {
    private  double x;
    private double y;
    public Pheromone(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void draw(GraphicsContext ctx) {
        ctx.setFill(Color.GREENYELLOW);
        ctx.fillOval(x, y, 5, 5);
    }
    public Pheromone copy() {
        return new Pheromone(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Pheromone pheromone = (Pheromone) o;
        return Double.compare(pheromone.getX(), getX()) == 0 && Double.compare(pheromone.getY(), getY()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
