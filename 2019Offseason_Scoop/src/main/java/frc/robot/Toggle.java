package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Toggle {

	private boolean buttonState;
    private boolean state = false;

	private Joystick stick;
	private int buttonNumber;

	public Toggle(Joystick stick, int buttonNumber) {
		this.stick = stick;
		this.buttonNumber = buttonNumber;
		state = false;
	}

	public boolean toggle() {
		if (stick.getRawButton(buttonNumber)) {
			if (!buttonState) {
				state = !state;
			}
			buttonState = true;
		} else {
			buttonState = false;
		}
		return state;
    }
    
	public void setState(boolean state) {
		this.state = state;
	}
}