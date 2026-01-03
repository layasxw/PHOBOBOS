package org.firstinspires.ftc.teamcode.controllers;


public class DawgPIDFController {
    private double kP, kI, kD, kF;
    private double setPoint;
    private double measuredValue;
    private double totalError = 0;
    private double prevError = 0;
    private double lastTime = 0;

    private DawgTimer timer = new DawgTimer();

    public DawgPIDFController(double kp, double ki, double kd, double kf) {
        this(kp, ki, kd, kf, 0, 0);
    }

    public DawgPIDFController(double kp, double ki, double kd, double kf,
                              double setPoint, double measuredValue) {
        this.kP = kp;
        this.kI = ki;
        this.kD = kd;
        this.kF = kf;
        this.setPoint = setPoint;
        this.measuredValue = measuredValue;
        reset();
    }

    public void reset() {
        totalError = 0;
        prevError = 0;
        lastTime = 0;
        timer.reset();
    }


    public void setPIDF(double kp, double ki, double kd, double kf) {
        this.kP = kp;
        this.kI = ki;
        this.kD = kd;
        this.kF = kf;
    }

    public void setSetPoint(double setPoint) {
        this.setPoint = setPoint;
    }

    public double calculate(double currentValue) {
        return calculate(currentValue, setPoint);
    }

    public double calculate(double currentValue, double targetValue) {
        // 1. Обновляем время
        double currentTime = timer.seconds();
        double deltaTime = lastTime == 0 ? 0 : currentTime - lastTime;
        lastTime = currentTime;

        // 2. Вычисляем ошибку
        double error = targetValue - currentValue;

        // 3. P-компонент
        double p = kP * error;

        // 4. I-компонент с защитой от переполнения
        if (deltaTime > 0) {
            totalError += error * deltaTime;
            // Простая защита - ограничиваем интеграл
            if (totalError > 1000) totalError = 1000;
            if (totalError < -1000) totalError = -1000;
        }
        double i = kI * totalError;

        // 5. D-компонент
        double d = 0;
        if (deltaTime > 0.0001) {
            d = kD * (error - prevError) / deltaTime;
        }
        prevError = error;

        // 6. F-компонент (опционально)
        double f = kF * targetValue;

        // 7. Суммируем всё
        double output = p + i + d + f;

        // 8. Ограничиваем выход (-1.0 до 1.0)
        return Math.max(-1.0, Math.min(1.0, output));
    }
}