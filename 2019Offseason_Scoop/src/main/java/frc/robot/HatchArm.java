package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class HatchArm implements System {
    
    private WPI_TalonSRX hatchArmMotor;
    
    static final double HATCH_ARM_SPEED = 0.5;

    public HatchArm() {
        hatchArmMotor = new WPI_TalonSRX(Constants.HATCH_ARM_MOTOR_ID);
        initialize();
    }

    @Override
    public void initialize() {
        hatchArmMotor.set(0);
        currentHatchArmState = HatchArmStates.NOT_MOVING;
    }

    @Override
    public void control(Controllers controllers) {
        hatchArm(controllers.getHatchArmAxis());
    }

    public enum HatchArmStates {
        NOT_MOVING, MOVING_UP, MOVING_DOWN
    }

    private HatchArmStates currentHatchArmState;

    public void hatchArm(double hatchArmAxis) {
        switch(currentHatchArmState) {
            case NOT_MOVING:
                if (hatchArmAxis <= -Constants.AXIS_THRESHOLD) {
                    currentHatchArmState = HatchArmStates.MOVING_UP;
                } else if (hatchArmAxis >= Constants.AXIS_THRESHOLD) {
                    currentHatchArmState = HatchArmStates.MOVING_DOWN;
                }
                break;
            case MOVING_UP:
                if (hatchArmAxis > -Constants.AXIS_THRESHOLD) {
                    initialize();
                } else {
                    hatchArmMotor.set(hatchArmAxis);
                }
                break;
            case MOVING_DOWN:
                if (hatchArmAxis < -Constants.AXIS_THRESHOLD)  {
                    initialize();
                } else {
                    hatchArmMotor.set(hatchArmAxis);
                }
                break;
        }
    }
}