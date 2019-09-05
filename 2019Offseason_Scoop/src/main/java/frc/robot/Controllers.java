package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controllers {

    // Manipulator Controller
    private Joystick manipulatorStick;
    private double hatchArmAxis;
    private double cargoArmAxis;
    private double cargoIntakeTrigger;
    private double cargoOuttakeTrigger;

    // Mobility Controller
    private Joystick mobilityStick;
    private double driveSpeedAxis;
    private double driveTurnAxis;
    private boolean liftOutButton;
    private boolean liftInButton;
    private Toggle sideToggle;
    private boolean switchSide;
    private Toggle cameraToggle;
    private boolean cameraPower;


    public Controllers() {
        // Manipulator Controller
        manipulatorStick = new Joystick(Constants.MANIPULATOR_CONTROLLER_ID);

        // Mobility Controller
        mobilityStick = new Joystick(Constants.MOBILITY_CONTROLLER_ID);
        sideToggle = new Toggle(mobilityStick, Constants.SIDE_TOGGLE_BUTTON);
        cameraToggle = new Toggle(mobilityStick, Constants.CAMERA_TOGGLE_BUTTON);
    }

    public void updateControllerValues() {
        // Manipulator Controller
        hatchArmAxis = manipulatorStick.getRawAxis(Constants.HATCH_ARM_AXIS);
        cargoArmAxis = manipulatorStick.getRawAxis(Constants.CARGO_ARM_AXIS);
        cargoIntakeTrigger = manipulatorStick.getRawAxis(Constants.CARGO_INTAKE_TRIGGER);
        cargoOuttakeTrigger = manipulatorStick.getRawAxis(Constants.CARGO_OUTTAKE_TRIGGER);

        // Mobility Controller
        driveSpeedAxis = mobilityStick.getRawAxis(Constants.DRIVE_SPEED_AXIS);
        driveTurnAxis = -mobilityStick.getRawAxis(Constants.DRIVE_TURN_AXIS); // turn axis always negative for arcade drive
        liftOutButton = mobilityStick.getRawButton(Constants.LIFT_OUT_BUTTON);
        liftInButton = mobilityStick.getRawButton(Constants.LIFT_IN_BUTTON);
        switchSide = sideToggle.toggle();
        cameraPower = cameraToggle.toggle();
    }

    // Manipulator Controller Getters
    public double getHatchArmAxis() {
        return hatchArmAxis;
    }
    public double getCargoArmAxis() {
        return cargoArmAxis;
    }
    public double getCargoIntakeTrigger() {
        return cargoIntakeTrigger;
    }
    public double getCargoOuttakeTrigger() {
        return cargoOuttakeTrigger;
    }

    // Mobility Controller Getters
    public double getDriveSpeedAxis() {
        return driveSpeedAxis;
    }
    public double getDriveTurnAxis() {
        return driveTurnAxis;
    }
    public boolean isLiftOutButton() {
        return liftOutButton;
    }
    public boolean isLiftInButton() {
        return liftInButton;
    }
    public boolean isSwitchSide() {
        return switchSide;
    }
    public boolean isCameraPower() {
        return cameraPower;
    }
}