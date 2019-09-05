package frc.robot;

import com.ctre.phoenix.motorcontrol.SensorCollection;

public class Encoders {

    public SensorCollection leftSensors;
    public SensorCollection rightSensors;

    static final double WHEEL_RADIUS_IN = 3;
    static final double CIRCUMFERENCE = 2 * Math.PI * WHEEL_RADIUS_IN;
	static final int TICKS_PER_ROTATION = 400; //400
    static final double DISTANCE_PER_TICK = CIRCUMFERENCE / TICKS_PER_ROTATION;

    public Encoders(Drive drive) {
        leftSensors = drive.getLeftSensors();
        rightSensors = drive.getRightSensors();
    }

    public int getLeftCount() {
		return leftSensors.getQuadraturePosition();
	}
	public int getRightCount() {
		return -rightSensors.getQuadraturePosition();
	}
	public int getAverageCount() {
		return (getLeftCount() + getRightCount()) / 2;
	}
	
	
	public double getLeftDistanceInches() {
		return getLeftCount() * DISTANCE_PER_TICK;
	}
	public double getRightDistanceInches() {
		return getRightCount() * DISTANCE_PER_TICK;
	}
	public double getAverageDistanceInches() {
		return (getLeftDistanceInches() + getRightDistanceInches()) / 2;
	}
	
	
	public double getLeftDistanceFeet() {
		return (getLeftCount() * DISTANCE_PER_TICK) / 12;
	}
	public double getRightDistanceFeet() {
		return (getRightCount() * DISTANCE_PER_TICK) / 12;
	}
	public double getAverageDistanceFeet() {
		return (getLeftDistanceFeet() + getRightDistanceFeet()) / 2;
	}
	
	
	public void resetLeftEncoder() {
		leftSensors.setQuadraturePosition(0, 10);
	}

	public void resetRightEncoder() {
		rightSensors.setQuadraturePosition(0, 10);
	}

	public void reset() {
		leftSensors.setQuadraturePosition(0, 10);
		rightSensors.setQuadraturePosition(0, 10);
	}
}