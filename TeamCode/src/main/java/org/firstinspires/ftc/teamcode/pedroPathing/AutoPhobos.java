package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;



@com.qualcomm.robotcore.eventloop.opmode.Autonomous
public class AutoPhobos extends LinearOpMode {

    public void goTo(Pose startPose, Pose target, Follower follower) {
        PathChain chain = follower.pathBuilder()
                .addPath(new BezierCurve(startPose, target))
                .build();

        follower.followPath(chain);

        while (opModeIsActive() && follower.isBusy()) {
            follower.update();
        }
    }

    public void goToIntake(Pose startPose, Pose target, Follower follower, DcMotor intake) {

        PathChain chain = follower.pathBuilder()
                .addPath(new BezierCurve(startPose, target))
                .build();

        follower.followPath(chain);

        while (opModeIsActive() && follower.isBusy()) {
            follower.update();
        }

    }


    public void shoot(Servo servoShooter, Servo servoRamp, DcMotor shooterLeft, DcMotor shooterRight) {
        shooterLeft.setPower(1);
        shooterRight.setPower(1);
        sleep(1300);

        for (int i = 0; i < 3; i++) {
            servoRamp.setPosition(1.0);
            servoShooter.setPosition(1);
            sleep(500);
            servoRamp.setPosition(0);
            servoShooter.setPosition(0);
            sleep(500);
        }

        shooterLeft.setPower(0);
        shooterRight.setPower(0);
    }

    DcMotor frontLeft,  frontRight, backLeft, backRight, intake;

    @Override
    public void runOpMode() throws InterruptedException {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        DcMotor shooterLeft = hardwareMap.get(DcMotor.class, "shooterLeft");
        DcMotor shooterRight = hardwareMap.get(DcMotor.class, "shooterRight");
        Servo servoShooter = hardwareMap.get(Servo.class, "servoShooter");
        Servo servoRamp = hardwareMap.get(Servo.class, "servoRamp");
        Follower follower = Constants.createFollower(hardwareMap);
        Pose START_BLUE = new Pose(60, 8, Math.toRadians(90));

        Pose SHOOT_POS = new Pose(70, 70, Math.toRadians(135)); //


        shooterRight.setDirection(DcMotor.Direction.REVERSE);
        shooterLeft.setDirection(DcMotor.Direction.FORWARD);
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        // Optional but recommended
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();


        intake.setPower(1.0);
        follower.update();
        //start blue down
        goTo(START_BLUE, SHOOT_POS, follower);
        shoot(servoShooter, servoRamp, shooterLeft, shooterRight);




        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        intake.setPower(0);
    }
}


