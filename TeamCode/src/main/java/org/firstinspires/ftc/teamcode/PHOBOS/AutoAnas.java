package org.firstinspires.ftc.teamcode.PHOBOS;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "AutoBlueDown")
public class AutoAnas extends LinearOpMode {

    DcMotor frontLeft, frontRight, backLeft, backRight, shooterLeft, shooterRight, intake;
    Servo servoRamp, servoRamp2;

    @Override
    public void runOpMode() throws InterruptedException {

        // Инициализация моторов
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        shooterLeft = hardwareMap.get(DcMotor.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotor.class, "shooterRight");
        intake = hardwareMap.get(DcMotor.class, "intake");

        /*servoRamp = hardwareMap.get(Servo.class, "servoRamp");  // имя из конфигурации
        servoRamp.setPosition(1.0);

        servoRamp2 = hardwareMap.get(Servo.class, "servoRamp2");  // имя из конфигурации
        servoRamp2.setPosition(0.0);*/

        // Настройка направлений и поведения
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        shooterLeft.setDirection(DcMotor.Direction.FORWARD);
        shooterRight.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();
        if (isStopRequested()) return;

        // -------------------------------
//        // 1. Движение вперёд (пример)
//        frontRight.setPower(-0.6);
//        frontLeft.setPower(-0.6);
//        backRight.setPower(-0.6);
//        backLeft.setPower(-0.6);
//
//        sleep(300);
//        frontRight.setPower(0);
//        frontLeft.setPower(0);
//        backRight.setPower(0);
//        backLeft.setPower(0);

//        // 2. Запуск шутеров и интейка по одному мячу
//        intake.setPower(1.0);

        // 3. Поворот влево (моторы для поворота)
        frontRight.setPower(-0.5);
        frontLeft.setPower(0.5);
        backRight.setPower(0.5);
        backLeft.setPower(-0.5);
        // пример левый поворот
        sleep(1000);
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);

//        // 4. Движение прямо 2 секунды
//        frontRight.setPower(-0.6);
//        frontLeft.setPower(-0.6);
//        backRight.setPower(-0.6);
//        backLeft.setPower(-0.6);
//        sleep(2000);
//        frontRight.setPower(0);
//        frontLeft.setPower(0);
//        backRight.setPower(0);
//        backLeft.setPower(0);

//        // 5. Включение интейка на 2 секунды
//        intake.setPower(1.0);
//        sleep(2000);
//        intake.setPower(0);

        //        // 6. Движение назад 2 секунды
//        frontRight.setPower(0.6);
//        frontLeft.setPower(0.6);
//        backRight.setPower(0.6);
//        backLeft.setPower(0.6);
//        sleep(2000);
//        frontRight.setPower(0);
//        frontLeft.setPower(0);
//        backRight.setPower(0);
//        backLeft.setPower(0);

//        // 7. Поворот вправо
//        frontRight.setPower(0.5);
//        frontLeft.setPower(-0.5);
//        backRight.setPower(0.5);
//        backLeft.setPower(-0.5); // пример правый поворот
//        sleep(500);
//        frontRight.setPower(0);
//        frontLeft.setPower(0);
//        backRight.setPower(0);
//        backLeft.setPower(0);

        // 8. Шут 3 мяча
//        shoot3Times();
    }

    // -------------------------------



}
