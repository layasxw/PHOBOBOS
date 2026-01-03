package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
@Autonomous(name="AxonTest")
public class AxonTest extends LinearOpMode {

    Servo ax1, ax2;

    public void setAxonAngle(Servo servo, double angle) {
        angle = Math.max(0, Math.min(180, angle));
        servo.setPosition(angle / 180.0);
    }

    @Override
    public void runOpMode() {

        ax1 = hardwareMap.get(Servo.class, "ax1");
        ax2 = hardwareMap.get(Servo.class, "ax2");

        ax1.setDirection(Servo.Direction.FORWARD);
        ax2.setDirection(Servo.Direction.FORWARD);

        ax1.setPosition(0.5);
        ax2.setPosition(0.5);

        telemetry.addLine("Init done");
        telemetry.update();

        waitForStart();
        if (!opModeIsActive()) return;
        while (opModeIsActive()) {
            telemetry.addData("ax1 pos (0-1)", ax1.getPosition());
            telemetry.addData("ax2 pos (0-1)", ax2.getPosition());
            telemetry.update();


            telemetry.addLine("Started");
            telemetry.update();

            ax1.setPosition(1.0);
            ax2.setPosition(1.0);
            sleep(1500);

            ax1.setPosition(0);
            ax2.setPosition(0);
            sleep(1500);

            telemetry.addLine("Auto Complete");
            telemetry.update();

        }

    }
}
