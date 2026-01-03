package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
public class ImuPractice extends OpMode {

    Imu bench = new Imu();

    @Override
    public void init() {
        bench.init(hardwareMap);

    }

    @Override
    public void loop() {
        telemetry.addData("Heading", bench.getHeading(AngleUnit.DEGREES));
        telemetry.update();
    }
}