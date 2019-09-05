package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controllers {

    private Joystick mobilityStick;
    private double driveSpeedAxis;
    private double driveTurnAxis;
    private Toggle followToggle;
    private boolean followButton;


    public Controllers() {
        mobilityStick = new Joystick(Constants.MOBILITY_CONTROLLER_ID);
        followToggle = new Toggle(mobilityStick, Constants.FOLLOW_TOGGLE_BUTTON);
    }

    public void updateControllerValues() {
        driveSpeedAxis = mobilityStick.getRawAxis(Constants.DRIVE_SPEED_AXIS);
        driveTurnAxis = -mobilityStick.getRawAxis(Constants.DRIVE_TURN_AXIS); // turn axis always negative for arcade drive
        followButton = followToggle.toggle();
    }

    public boolean isFollowButton() {
        return followButton;
    }
    public double getDriveSpeedAxis() {
        return driveSpeedAxis;
    }
    public double getDriveTurnAxis() {
        return driveTurnAxis;
    }
}