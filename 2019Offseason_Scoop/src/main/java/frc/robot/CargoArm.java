package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class CargoArm implements System {

    private WPI_TalonSRX cargoArmMotor;

    static final double CARGO_ARM_SPEED = 0.85;

    public CargoArm() {
        cargoArmMotor = new WPI_TalonSRX(Constants.CARGO_ARM_MOTOR_ID);
        cargoArmMotor.setInverted(true);
        initialize();
    }

    @Override
    public void initialize() {
        cargoArmMotor.set(0);
        currentCargoArmState = CargoArmStates.NOT_MOVING;
    }

    @Override
    public void control(Controllers controllers) {
        cargoArm(controllers.getCargoArmAxis());
    }

    public enum CargoArmStates {
        NOT_MOVING, MOVING_UP, MOVING_DOWN
    }

    private CargoArmStates currentCargoArmState;

    public void cargoArm(double cargoArmAxis) {
        switch(currentCargoArmState) {
            case NOT_MOVING:
                if (cargoArmAxis >= Constants.AXIS_THRESHOLD) {
                    currentCargoArmState = CargoArmStates.MOVING_UP; 
                } else if (cargoArmAxis <= -Constants.AXIS_THRESHOLD) {
                    currentCargoArmState = CargoArmStates.MOVING_DOWN;
                }
                break;
            case MOVING_UP:
                if (cargoArmAxis < Constants.AXIS_THRESHOLD) {
                    initialize();
                } else {
                    cargoArmMotor.set(cargoArmAxis);
                }
                break;
            case MOVING_DOWN:
                if (cargoArmAxis > -Constants.AXIS_THRESHOLD) {
                    initialize();
                } else {
                    cargoArmMotor.set(cargoArmAxis);
                } 
                break;
        }
    }
}