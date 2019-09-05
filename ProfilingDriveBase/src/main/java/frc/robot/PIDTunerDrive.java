package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PIDTunerDrive extends PathFollowingDrive {

    private double desiredFeet = 0;
    private double desiredInches = 0;
    private double desiredTicks = 0;

    private boolean zeroEncoders  = false;
    private boolean testPID = false;

    private double motorSpeed = 0;
    private boolean testVelocity = false;
    private double setTime = 0;
    private double counter = 0;
    private double lastVelocity = 0;
    private double velocity = 0;
    private double acceleration = 0;

    public PIDTunerDrive() {
        midLeftMotor.setSensorPhase(false);
        midLeftMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        midLeftMotor.configClosedLoopPeakOutput(0, .95, 10);

        midRightMotor.setSensorPhase(false);
        midRightMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        midRightMotor.configClosedLoopPeakOutput(0, .95, 10);
    }

    public void initialize() {
        super.initialize();
        SmartDashboard.putNumber("P", P);
		SmartDashboard.putNumber("I", I);
        SmartDashboard.putNumber("D", D);
        SmartDashboard.putNumber("F", F);
		
		SmartDashboard.putNumber("desired feet", desiredFeet);
		
		SmartDashboard.putBoolean("Zero Encoder", false);
		SmartDashboard.putBoolean("Test PID", false);
        
        SmartDashboard.putNumber("Motor Speed", motorSpeed);
        SmartDashboard.putBoolean("Test Velocity", false);
        SmartDashboard.putNumber("Set Time", setTime);
        
        counter = 0;
        currentTuneState = TuneStates.NOT_MOVING;
    }

    public enum TuneStates {
        NOT_MOVING, PIDTune, VelocityTune
    }

    TuneStates currentTuneState;

    public void tune() {
        frontRightMotor.setInverted(true);
        midRightMotor.setInverted(true);
        backRightMotor.setInverted(true);

        desiredFeet = SmartDashboard.getNumber("desired feet", 0);
		desiredInches = desiredFeet * 12;
		desiredTicks = (desiredFeet * 12) / Encoders.DISTANCE_PER_TICK;
		
		SmartDashboard.putNumber("desired inches", desiredInches);
		SmartDashboard.putNumber("desired pulses", desiredTicks);
		
		SmartDashboard.putNumber("encoder feet", encoders.getAverageDistanceFeet());
		SmartDashboard.putNumber("encoder inches", encoders.getAverageDistanceInches());
		SmartDashboard.putNumber("encoder pulses", encoders.getAverageCount());
		
		SmartDashboard.putNumber("encoder left pulses", encoders.getLeftCount());
        SmartDashboard.putNumber("encoder right pulses", encoders.getRightCount());
        
        SmartDashboard.putNumber("Time", counter);
        SmartDashboard.putNumber("Velocity", velocity);
        SmartDashboard.putNumber("Acceleration", acceleration);
		
		P = SmartDashboard.getNumber("P", 0);
		I = SmartDashboard.getNumber("I", 0);
        D = SmartDashboard.getNumber("D", 0);
        F = SmartDashboard.getNumber("F", 0);
		
		zeroEncoders = SmartDashboard.getBoolean("Zero Encoder", false);
        testPID = SmartDashboard.getBoolean("Test PID", false);
        motorSpeed = SmartDashboard.getNumber("Motor Speed", 0);
        testVelocity = SmartDashboard.getBoolean("Test Velocity", false);
        setTime = SmartDashboard.getNumber("Set Time", 0);

        if (zeroEncoders) {
            encoders.reset();
        }

        switch(currentTuneState) {
            case NOT_MOVING:
                drive(0, 0);
                counter = 0;
                if (testPID) {
                    currentTuneState = TuneStates.PIDTune;
                } else if (testVelocity) {
                    currentTuneState = TuneStates.VelocityTune;
                }
                break;
            case PIDTune:
                if (!testPID) {
                    currentTuneState = TuneStates.NOT_MOVING;
                } else {
                    midLeftMotor.config_kP(0, P, 10);
                    midLeftMotor.config_kI(0, I, 10);
                    midLeftMotor.config_kD(0, D, 10);
                    midLeftMotor.config_kF(0, F, 10);
                    midLeftMotor.set(ControlMode.Position, desiredTicks);

                    midRightMotor.config_kP(0, P, 10);
                    midRightMotor.config_kI(0, I, 10);
                    midRightMotor.config_kD(0, D, 10);
                    midRightMotor.config_kF(0, F, 10);
                    midRightMotor.set(ControlMode.Position, desiredTicks);
                }
                break;
            case VelocityTune:
                if (!testVelocity) {
                    currentTuneState = TuneStates.NOT_MOVING;
                } else {
                    if (counter < setTime) {
                    //if (encoders.getAverageDistanceFeet() < desiredFeet) {
                        counter += 0.05;
                        
                        // midLeftMotor.set(ControlMode.PercentOutput, motorSpeed);
                        // midRightMotor.set(ControlMode.PercentOutput, motorSpeed);
                        velocity = encoders.getAverageDistanceFeet() / counter;
                        System.out.println("Velocity: " + velocity);
                        System.out.println("Acceleration: " + acceleration);
                        acceleration = (velocity - lastVelocity) / 0.05;
                        lastVelocity = velocity;
                    } else {
                        midLeftMotor.set(0);
                        midRightMotor.set(0);
                        testVelocity = false;
                    }
                }
                break;
        }
    }

}