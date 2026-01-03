package org.firstinspires.ftc.teamcode.controllers;


public class DawgTimer {
    private long startTime;

    public DawgTimer() {
        reset();
    }

    public void reset() {
        startTime = System.nanoTime();
    }

    public double seconds() {
        return (System.nanoTime() - startTime) / 1_000_000_000.0;
    }

    public double milliseconds() {
        return (System.nanoTime() - startTime) / 1_000_000.0;
    }
}
