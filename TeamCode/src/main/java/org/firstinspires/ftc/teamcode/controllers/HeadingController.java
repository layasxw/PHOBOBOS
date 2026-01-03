package org.firstinspires.ftc.teamcode.controllers;

import com.qualcomm.robotcore.hardware.DcMotor;

public class HeadingController {

    private DawgPIDFController pid;

    public HeadingController() {
        pid = new DawgPIDFController(
                0.02, // kP
                0.0,  // kI
                0.002,// kD
                0.0   // kF
        );
    }

    public double update(double currentHeading, double targetHeading) {

        double error = angleWrap(targetHeading - currentHeading);

        // используем PID, но передаём УЖЕ нормализованную ошибку
        double power = pid.calculate(0, error);

        return power;
    }

    public boolean atTarget(double current, double target) {
        return Math.abs(angleWrap(target - current)) < 1.0; // 1 градус
    }

    private double angleWrap(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }
}

/*
в опмоде
HeadingController turn = new HeadingController();

double heading = getHeading();
double turnPower = turn.update(heading, 90);

drive.setTurnPower(turnPower);*/
