package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive implements System{

    private WPI_TalonSRX frontLeftMotor;
    private WPI_TalonSRX midLeftMotor;
    private WPI_TalonSRX backLeftMotor;

    private WPI_TalonSRX frontRightMotor;
    private WPI_TalonSRX midRightMotor;
    private WPI_TalonSRX backRightMotor;

    private DifferentialDrive robotDrive;

    public static double DRIVE_SPEED = .95;

    public Drive() {
        frontLeftMotor = new WPI_TalonSRX(Constants.FRONT_LEFT_MOTOR_ID);
        midLeftMotor = new WPI_TalonSRX(Constants.MID_LEFT_MOTOR_ID);
        backLeftMotor = new WPI_TalonSRX(Constants.BACK_LEFT_MOTOR_ID);

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
    }

    @Override
    public void initialize() {
        midLeftMotor.set(0);
        midLeftMotor.set(0);
        SmartDashboard.putNumber("Drive Speed", DRIVE_SPEED);
    }

    @Override
    public void control(Controllers controllers) {
        driveSwitch(controllers.getDriveSpeedAxis() * DRIVE_SPEED, controllers.getDriveTurnAxis() * DRIVE_SPEED);
    }

    public void drive(double speedAxis, double turnAxis) {
        robotDrive.arcadeDrive(speedAxis, turnAxis);
    }

    public void driveSwitch(double speedAxis, double turnAxis) {
        switch(SwitchSides.side) {
            case HATCH_SIDE:
                drive(speedAxis, turnAxis);
                break;
            case CARGO_SIDE:
                drive(-speedAxis, turnAxis);
                break;
        }
    }

    public void driveSpeedReset() {
        DRIVE_SPEED = SmartDashboard.getNumber("Drive Speed", .95);
    }
}