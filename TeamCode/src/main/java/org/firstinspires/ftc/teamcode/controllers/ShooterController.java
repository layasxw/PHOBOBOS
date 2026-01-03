/*
package org.firstinspires.ftc.teamcode.controllers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

*/
/**
 * ShooterController
 *
 * Управляет:
 *  - скоростью шутера (через PID по RPM)
 *  - углом вылета (через серво)
 *
 * Вход: distance до цели
 * Выход: motor power + servo position
 *//*



public class ShooterController {

    */
/* ===================== HARDWARE ===================== *//*


    private final DcMotor shooter1, shooter2;
    private final Servo angleServo;

    */
/* ===================== PID ===================== *//*


    private final DawgPIDFController pid;

    // ⚠️ ТИКИ НА 1 ОБОРОТ МОТОРА (НЕ КОЛЕСА)
    private static final double TICKS_PER_REV = 28.0;

    // максимальные обороты мотора (примерно, подстрой)
    private static final double MAX_RPM = 5000.0;

    */
/* ===================== STATE ===================== *//*


    private double lastEncoder = 0.0;
    private double lastTime = 0.0;

    private double currentRPM = 0.0;
    private double targetRPM = 0.0;

    */
/* ===================== PRESET TABLE ===================== *//*


    private static class ShooterPreset {
        double distance;   // метры
        double rpm;        // целевые обороты шутера
        double servoPos;   // позиция серво угла

        ShooterPreset(double distance, double rpm, double servoPos) {
            this.distance = distance;
            this.rpm = rpm;
            this.servoPos = servoPos;
        }
    }

    // потюнить
    private final ShooterPreset[] presets = {
            new ShooterPreset(1.0, 2800, 0.72),
            new ShooterPreset(1.5, 3200, 0.68),
            new ShooterPreset(2.0, 3600, 0.63),
            new ShooterPreset(2.5, 4000, 0.58)
    };

    */
/* ===================== CONSTRUCTOR ===================== *//*


    public ShooterController(DcMotor shooter1, DcMotor shooter2, Servo angleServo) {
        this.shooter1 = shooter1;
        this.shooter2 = shooter2;
        this.angleServo = angleServo;

        pid = new DawgPIDFController(
                0.002,           // kP
                0.0,             // kI
                0.0,             // kD
                1.0 / MAX_RPM    // kF
        );

        shooter1.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        shooter2.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    */
/* ===================== PUBLIC API ===================== *//*


    */
/*

    public void updateFromDistance(double distance) {

        ShooterPreset preset = getInterpolatedPreset(distance);

        // выставляем угол
        angleServo.setPosition(preset.servoPos);

        //обновляем targetRPM
        targetRPM = preset.rpm;

        //обновляем PID
        updatePID();
    }

/*

    public void stop() {
        shooter1.setPower(0.0);
        shooter2.setPower(0.0);
        targetRPM = 0.0;
        currentRPM = 0.0;
        lastTime = 0.0;
    }

    */
/*

    public boolean atSpeed(double toleranceRPM) {
        return Math.abs(targetRPM - currentRPM) < toleranceRPM;
    }

    public boolean atSpeed() {
        return atSpeed(50); // дефолт ±50 RPM
    }

    public double getCurrentRPM() {
        return currentRPM;
    }

    public double getTargetRPM() {
        return targetRPM;
    }

    */
/* ===================== INTERNAL ===================== *//*


    private void updatePID() {
        double time = System.nanoTime() / 1e9;
        double dt = (lastTime == 0.0) ? 0.0 : (time - lastTime);
        lastTime = time;

        double encoder = shooter1.getCurrentPosition();
        double delta = encoder - lastEncoder;
        lastEncoder = encoder;

        if (dt > 0.0) {
            currentRPM = (delta / TICKS_PER_REV) / dt * 60.0;
        }

        double power = pid.calculate(currentRPM, targetRPM);
        shooterMotor.setPower(clamp(power, 0.0, 1.0));
    }

    private ShooterPreset getInterpolatedPreset(double distance) {

        // ниже минимального
        if (distance <= presets[0].distance) {
            return presets[0];
        }

        // выше максимального
        if (distance >= presets[presets.length - 1].distance) {
            return presets[presets.length - 1];
        }

        // интерполяция между точками
        for (int i = 0; i < presets.length - 1; i++) {
            ShooterPreset a = presets[i];
            ShooterPreset b = presets[i + 1];

            if (distance >= a.distance && distance <= b.distance) {
                double t = (distance - a.distance) / (b.distance - a.distance);

                double rpm = lerp(a.rpm, b.rpm, t);
                double servo = lerp(a.servoPos, b.servoPos, t);

                return new ShooterPreset(distance, rpm, servo);
            }
        }

        // fallback (не должен случаться)
        return presets[0];
    }

    private double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}

*/
/*ShooterController shooter;

@Override
public void init() {
    DcMotorEx shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter");
    Servo angleServo = hardwareMap.get(Servo.class, "angleServo");

    shooter = new ShooterController(shooterMotor, angleServo);
}

@Override
public void loop() {

    if (gamepad1.right_trigger > 0.5) {
        double distance = getDistanceToTarget(); // AprilTag / odometry
        shooter.updateFromDistance(distance);

        if (shooter.atSpeed()) {
            feederServo.setPosition(1.0); // подача
        }
    } else {
        shooter.stop();
    }

    telemetry.addData("RPM", shooter.getCurrentRPM());
    telemetry.addData("Target RPM", shooter.getTargetRPM());
}*/

