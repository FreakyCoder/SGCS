package com.everfreaky.sgcs;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Bot {
    // bot position
    private double x;
    private double y;
    private static final double radius = 10;
    // home coordinates
    private final double hx;
    private final double hy;
    // the angle at which the bot is currently moving
    private double currAng;
    // movement speed
    private final double speed;
    // battery discharge rate
    private final double batteryDischargeRate;
    // remaining battery
    private double battery = 100;
    // number of directions to consider
    private final int consideredPositions;
    // pheromone map
    private final Set<Pheromone> pheromones;
    private int ticks;
    // should the trail be visible
    private boolean trailVisibility;
    // is the robot currently returning to recharge
    private boolean returning;
    // random generator
    private static final Random rand = new Random(System.nanoTime());
    // battery percentage font
    private static final Font font = Font.loadFont(Bot.class.getResourceAsStream("/fonts/font.ttf"), 18);
    public Bot(double x, double y, double hx, double hy, double speed, double batteryDischargeRate, int consideredPositions) {
        // set parameters
        this.x = x;
        this.y = y;
        this.hx = hx;
        this.hy = hy;
        this.currAng = 0;
        this.speed = speed;
        this.batteryDischargeRate = batteryDischargeRate;
        this.consideredPositions = consideredPositions;
        this.trailVisibility = false;
        this.returning = false;
        this.ticks = 0;
        // initialize pheromone map
        pheromones = new HashSet<>();
    }
    // Find the distance between two points
    private double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
    // update the position and battery
    public void update(long timeStep) {
        Simulation sim = Simulation.getInstance();
        // update all pheromones
        for (Pheromone p : pheromones) {
            p.update(timeStep);
        }
        // delete pheromone if expired
        pheromones.removeIf(p -> p.getStrength() <= 0);
        // decide the movement direction
        if (ticks % 120 == 0) {
            if (!returning) {
                double bestDesirability = Double.NaN, bestAng = 0, desirability, ang;
                // possible future positions
                for (int i = 0; i < consideredPositions; ++i) {
                    // generate random angle for direction
                    ang = 360 * rand.nextDouble();
                    // estimate coordinates of the future positions from the angle
                    double fx = x + 120 * timeStep * speed * Math.cos(Math.toRadians(ang));
                    double fy = y + 120 * timeStep * speed * Math.sin(Math.toRadians(ang));
                    // ensure that the robot has around 10% battery when it returns home
                    if ((dist(x, y, fx, fy) + dist(fx, fy, hx, hy)) / speed * batteryDischargeRate / 1000 < battery - 10) {
                        desirability = 0;
                        // calculate the distance to each pheromone and add it to the desirability
                        // the farther the positions is from all pheromones, the more desirable it is
                        for (Pheromone p : pheromones) {
                            double d = dist(fx, fy, p.getX(), p.getY());
                            desirability += p.getStrength() * d;
                        }
                        // find the best desirability and angle, only if the current positions allows for the return
                        // of the robot, considering its battery level and speed
                        if (Double.isNaN(bestDesirability) || bestDesirability < desirability) {
                            bestDesirability = desirability;
                            bestAng = ang;
                        }
                    }
                }
                // if no future position is possible, return to home
                if (Double.isNaN(bestDesirability)) {
                    currAng = Math.toDegrees(Math.atan2(hy - y, hx - x));
                    returning = true;
                } else {
                    // go to the most desired future position
                    currAng = bestAng;
                }
            }
        }
        if (returning && dist(x, y, hx, hy) <= timeStep * speed) {
            // recharge
            returning = false;
            battery = 100;
            x = hx;
            y = hy;
            return;
        }
        if (ticks % 60 == 0) {
            // drop new pheromone every 60 ticks
            pheromones.add(new Pheromone(x, y, sim.getPheromoneDecayRate() / 100));
        }
        ticks = (ticks + 1) % 120;
        // move the bot in the chosen direction
        x += timeStep * speed * Math.cos(Math.toRadians(currAng));
        y += timeStep * speed * Math.sin(Math.toRadians(currAng));
        // discharge the battery
        battery -= batteryDischargeRate * timeStep / 1000;
    }
    // setters
    public void setTrailVisibility(boolean value) {
        trailVisibility = value;
    }
    // getters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getBattery() { return battery; }
    public Set<Pheromone> getPheromones() { return pheromones; }
    // add pheromones to the pheromone map
    public void updatePheromones(Set<Pheromone> newPheromones) {
        pheromones.addAll(newPheromones.stream().map(Pheromone::copy).collect(Collectors.toSet()));
    }
    // draw the robot
    public void draw(GraphicsContext ctx) {
        ctx.setFill(Color.BLACK);
        ctx.setGlobalAlpha(1);
        ctx.fillOval(x, y, radius, radius);
        // show remaining battery above the robot
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setFont(font);
        ctx.fillText(String.format("%.2f%%", battery), x + radius / 2, y - radius / 2);
        if (trailVisibility) {
            for (Pheromone p : pheromones) {
                p.draw(ctx);
            }
        }
    }
}
