package frc.robot;

import java.io.File;
import java.io.IOException;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.PathfinderFRC;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import edu.wpi.first.wpilibj.Filesystem;

public class PathFollowingDrive extends Drive{

    static final double WHEEL_DIAMETER_FT = (Encoders.WHEEL_RADIUS_IN * 2) / 12;
    static double P = 0;
    static double I = 0;
    static double D = 0;
    static double F = 0;
    static double MAX_VELOCITY = 17; //feet per second 17
    static double MAX_ACCELERATION = 6.56;
    static final double WHEEL_BASE_WIDTH = 1.9167; //in feet

    private double desiredFeet = 0;
    private double desiredInches = 0;
    private double desiredTicks = 0;

    private boolean zeroEncoders  = false;
    private boolean test = false;

    // private File leftFile;
    // private File rightFile;
    // private Trajectory leftTraj;
    // private Trajectory rightTraj;

    EncoderFollower left;
    EncoderFollower right;

    private AHRS gyro;
    protected Encoders encoders;

    public PathFollowingDrive() {
        // try {
        //     //leftTraj = PathfinderFRC.getTrajectory("TestPath1.left");
        //     //rightTraj = PathfinderFRC.getTrajectory("TestPath1.right");
        //     leftTraj = Pathfinder.readFromCSV( new File(Filesystem.getDeployDirectory(), "TestPath1.left.pf1.csv"));
        //     rightTraj = Pathfinder.readFromCSV( new File(Filesystem.getDeployDirectory(), "TestPath1.right.pf1.csv"));
        // } catch (IOException ex) {
        //     System.out.println(ex.toString());
        //     System.out.println("Could not find csv file");
        // }

        encoders = new Encoders(this);
        gyro = new AHRS(SPI.Port.kMXP);
    }

    public void initialize() {
        encoders.reset();
        gyro.reset();
        currentMoveState = MoveStates.TELEOP;

        SmartDashboard.putNumber("P", P);
		SmartDashboard.putNumber("I", I);
        SmartDashboard.putNumber("D", D);
        SmartDashboard.putNumber("Max Velocity", MAX_VELOCITY);
        SmartDashboard.putNumber("Max Acceleration", MAX_ACCELERATION);
        SmartDashboard.putNumber("desired feet", desiredFeet);
		
		SmartDashboard.putBoolean("Zero Encoder", false);
		SmartDashboard.putBoolean("Test", test);
    }

    public void init() {
        desiredFeet = SmartDashboard.getNumber("desired feet", 0);
		desiredInches = desiredFeet * 12;
        desiredTicks = (desiredFeet * 12) / Encoders.DISTANCE_PER_TICK;
        
        P = SmartDashboard.getNumber("P", 0);
		I = SmartDashboard.getNumber("I", 0);
        D = SmartDashboard.getNumber("D", 0);
        MAX_VELOCITY = SmartDashboard.getNumber("Max Velocity", 0);
        MAX_ACCELERATION = SmartDashboard.getNumber("Max Acceleration", 0);

        Waypoint[] points = new Waypoint[] {
            new Waypoint(0, 0, 0),
            new Waypoint(desiredFeet, 0, 0)
        };
    
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_LOW, 0.05, MAX_VELOCITY, MAX_ACCELERATION, 60);
        Trajectory trajectory = Pathfinder.generate(points, config);
        TankModifier modifier = new TankModifier(trajectory).modify(0.5);
    
        left = new EncoderFollower(modifier.getLeftTrajectory());
        right = new EncoderFollower(modifier.getRightTrajectory());

        left.configureEncoder(encoders.getLeftCount(), Encoders.TICKS_PER_ROTATION, WHEEL_DIAMETER_FT);
        right.configureEncoder(encoders.getRightCount(), Encoders.TICKS_PER_ROTATION, WHEEL_DIAMETER_FT);
        left.configurePIDVA(P, I, D, 1 / MAX_VELOCITY, 0);
        right.configurePIDVA(P, I, D, 1 / MAX_VELOCITY, 0);

        left.reset();
        right.reset();
    }

    public void runPath() {
        double l = left.calculate(encoders.getLeftCount());
        double r = right.calculate(encoders.getRightCount());

        double gyroHeading = gyro.getAngle();
        double desiredHeading = Pathfinder.r2d(left.getHeading());

        double angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);
        if (Math.abs(angleDifference) > 180.0) {
            angleDifference = (angleDifference > 0) ? angleDifference - 360 : angleDifference + 360;
        } 
        double turn = 0.8 * (-1.0 / 80.0) * angleDifference;

        midLeftMotor.set(l + turn);
        midRightMotor.set(r + turn);
    }

    public enum MoveStates {
        TELEOP, PROFILE
    }

    MoveStates currentMoveState;

    public void drive(boolean followToggle, double speedAxis, double turnAxis) {
        SmartDashboard.putNumber("desired inches", desiredInches);
		SmartDashboard.putNumber("desired pulses", desiredTicks);
		
		SmartDashboard.putNumber("encoder feet", encoders.getAverageDistanceFeet());
		SmartDashboard.putNumber("encoder inches", encoders.getAverageDistanceInches());
		SmartDashboard.putNumber("encoder pulses", encoders.getAverageCount());
		
		SmartDashboard.putNumber("encoder left pulses", encoders.getLeftCount());
        SmartDashboard.putNumber("encoder right pulses", encoders.getRightCount());

        SmartDashboard.putNumber("Gyro", gyro.getAngle());

        zeroEncoders = SmartDashboard.getBoolean("Zero Encoder", false);
        test = SmartDashboard.getBoolean("Test", false);

        if (zeroEncoders) {
            encoders.reset();
        }

        switch(currentMoveState) {
            case TELEOP:
                frontRightMotor.setInverted(false);
                midRightMotor.setInverted(false);
                backRightMotor.setInverted(false);
                drive(speedAxis, turnAxis);
                if (test) {
                    init();
                    initialize();
                    currentMoveState = MoveStates.PROFILE;
                }
                break;
            case PROFILE:
                frontRightMotor.setInverted(true);
                midRightMotor.setInverted(true);
                backRightMotor.setInverted(true);
                runPath();
                if (!test) {
                    initialize();
                    currentMoveState = MoveStates.TELEOP;
                }
                break;
        }
    }
}