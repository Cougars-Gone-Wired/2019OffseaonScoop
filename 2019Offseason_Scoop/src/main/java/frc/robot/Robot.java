package frc.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {

  private ArrayList<System> systems = new ArrayList<>();

  private Controllers controllers;
  private HatchArm hatchArm;
  private CargoArm cargoArm;
  private CargoIntake cargoIntake;
  private Drive drive;
  private Lift lift;
  private SwitchSides sides;
  private Cameras cameras;

  @Override
  public void robotInit() {
    controllers = new Controllers();
    hatchArm = new HatchArm();
    cargoArm = new CargoArm();
    cargoIntake = new CargoIntake();
    drive = new Drive();
    lift = new Lift();
    sides = new SwitchSides();
    cameras = new Cameras();

    systems.add(hatchArm);
    systems.add(cargoArm);
    systems.add(cargoIntake);
    systems.add(drive);
    systems.add(lift);
    systems.add(sides);
    systems.add(cameras);

    new Thread(cameras).start();
  }

  @Override
  public void autonomousInit() {
    initialize();
  }

  @Override
  public void autonomousPeriodic() {
    driverControl();
  }

  @Override
  public void teleopInit() {
    initialize();
  }

  @Override
  public void teleopPeriodic() {
    driverControl();
  }

  @Override
  public void disabledInit() {
    drive.driveSpeedReset();
  }

  public void initialize() {
    for(System system : systems) system.initialize();
  }

  public void driverControl() {
    controllers.updateControllerValues();
    for(System system : systems) system.control(controllers);
  }
}
