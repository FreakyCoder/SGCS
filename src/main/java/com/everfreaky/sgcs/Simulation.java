package com.everfreaky.sgcs;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Simulation {
    private final Canvas canvas;
    private final GraphicsContext ctx;
    private int count;
    private int commRange;
    private double failureChance;
    private Bot[] bots;
    private final Random rand;
    private long loopTime;
    public Simulation(double width, double height) {
        this.canvas = new Canvas(width, height);
        canvas.setStyle("-fx-border-color: black");
        this.ctx = this.canvas.getGraphicsContext2D();
        this.loopTime = 0;
        this.rand = new Random(System.nanoTime());
    }

    public void setParameters(int count, int commRange, double failureChance) {
        this.count = count;
        this.commRange = commRange;
        this.failureChance = failureChance;
        bots = new Bot[count];
        for (int i = 0; i < count; ++ i) {
            bots[i] = new Bot(canvas.getWidth() / 2 + count * Math.cos(Math.toRadians(i * (360.0 / count))), canvas.getHeight() / 2 + count * Math.sin(Math.toRadians(i * (360.0 / count))), canvas.getWidth() / 2, canvas.getHeight() / 2, 0.1);
        }
    }
    public int getCount() {
        return count;
    }
    public int getCommRange() {
        return commRange;
    }
    public double getFailureChance() {
        return failureChance;
    }
    private double dist(Bot a, Bot b) {
        return Math.sqrt((a.getX() - b.getX()) * (a.getX() - b.getX()) + (a.getY() - b.getY()) * (a.getY() - b.getY()));
    }
    public void update(long timeStep) {
        timeStep *= 0.000001;
        loopTime += timeStep;
        for (int i = 0; i < bots.length; ++i) {
            if (bots[i] != null) {
                if (loopTime >= 1000) {
                    double chance = rand.nextDouble();
                    if (chance < failureChance) {
                        System.out.printf("%d: %f\n", i, chance);
                        bots[i] = null;
                        continue;
                    }
                }
                bots[i].move(timeStep);
                for (int j = 0; j < i; ++j) {
                    if (bots[j] != null && dist(bots[i], bots[j]) <= commRange) {
                        bots[i].updatePheromones(bots[j].getPheromones());
                        bots[j].updatePheromones(bots[i].getPheromones());
                    }
                }
            }
        }
        loopTime %= 1000;
    }
    public void draw() {
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (Bot bot : bots) {
            if (bot != null) {
                bot.draw(ctx);
            }
        }
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
