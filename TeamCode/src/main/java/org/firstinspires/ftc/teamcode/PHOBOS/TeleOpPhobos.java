package org.firstinspires.ftc.teamcode.PHOBOS;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "TeleOp")
public class TeleOpPhobos extends OpMode {

    // ================= DRIVE =================
    DcMotor frontLeft, frontRight, backLeft, backRight;

    // ================= MECHANISMS =================
    DcMotor intake;
    DcMotor shooterLeft, shooterRight;
    DcMotor transferMotor;

    Servo servoLeft;   // верхнее серво шутера
    Servo servoRight; // нижнее серво шутера
    Servo axonOne;          // аксоны турели
    Servo axonTwo;

    IMU imu;

    // ================= STATES =================
    boolean shooterOn = false;
    boolean shooterAngleUp = false;

    boolean lastX = false;
    boolean lastB = false;

    // ====== Настройки серво шутера ======
    final double SHOOTER_DOWN = 0.0;
    final double SHOOTER_UP   = 1.0;

    @Override
    public void init() {

        // ================= DRIVE =================
        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        backRight  = hardwareMap.get(DcMotor.class, "backRight");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // ================= MECHANISMS =================
        shooterLeft  = hardwareMap.get(DcMotor.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotor.class, "shooterRight");

        shooterRight.setDirection(DcMotor.Direction.REVERSE);
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        transferMotor = hardwareMap.get(DcMotor.class, "transfer");
        transferMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        servoRight   = hardwareMap.get(Servo.class, "servoRight");
        servoLeft = hardwareMap.get(Servo.class, "servoLeft");

        axonOne = hardwareMap.get(Servo.class, "axon1");
        axonTwo = hardwareMap.get(Servo.class, "axon2");

        // стартовая позиция шутера
        setShooterAngle(SHOOTER_DOWN);

        // стартовая позиция турели
        axonOne.setPosition(0.5);
        axonTwo.setPosition(0.5);

        // ================= IMU =================
        imu = hardwareMap.get(IMU.class, "imu");
    }

    @Override
    public void loop() {

        // ========= FIELD-CENTRIC DRIVE =========
        double y  = -gamepad1.left_stick_y;
        double x  = gamepad1.left_stick_x * 1.1;
        double rx = gamepad1.right_stick_x;

        double robotAngle = imu.getRobotYawPitchRollAngles().getYaw() * Math.PI / 180.0; // в радианы

        double temp = y * Math.cos(robotAngle) + x * Math.sin(robotAngle);
        x = -y * Math.sin(robotAngle) + x * Math.cos(robotAngle);
        y = temp;

        double fl = y + x + rx;
        double bl = y - x + rx;
        double fr = y - x - rx;
        double br = y + x - rx;

        double max = Math.max(1.0, Math.max(Math.abs(fl),
                Math.max(Math.abs(bl), Math.max(Math.abs(fr), Math.abs(br)))));

        frontLeft.setPower(fl / max);
        backLeft.setPower(bl / max);
        frontRight.setPower(fr / max);
        backRight.setPower(br / max);

        // ========= INTAKE =========
        if (gamepad1.right_bumper) { // R1 → вперед
            intake.setPower(1.0);
        } else if (gamepad1.right_trigger > 0.3) { // R2 → назад
            intake.setPower(-1.0);
        } else {
            intake.setPower(0.0);
        }

        // ========= SHOOTER MOTORS (X toggle) =========
        boolean xPressed = gamepad1.x;
        if (xPressed && !lastX) shooterOn = !shooterOn;
        lastX = xPressed;

        shooterLeft.setPower(shooterOn ? 0.7 : 0.0);
        shooterRight.setPower(shooterOn ? 0.7 : 0.0);

        // ========= TRANSFER MOTOR =========
        if (gamepad1.left_bumper) { // L1 → назад
            transferMotor.setPower(-1.0);
        } else if (gamepad1.right_trigger > 0.3) { // R2 → вперед
            transferMotor.setPower(1.0);
        } else {
            transferMotor.setPower(0.0);
        }

        // ========= SHOOTER SERVO ANGLE (B toggle) =========
        boolean bPressed = gamepad1.b;
        if (bPressed && !lastB) shooterAngleUp = !shooterAngleUp;
        lastB = bPressed;

        if (shooterAngleUp) {
            setShooterAngle(SHOOTER_UP);
        } else {
            setShooterAngle(SHOOTER_DOWN);
        }

        // ========= TURRET AXONS =========
        double turretInput = gamepad1.right_stick_x; // джойстик горизонтально
        double turretPos = 0.5 + 0.5 * turretInput;  // преобразуем -1..1 → 0..1
        turretPos = Math.max(0.0, Math.min(1.0, turretPos));

        axonOne.setPosition(turretPos);
        axonTwo.setPosition(1.0 - turretPos); // зеркально

        // ========= TELEMETRY =========
        telemetry.addData("Shooter Power", shooterOn);
        telemetry.addData("Shooter Angle Up", shooterAngleUp);
        telemetry.addData("Intake Power", intake.getPower());
        telemetry.addData("Transfer Power", transferMotor.getPower());
        telemetry.addData("Turret Pos", turretPos);
        telemetry.update();
    }

    // ====== Методы ======
    void setShooterAngle(double angle) {
        angle = Math.max(0.0, Math.min(1.0, angle));
        servoRight.setPosition(angle);
        servoLeft.setPosition(1.0 - angle);
    }
}
