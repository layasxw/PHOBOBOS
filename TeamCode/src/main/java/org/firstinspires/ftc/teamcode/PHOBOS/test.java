package org.firstinspires.ftc.teamcode.PHOBOS;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class test extends LinearOpMode {
    DcMotor motor;

    @Override
    public void runOpMode() throws InterruptedException {
        motor = hardwareMap.get(DcMotor.class, "motor");
        waitForStart();
        while(opModeIsActive())
            motor.setPower(1.0);
    }





}
