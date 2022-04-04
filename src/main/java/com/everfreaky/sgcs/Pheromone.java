package com.everfreaky.sgcs;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Objects;

public class Pheromone {
    // pheromone position
    private final double x;
    private final double y;
    public Pheromone(double x, double y) {
        this.x = x;
        this.y = y;
    }
    // getters
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    // draw the pheromone
    public void draw(GraphicsContext ctx) {
        ctx.setFill(Color.GREENYELLOW);
        ctx.fillOval(x, y, 5, 5);
    }
    // allow the pheromone to be copied when two robots communicate
    public Pheromone copy() {
        return new Pheromone(x, y);
    }
    // override equals method to prevent duplicates in set
    @Override
    public boolean equals(Object o) {
        // an object is always equal to itself
        if (this == o) { return true; }
        // if the compared object is null or isn't of the same class, the two objects do not equal each other
        if (o == null || getClass() != o.getClass()) { return false; }
        // cast to pheromone
        Pheromone pheromone = (Pheromone) o;
        // compare the positions
        return Double.compare(pheromone.getX(), getX()) == 0 && Double.compare(pheromone.getY(), getY()) == 0;
    }

    // to be used in hash set
    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
