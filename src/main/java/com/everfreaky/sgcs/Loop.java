package com.everfreaky.sgcs;

import javafx.animation.AnimationTimer;

public class Loop extends AnimationTimer {
    private long prevTime;
    private final Simulation sim;
    private boolean playing;
    public Loop(Simulation sim) {
        this.sim = sim;
        this.playing = false;
    }
    public void start() {
        prevTime = System.nanoTime();
        super.start();
    }
    public void handle(long currTime) {
        if (playing) {
            sim.update(currTime - prevTime);
            sim.draw();
        }
        prevTime = currTime;
    }
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
