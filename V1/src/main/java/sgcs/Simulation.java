package sgcs;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Screen;

import java.util.Random;

public class Simulation {
    // canvas
    private final Canvas canvas;
    // drawing context
    private final GraphicsContext ctx;
    // robot count
    private int count;
    // robot communication range
    private int commRange;
    // robot chance to fail every second
    private double failureChance;
    // robot speed
    private double speed;
    // the rate at which a robot's battery discharges
    private double batteryDischargeRate;
    // considered future positions by each robot
    private int consideredPositions;
    // pheromone decay rate
    private double pheromoneDecayRate;
    // array of the robots
    private Bot[] bots;
    // random generator
    private final Random rand;
    // time since last failure check in milliseconds
    private long loopTime;
    // singleton instance
    private static final Simulation instance = new Simulation(Screen.getPrimary().getBounds().getWidth() / 2, Screen.getPrimary().getBounds().getHeight());
    // prevent initialization
    private Simulation(double width, double height) {
        this.canvas = new Canvas(width, height);
        canvas.setStyle("-fx-border-color: black");
        this.ctx = this.canvas.getGraphicsContext2D();
        this.loopTime = 0;
        this.rand = new Random(System.nanoTime());
    }
    // get singleton instance
    public static Simulation getInstance() {
        return instance;
    }

    // set the simulation parameters
    public void setParameters(int count, int commRange, double failureChance, double speed, double batteryDischargeRate, int consideredPositions, double pheromoneDecayRate) {
        // set parameters
        this.count = count;
        this.commRange = commRange;
        // convert from percentage
        this.failureChance = failureChance / 100;
        this.speed = speed;
        this.batteryDischargeRate = batteryDischargeRate;
        this.consideredPositions = consideredPositions;
        this.pheromoneDecayRate = pheromoneDecayRate;
        // initialize bot array
        bots = new Bot[count];
        // create new bots in a circle
        for (int i = 0; i < count; ++ i) {
            bots[i] = new Bot(canvas.getWidth() / 2 + count * Math.cos(Math.toRadians(i * (360.0 / count))), canvas.getHeight() / 2 + count * Math.sin(Math.toRadians(i * (360.0 / count))), canvas.getWidth() / 2, canvas.getHeight() / 2, speed * 0.1, batteryDischargeRate, consideredPositions);
        }
    }
    // toggle the visibility of robot's pheromones
    public void setTrailVisibility(int i, boolean value) {
        if (bots[i] != null) { bots[i].setTrailVisibility(value); }
    }
    // getters
    public int getCount() { return count; }
    public int getCommRange() { return commRange; }
    public double getFailureChance() { return failureChance * 100; }
    public double getSpeed() { return speed; }
    public double getBatteryDischargeRate() { return batteryDischargeRate; }
    public int getConsideredPositions() { return consideredPositions; }
    public double getPheromoneDecayRate() { return pheromoneDecayRate; }
    // calculate the distance between two robots
    private double dist(Bot a, Bot b) {
        return Math.sqrt((a.getX() - b.getX()) * (a.getX() - b.getX()) + (a.getY() - b.getY()) * (a.getY() - b.getY()));
    }
    // update called by the AnimationTimer
    public void update(long timeStep) {
        // convert to milliseconds
        timeStep *= 0.000001;
        // add time to counter
        loopTime += timeStep;
        for (int i = 0; i < bots.length; ++i) {
            // if the bot is still functional
            if (bots[i] != null) {
                // every second, check for failure
                if (loopTime >= 1000) {
                    double chance = rand.nextDouble();
                    if (chance < failureChance) {
                        // destroy bot if it fails
                        bots[i] = null;
                        continue;
                    }
                }
                // check if the robot's battery is fully discharged, and destroy it
                if (bots[i].getBattery() <= 0) {
                    bots[i] = null;
                    continue;
                }
                // move the bot
                bots[i].update(timeStep);
                // check which two bots are within communication range to exchange pheromone maps
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
    // draw the simulation
    public void draw() {
        // clear the canvas
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        // draw each bot
        for (Bot bot : bots) {
            // only if still functioning
            if (bot != null) {
                bot.draw(ctx);
            }
        }
    }

    // canvas getter
    public Canvas getCanvas() {
        return canvas;
    }
}
