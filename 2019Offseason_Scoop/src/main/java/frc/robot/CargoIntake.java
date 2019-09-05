package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class CargoIntake implements System {

    private WPI_TalonSRX cargoIntakeMotor;

    static final double CARGO_INTAKE_SPEED = 0.7;
    static final double CARGO_OUTTAKE_SPEED = -1.0;

    public CargoIntake() {
        cargoIntakeMotor = new WPI_TalonSRX(Constants.CARGO_INTAKE_MOTOR_ID);
    }

    @Override
    public void initialize() {
        cargoIntakeMotor.set(0);
        currentCargoIntakeState = CargoIntakeStates.NOT_MOVING;
    }

    @Override
    public void control(Controllers controllers) {
        cargoIntake(controllers.getCargoIntakeTrigger(), controllers.getCargoOuttakeTrigger());
    }

    public enum CargoIntakeStates {
        NOT_MOVING, INTAKING, OUTTAKING
    }

    CargoIntakeStates currentCargoIntakeState;

    public void cargoIntake(double cargoIntakeTrigger, double cargoOuttakeTrigger) {
        switch(currentCargoIntakeState) {
            case NOT_MOVING:
                if ((cargoIntakeTrigger >= Constants.AXIS_THRESHOLD) && (cargoOuttakeTrigger < Constants.AXIS_THRESHOLD)) {
                    cargoIntakeMotor.set(CARGO_INTAKE_SPEED);
                    currentCargoIntakeState = CargoIntakeStates.INTAKING;
                } else if ((cargoOuttakeTrigger >= Constants.AXIS_THRESHOLD) && (cargoIntakeTrigger < Constants.AXIS_THRESHOLD)) {
                    cargoIntakeMotor.set(CARGO_OUTTAKE_SPEED);
                    currentCargoIntakeState = CargoIntakeStates.OUTTAKING;
                }
                break;
            case INTAKING:
                if ((cargoIntakeTrigger < Constants.AXIS_THRESHOLD) || (cargoOuttakeTrigger >= Constants.AXIS_THRESHOLD)) {
                    initialize();
                }
                break;
            case OUTTAKING:
                if ((cargoOuttakeTrigger < Constants.AXIS_THRESHOLD) || (cargoIntakeTrigger >= Constants.AXIS_THRESHOLD)) {
                    initialize();
                }
                break;
        }
    }
}