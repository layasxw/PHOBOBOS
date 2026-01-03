package org.firstinspires.ftc.teamcode.PHOBOS;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;



@TeleOp(name = "MecanumDrive")
public class TeleOpOld extends OpMode {

    Servo servoRamp,servoRamp2,servoShooter;



    DcMotor frontLeft, frontRight, backLeft, backRight, intake, shooterLeft, shooterRight;


    boolean shooterOn = false;
    boolean lastX = false;

    boolean shooterServoOn = false;
    boolean lastDown = false;

    boolean reg = false;
    boolean lastB = false;

    @Override
    public void init() {
        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        backRight  = hardwareMap.get(DcMotor.class, "backRight");
        shooterLeft  = hardwareMap.get(DcMotor.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotor.class, "shooterRight");

        servoShooter = hardwareMap.get(Servo.class, "servoShooter");  // имя из конфигурации


        /*servoRamp = hardwareMap.get(Servo.class, "servoRamp");  // имя из конфигурации
        servoRamp.setPosition(1.0);

        servoRamp2 = hardwareMap.get(Servo.class, "servoRamp2");  // имя из конфигурации
        servoRamp2.setPosition(0.0);*/


        shooterRight.setDirection(DcMotor.Direction.REVERSE);
        shooterLeft.setDirection(DcMotor.Direction.FORWARD);
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

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
    }
    @Override
    public void loop() {
        // Left stick: move & strafe, right stick X: rotate
        double y  = gamepad1.left_stick_y;          // forward/back
        double x  = -gamepad1.left_stick_x * 1.1;     // strafe
        double rx = -gamepad1.right_stick_x;          // rotation
        boolean xPressed = gamepad1.x;
        boolean downPressed = gamepad1.a;
        boolean bPressed = gamepad1.b;
        double frontLeftPower  = y + x + rx;
        double backLeftPower   = y - x + rx;
        double frontRightPower = y - x - rx;
        double backRightPower  = y + x - rx;
// Normalize, so nothing exceeds |1|
        double max = Math.max(1.0,
                Math.max(Math.abs(frontLeftPower),
                        Math.max(Math.abs(backLeftPower),
                                Math.max(Math.abs(frontRightPower),
                                        Math.abs(backRightPower)))));
        frontLeft.setPower((frontLeftPower * frontLeftPower * frontLeftPower) / max);
        backLeft.setPower((backLeftPower * backLeftPower * backLeftPower) / max);
        frontRight.setPower((frontRightPower * frontRightPower * frontRightPower) / max);
        backRight.setPower(Math.pow(backRightPower, 3) / max);

        telemetry.addData("FL", frontLeft.getPower());
        telemetry.addData("FR", frontRight.getPower());
        telemetry.addData("BL", backLeft.getPower());
        telemetry.addData("BR", backRight.getPower());


        if (gamepad1.right_bumper) {
            intake.setPower(1.0);      // R1 → вперёд
        } else if (gamepad1.right_trigger > 0.3) {
            intake.setPower(-1.0);     // R2 → назад
        } else {
            intake.setPower(0);        // ничего не нажато → стоп
        }

        if (xPressed && !lastX) {
            shooterOn = !shooterOn;
        }

        lastX = xPressed;

        if (shooterOn) {
            shooterLeft.setPower(0.7);
            shooterRight.setPower(0.7);
        } else {
            shooterLeft.setPower(0);
            shooterRight.setPower(0.0);
        }

        if (bPressed && !lastB) {
            reg = !reg;
        }

        lastB = bPressed;

        if (reg) {
            servoShooter.setPosition(1.0);   // крутится вперёд
        } else {
            servoShooter.setPosition(0.4);     // стоп
        }

        if (downPressed && !lastDown) {
            shooterServoOn = !shooterServoOn;
        }

        lastDown = downPressed;

        if (shooterServoOn) {
            servoRamp.setPosition(0.5);
            servoRamp2.setPosition(0.5);
        } else {
            servoRamp.setPosition(1.0);
            servoRamp2.setPosition(0.0);
        }


        telemetry.addData("Right Trigger", gamepad1.right_trigger);
        telemetry.addData("Right Bumper", gamepad1.right_bumper);
        telemetry.addData("Intake Power", intake.getPower());

        telemetry.addData("ServoRamp pos", servoRamp.getPosition());
        telemetry.addData("ServoRamp2 pos", servoRamp2.getPosition());

        double shooterServoPower = reg ? 1.0 : 0.0;

        telemetry.addData("Shooter Servo", "power=%.2f  state=%s",
                shooterServoPower,
                shooterServoPower == 0 ? "STOP" : (shooterServoPower > 0 ? "FORWARD" : "REVERSE")
        );


        telemetry.update();

    }
}