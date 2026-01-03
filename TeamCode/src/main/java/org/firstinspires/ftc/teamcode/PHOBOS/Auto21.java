package org.firstinspires.ftc.teamcode.PHOBOS;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "AUTO21")
public class Auto21 extends OpMode {

    // ================= CORE =================
    private Follower follower;
    private Timer stateTimer;

    private DcMotor intake;
    private DcMotor transferMotor;
    private DcMotor shooterLeft, shooterRight;
    private Servo servoLeft, servoRight;

    // ================= POSES =================
    private final Pose startPose = new Pose(20, 123);
    private final Pose shootPose = new Pose(60, 84);

    private final Pose intake1Start = new Pose(36, 84);
    private final Pose intake1End   = new Pose(14, 84);

    private final Pose intake2Start = new Pose(36, 60);
    private final Pose intake2End   = new Pose(14, 60);

    private final Pose intake4Start = new Pose(36, 10);
    private final Pose intake4End   = new Pose(14, 10);

    private final Pose gate = new Pose(16, 66);

    // ================= PATHS =================
    private PathChain startToShoot;
    private PathChain shootToI2, i2, i2ToShoot;
    private PathChain shootToGate;
    private PathChain shootToI4, i4, i4ToShoot;
    private PathChain shootToI1, i1, i1ToShoot;

    // ================= STATES =================
    private enum State {
        START_TO_SHOOT,
        SHOOT,

        TO_I2, I2, BACK_I2,

        TO_GATE, GATE_COLLECT,

        TO_I4_A, I4_A, BACK_I4_A,
        TO_I4_B, I4_B, BACK_I4_B,

        TO_I1, I1, BACK_I1,

        TO_I4_C, I4_C, BACK_I4_C,

        DONE
    }

    private State state;

    // ====== SERVO SHOOTER POSITIONS ======
    final double SHOOTER_DOWN = 0.0;
    final double SHOOTER_UP   = 1.0;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setPose(startPose);

        intake = hardwareMap.get(DcMotor.class, "intake");
        transferMotor = hardwareMap.get(DcMotor.class, "transfer");

        shooterLeft  = hardwareMap.get(DcMotor.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotor.class, "shooterRight");
        shooterRight.setDirection(DcMotor.Direction.REVERSE);

        servoLeft = hardwareMap.get(Servo.class, "servoLeft");
        servoRight = hardwareMap.get(Servo.class, "servoRight");

        setShooterAngle(SHOOTER_DOWN);

        intake.setPower(0);
        transferMotor.setPower(0);
        shooterLeft.setPower(0);
        shooterRight.setPower(0);

        stateTimer = new Timer();
        stateTimer.resetTimer();

        buildPaths();
        state = State.START_TO_SHOOT;
    }

    // ================= PATH BUILDER =================
    private PathChain path(Pose a, Pose b) {
        return follower.pathBuilder()
                .addPath(new BezierLine(a, b))
                .setLinearHeadingInterpolation(a.getHeading(), b.getHeading())
                .build();
    }

    private void buildPaths() {
        startToShoot = path(startPose, shootPose);

        shootToI2 = path(shootPose, intake2Start);
        i2 = path(intake2Start, intake2End);
        i2ToShoot = path(intake2End, shootPose);

        shootToGate = path(shootPose, gate);

        shootToI4 = path(shootPose, intake4Start);
        i4 = path(intake4Start, intake4End);
        i4ToShoot = path(intake4End, shootPose);

        shootToI1 = path(shootPose, intake1Start);
        i1 = path(intake1Start, intake1End);
        i1ToShoot = path(intake1End, shootPose);
    }

    @Override
    public void loop() {
        follower.update();
        updateFSM();

        telemetry.addData("STATE", state);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.update();
    }

    private void updateFSM() {
        switch (state) {

            case START_TO_SHOOT:
                follow(startToShoot, State.SHOOT);
                break;

            case SHOOT:
                shootThen(nextAfterShoot());
                break;

            case TO_I2:
                follow(shootToI2, State.I2);
                break;

            case I2:
                intakePath(i2, State.BACK_I2);
                break;

            case BACK_I2:
                follow(i2ToShoot, State.SHOOT);
                break;

            case TO_GATE:
                follow(shootToGate, State.GATE_COLLECT);
                break;

            case GATE_COLLECT:
                intake.setPower(1);
                if (stateTimer.getElapsedTimeSeconds() > 0.7) {
                    intake.setPower(0);
                    set(State.TO_I4_A);
                }
                break;

            case TO_I4_A:
                follow(shootToI4, State.I4_A);
                break;

            case I4_A:
                intakePath(i4, State.BACK_I4_A);
                break;

            case BACK_I4_A:
                follow(i4ToShoot, State.SHOOT);
                break;

            case TO_I4_B:
                follow(shootToI4, State.I4_B);
                break;

            case I4_B:
                intakePath(i4, State.BACK_I4_B);
                break;

            case BACK_I4_B:
                follow(i4ToShoot, State.SHOOT);
                break;

            case TO_I1:
                follow(shootToI1, State.I1);
                break;

            case I1:
                intakePath(i1, State.BACK_I1);
                break;

            case BACK_I1:
                follow(i1ToShoot, State.SHOOT);
                break;

            case TO_I4_C:
                follow(shootToI4, State.I4_C);
                break;

            case I4_C:
                intakePath(i4, State.BACK_I4_C);
                break;

            case BACK_I4_C:
                follow(i4ToShoot, State.DONE);
                break;

            case DONE:
                intake.setPower(0);
                transferMotor.setPower(0);
                shooterLeft.setPower(0);
                shooterRight.setPower(0);
                break;
        }
    }

    // ================= HELPERS =================
    private void follow(PathChain path, State next) {
        if (!follower.isBusy()) {
            follower.followPath(path, true);
            set(next);
        }
    }

    private void intakePath(PathChain path, State next) {
        intake.setPower(1);

        if (!follower.isBusy()) {
            follower.followPath(path, true);
        }

        if (!follower.isBusy()) {
            intake.setPower(0);
            set(next);
        }
    }

    private void shootThen(State next) {
        double t = stateTimer.getElapsedTimeSeconds();


        if (t < 0.2) setShooterAngle(SHOOTER_UP);
        else if (t < 0.5) transferMotor.setPower(1);
        else {
            transferMotor.setPower(0);
            setShooterAngle(SHOOTER_DOWN);
            set(next);
        }
    }

    private void set(State s) {
        state = s;
        stateTimer.resetTimer();
    }

    private State nextAfterShoot() {
        return State.TO_I2;
    }

    // ====== Методы ======
    private void setShooterAngle(double angle) {
        angle = Math.max(0.0, Math.min(1.0, angle));
        servoLeft.setPosition(angle);
        servoRight.setPosition(1.0 - angle);
    }
}
