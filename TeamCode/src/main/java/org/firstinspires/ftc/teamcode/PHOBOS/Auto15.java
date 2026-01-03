package org.firstinspires.ftc.teamcode.PHOBOS;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "AUTOPATHING")
public class Auto15 extends OpMode {

    /* ================= HARDWARE ================= */
    private DcMotorEx shooter1, shooter2;
    private DcMotor intake;
    private Servo angleServo;

    /* ================= PATHING ================= */
    private Follower follower;
    private Timer pathTimer;

    private boolean pathStarted = false;

    /* ================= SHOOTER ================= */
    private boolean shooterSpinning = false;
    private boolean shooterFired = false;

    private static final double SHOOT_POWER = 1.0;
    private static final double SHOOT_ANGLE = 0.55;

    /* ================= FSM ================= */
    public enum PathState {
        DRIVE_START_TO_SHOOT,
        SHOOT,

        DRIVE_SHOOT_TO_I1_START,
        DRIVE_I1_START_TO_END,
        DRIVE_I1_END_TO_SHOOT,

        DRIVE_SHOOT_TO_I2_START,
        DRIVE_I2_START_TO_END,
        DRIVE_I2_END_TO_SHOOT,

        DRIVE_SHOOT_TO_I3_START,
        DRIVE_I3_START_TO_END,
        DRIVE_I3_END_TO_SHOOT,

        DRIVE_SHOOT_TO_I4_START,
        DRIVE_I4_START_TO_END,
        DRIVE_I4_END_TO_SHOOT,

        DONE
    }

    private PathState pathState;

    /* ================= POSES ================= */
    private final Pose startPose  = new Pose(20, 123, 0);
    private final Pose shootPose  = new Pose(60, 84, 0);

    private final Pose i1s = new Pose(36, 84, 0);
    private final Pose i1e = new Pose(14, 84, 0);

    private final Pose i2s = new Pose(36, 60, 0);
    private final Pose i2e = new Pose(14, 60, 0);

    private final Pose i3s = new Pose(36, 36, 0);
    private final Pose i3e = new Pose(14, 36, 0);

    private final Pose i4s = new Pose(36, 10, 0);
    private final Pose i4e = new Pose(14, 10, 0);

    private final Pose gate = new Pose(16, 66);

    /* ================= PATHS ================= */
    private PathChain
            s_to_shoot,
            shoot_to_i1s, i1s_to_i1e, i1e_to_shoot,
            shoot_to_i2s, i2s_to_i2e, i2e_to_shoot,
            shoot_to_i3s, i3s_to_i3e, i3e_to_shoot,
            shoot_to_i4s, i4s_to_i4e, i4e_to_shoot;

    /* ================= INIT ================= */
    @Override
    public void init() {
        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter1");
        shooter2 = hardwareMap.get(DcMotorEx.class, "shooter2");
        intake   = hardwareMap.get(DcMotor.class, "intake");
        angleServo = hardwareMap.get(Servo.class, "angleServo");

        shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        follower = Constants.createFollower(hardwareMap);
        pathTimer = new Timer();

        buildPaths();
        follower.setPose(startPose);

        pathState = PathState.DRIVE_START_TO_SHOOT;
    }

    /* ================= PATH BUILD ================= */
    private void buildPaths() {
        s_to_shoot = path(startPose, shootPose);

        shoot_to_i1s = path(shootPose, i1s);
        i1s_to_i1e   = path(i1s, i1e);
        i1e_to_shoot = path(i1e, shootPose);

        shoot_to_i2s = path(shootPose, i2s);
        i2s_to_i2e   = path(i2s, i2e);
        i2e_to_shoot = path(i2e, shootPose);

        shoot_to_i3s = path(shootPose, i3s);
        i3s_to_i3e   = path(i3s, i3e);
        i3e_to_shoot = path(i3e, shootPose);

        shoot_to_i4s = path(shootPose, i4s);
        i4s_to_i4e   = path(i4s, i4e);
        i4e_to_shoot = path(i4e, shootPose);
    }

