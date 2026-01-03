package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "ServoTest")
public class ServoTest extends LinearOpMode {

    Servo servoShooterUp, servoShooterDown;

    // Настройки углов
    double INIT_ANGLE = 0.0;   // шутер вниз
    double SHOOT_ANGLE = 1.0;  // шутер вверх

    @Override
    public void runOpMode() throws InterruptedException {

        // Подключаем серво
        servoShooterUp = hardwareMap.get(Servo.class, "servoShooter1");
        servoShooterDown = hardwareMap.get(Servo.class, "servoShooter2");

        // Стартовая позиция шутера
        setShooterAngle(INIT_ANGLE);

        telemetry.addLine("READY");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        // Поднимаем шутер для выстрела
        setShooterAngle(SHOOT_ANGLE);
        sleep(600);

        // Сделать выстрел (можно заменить на отдельное действие, если есть “открывающий” серво)
        sleep(400);

        // Возврат в стартовую позицию
        setShooterAngle(INIT_ANGLE);
    }

    // ====== Методы ======
    void setShooterAngle(double angle) {
        angle = Math.max(0.0, Math.min(1.0, angle));
        servoShooterUp.setPosition(angle);       // верхнее серво
        servoShooterDown.setPosition(1.0 - angle); // нижнее серво зеркально
    }
}
