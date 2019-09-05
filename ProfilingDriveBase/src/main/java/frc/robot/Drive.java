package frc.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drive {

    protected WPI_TalonSRX frontLeftMotor;
    protected WPI_TalonSRX midLeftMotor;
    protected WPI_TalonSRX backLeftMotor;

    protected WPI_TalonSRX frontRightMotor;
    protected WPI_TalonSRX midRightMotor;
    protected WPI_TalonSRX backRightMotor;

    protected DifferentialDrive robotDrive;

    protected SensorCollection leftSensors;
    protected SensorCollection rightSensors;

    public Drive() {
        frontLeftMotor = new WPI_TalonSRX(Constants.FRONT_LEFT_MOTOR_ID);
        midLeftMotor = new WPI_TalonSRX(Constants.MID_LEFT_MOTOR_ID);
        backLeftMotor = new WPI_TalonSRX(Constants.BACK_LEFT_MOTOR_ID);

        midLeftMotor.setInverted(false); // true on practice bot
        frontLeftMotor.follow(midLeftMotor);
        backLeftMotor.follow(midLeftMotor);

        frontRightMotor = new WPI_TalonSRX(Constants.FRONT_RIGHT_MOTOR_ID);
        midRightMotor = new WPI_TalonSRX(Constants.MID_RIGHT_MOTOR_ID);
        backRightMotor = new WPI_TalonSRX(Constants.BACK_RIGHT_MOTOR_ID);

        frontRightMotor.follow(midRightMotor);
        backRightMotor.follow(midRightMotor);

        robotDrive = new DifferentialDrive(midLeftMotor, midRightMotor);
        robotDrive.setSafetyEnabled(false);
        robotDrive.setDeadband(Constants.AXIS_THRESHOLD);

        leftSensors = new SensorCollection(midLeftMotor);
        rightSensors = new SensorCollection(midRightMotor);
    }

    public void drive(double speedAxis, double turnAxis) {
        robotDrive.arcadeDrive(speedAxis, turnAxis);
    }

    public SensorCollection getLeftSensors() {
        return leftSensors;
    }
    public SensorCollection getRightSensors() {
        return rightSensors;
    }
}