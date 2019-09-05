package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {

  Controllers controllers;
  PathFollowingDrive drive;

  public Robot() {
    super(0.05);
  }

  @Override
  public void robotInit() {
    controllers = new Controllers();
    drive = new PIDTunerDrive();
  }

  @Override
  public void autonomousInit() {
    drive.initialize();
  }

  @Override
  public void autonomousPeriodic() {
    //drive.tune();
  }

  @Override
  public void teleopInit() {
    drive.initialize();
  }

  @Override
  public void teleopPeriodic() {
    controllers.updateControllerValues();
    drive.drive(controllers.isFollowButton(), controllers.getDriveSpeedAxis(), controllers.getDriveTurnAxis());
  }
}
