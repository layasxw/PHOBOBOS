/*
package org.firstinspires.ftc.teamcode.PHOBOS;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.controllers.DawgPIDFController;
import org.firstinspires.ftc.teamcode.vision.AprilTagCamera;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

public class AutoAimTeleOp extends OpMode {

    DawgPIDFController turretPID;
    AprilTagCamera camera;

    @Override
    public void init() {
        camera = new AprilTagCamera();
        camera.init(hardwareMap);

        // Настрой PID (подбирается эмпирически)
        turretPID = new DawgPIDFController(0.005, 0, 0, 0);
    }

    @Override
    public void loop() {
        AprilTagDetection tag = camera.getBestTag();
        if (tag == null) return;

        double errorX = tag.ftcPose.x;  // смещение по X

        double pidOutput = turretPID.calculate(errorX);

        // Двигаем оба Axon одинаково
        */
/*axonLeft.setPower(pidOutput);
        axonRight.setPower(pidOutput);*//*

    }

}

*/
