package com.everfreaky.sgcs;

import javafx.animation.AnimationTimer;

public class Loop extends AnimationTimer {
    private long prevTime;
    private boolean playing;
    private static final Loop instance =  new Loop();
    private Loop() {
        this.playing = false;
    }
    public static Loop getInstance() {
        return instance;
    }
    public void start() {
        prevTime = System.nanoTime();
        super.start();
    }
    public void handle(long currTime) {
        Simulation sim = Simulation.getInstance();
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
