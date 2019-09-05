package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Lift implements System {

    private WPI_TalonSRX liftLeftMotor;
    private WPI_TalonSRX liftRightMotor;

    static final double LIFT_SPEED = 0.75;

    public Lift() {
        liftLeftMotor = new WPI_TalonSRX(Constants.LIFT_LEFT_MOTOR_ID);
        liftRightMotor = new WPI_TalonSRX(Constants.LIFT_RIGHT_MOTOR_ID);
        initialize();
    }

    @Override
    public void initialize() {
        liftLeftMotor.set(0);
        liftRightMotor.set(0);
        currentLiftState = LiftStates.NOT_MOVING;
    }

    @Override
    public void control(Controllers controllers) {
        lift(controllers.isLiftOutButton(), controllers.isLiftInButton());
    }

    public enum LiftStates {
        NOT_MOVING, MOVING_OUT, MOVING_IN
    }

    private LiftStates currentLiftState;

    public void lift(boolean outButton, boolean inButton) {
        switch(currentLiftState) {
            case NOT_MOVING:
                initialize();
                if(outButton && !inButton) {
                    liftLeftMotor.set(-LIFT_SPEED);
                    liftRightMotor.set(LIFT_SPEED);
                    currentLiftState = LiftStates.MOVING_OUT;
                } else if (inButton && !outButton) {
                    liftLeftMotor.set(LIFT_SPEED);
                    liftRightMotor.set(-LIFT_SPEED);
                }
                break;
            case MOVING_OUT:
                if(!outButton || inButton) {
                    initialize();
                }
                break;
            case MOVING_IN:
                if(!inButton || outButton) {
                    initialize();
                }
                break;
        }
    }
}