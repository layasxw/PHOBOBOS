package org.firstinspires.ftc.teamcode.controllers;

public class DawgPIDController {
    private DawgPIDFController pidf;

    public DawgPIDController(double kp, double ki, double kd) {
        pidf = new DawgPIDFController(kp, ki, kd, 0);
    }

    public double calculate(double current, double target) {
        return pidf.calculate(current, target);
    }

    public void setPID(double kp, double ki, double kd) {
        pidf.setPIDF(kp, ki, kd, 0);
    }

    public void reset() {
        pidf.reset();
    }
}