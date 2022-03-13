package com.everfreaky.sgcs;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Bot {
    private double x;
    private double y;
    private final double hx;
    private final double hy;
    private final double speed;
    private final Set<Pheromone> pheromones;
    private int ticks;
    private final Random rand;
    public Bot(double x, double y, double hx, double hy, double speed) {
        this.x = x;
        this.y = y;
        this.hx = hx;
        this.hy = hy;
        this.speed = speed;
        this.ticks = 0;
        pheromones = new HashSet<>();
        this.rand = new Random(System.nanoTime());
    }
    private double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
    public void move(long timeStep) {
        double bdesirabiilty = Double.NaN, bang = 0, desirability, ang;
        for (int i = 0; i < 5; ++ i) {
            ang = 360 * rand.nextDouble();
            double fx = x + timeStep * speed * Math.cos(Math.toRadians(ang));
            double fy = y + timeStep * speed * Math.sin(Math.toRadians(ang));
            desirability = 0;
            for (Pheromone p : pheromones) {
                double d = dist(fx, fy, p.getX(), p.getY());
                desirability += d;
            }
            desirability -= dist(fx, fy, hx, hy) * dist(fx, fy, hx, hy);
            if (Double.isNaN(bdesirabiilty) || bdesirabiilty < desirability) {
                bdesirabiilty = desirability;
                bang = ang;
            }
        }
        if (ticks == 59) {
            pheromones.add(new Pheromone(x, y));
        }
        ticks = (ticks + 1) % 60;
        x += timeStep * speed * Math.cos(Math.toRadians(bang));
        y += timeStep * speed * Math.sin(Math.toRadians(bang));
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public Set<Pheromone> getPheromones() {
        return pheromones;
    }
    public void updatePheromones(Set<Pheromone> newPheromones) {
        pheromones.addAll(newPheromones.stream().map(Pheromone::copy).collect(Collectors.toSet()));
    }
    public void draw(GraphicsContext ctx) {
        ctx.setFill(Color.BLACK);
        ctx.fillOval(x, y, 10, 10);
    }
}