    private PathChain path(Pose a, Pose b) {
        return follower.pathBuilder()
                .addPath(new BezierLine(a, b))
                .setLinearHeadingInterpolation(a.getHeading(), b.getHeading())
                .build();
    }

    /* ================= LOOP ================= */
    @Override
    public void loop() {
        follower.update();
        stateMachine();

        telemetry.addData("STATE", pathState);
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
    }

    /* ================= FSM ================= */
    private void stateMachine() {
        switch (pathState) {

            case DRIVE_START_TO_SHOOT:
                followOnce(s_to_shoot, PathState.SHOOT);
                break;

            case SHOOT:
                shootThen(PathState.DRIVE_SHOOT_TO_I1_START);
                break;

            case DRIVE_SHOOT_TO_I1_START:
                followOnce(shoot_to_i1s, PathState.DRIVE_I1_START_TO_END);
                break;

            case DRIVE_I1_START_TO_END:
                intakeOnPath(i1s_to_i1e, PathState.DRIVE_I1_END_TO_SHOOT);
                break;

            case DRIVE_I1_END_TO_SHOOT:
                followOnce(i1e_to_shoot, PathState.SHOOT);
                break;

            case DRIVE_SHOOT_TO_I2_START:
                followOnce(shoot_to_i2s, PathState.DRIVE_I2_START_TO_END);
                break;

            case DRIVE_I2_START_TO_END:
                intakeOnPath(i2s_to_i2e, PathState.DRIVE_I2_END_TO_SHOOT);
                break;

            case DRIVE_I2_END_TO_SHOOT:
                followOnce(i2e_to_shoot, PathState.SHOOT);
                break;

            case DRIVE_SHOOT_TO_I3_START:
                followOnce(shoot_to_i3s, PathState.DRIVE_I3_START_TO_END);
                break;

            case DRIVE_I3_START_TO_END:
                intakeOnPath(i3s_to_i3e, PathState.DRIVE_I3_END_TO_SHOOT);
                break;

            case DRIVE_I3_END_TO_SHOOT:
                followOnce(i3e_to_shoot, PathState.SHOOT);
                break;

            case DRIVE_SHOOT_TO_I4_START:
                followOnce(shoot_to_i4s, PathState.DRIVE_I4_START_TO_END);
                break;

            case DRIVE_I4_START_TO_END:
                intakeOnPath(i4s_to_i4e, PathState.DRIVE_I4_END_TO_SHOOT);
                break;

            case DRIVE_I4_END_TO_SHOOT:
                followOnce(i4e_to_shoot, PathState.DONE);
                break;

            case DONE:
                shooter1.setPower(0);
                shooter2.setPower(0);
                intake.setPower(0);
                break;
        }
    }

    /* ================= HELPERS ================= */
    private void followOnce(PathChain path, PathState next) {
        if (!pathStarted) {
            follower.followPath(path, true);
            pathStarted = true;
        }
        if (!follower.isBusy()) {
            pathStarted = false;
            pathState = next;
        }
    }

    private void intakeOnPath(PathChain path, PathState next) {
        intake.setPower(1.0);
        followOnce(path, next);
        if (!pathStarted) intake.setPower(0.0);
    }

    private void shootThen(PathState next) {
        if (!shooterSpinning) {
            angleServo.setPosition(SHOOT_ANGLE);
            shooter1.setPower(SHOOT_POWER);
            shooter2.setPower(SHOOT_POWER);
            pathTimer.resetTimer();
            shooterSpinning = true;
        }

        if (!shooterFired && pathTimer.getElapsedTimeSeconds() > 0.6) {
            shooterFired = true;
            pathTimer.resetTimer();
        }

        if (shooterFired && pathTimer.getElapsedTimeSeconds() > 0.3) {
            shooter1.setPower(0);
            shooter2.setPower(0);
            shooterSpinning = false;
            shooterFired = false;
            pathState = next;
        }
    }
}
