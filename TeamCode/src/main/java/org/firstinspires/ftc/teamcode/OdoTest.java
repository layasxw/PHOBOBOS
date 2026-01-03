package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "ODO_TEST", group = "Test")
public class OdoTest extends OpMode {

    DcMotorEx leftOdo, rightOdo;

    @Override
    public void init() {
        leftOdo = hardwareMap.get(DcMotorEx.class, "leftOdo");
        rightOdo = hardwareMap.get(DcMotorEx.class, "rightOdo");

        leftOdo.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        rightOdo.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        leftOdo.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        rightOdo.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        telemetry.addData("Left Odo", leftOdo.getCurrentPosition());
        telemetry.addData("Right Odo", rightOdo.getCurrentPosition());
        telemetry.update();
    }
}