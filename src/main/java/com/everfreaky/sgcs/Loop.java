package com.everfreaky.sgcs;

import javafx.animation.AnimationTimer;

// inherits from the JavaFX AnimationTimer
public class Loop extends AnimationTimer {
    // time of previous iteration in nanoseconds
    private long prevTime;
    // is the simulation playing
    private boolean playing;
    // singleton
    private static final Loop instance =  new Loop();
    // prevent initialization
    private Loop() {
        this.playing = false;
    }
    // get the singleton instance
    public static Loop getInstance() {
        return instance;
    }
    // start the loop
    public void start() {
        prevTime = System.nanoTime();
        super.start();
    }
    // loop implementation
    public void handle(long currTime) {
        Simulation sim = Simulation.getInstance();
        if (playing) {
            // call the simulation update with the time passed since the last iteration
            sim.update(currTime - prevTime);
            // draw on the canvas
            sim.draw();
        }
        // record time of the current iteration to be used in the next
        prevTime = currTime;
    }
    // playing property setters and getter
    public void play() {
        playing = true;
    }
    public void pause() {
        playing = false;
    }
    public boolean isPlaying() {
        return playing;
    }
}
