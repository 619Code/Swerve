package org.usfirst.frc.team619.subsystems.drive;

public class SwerveCalcValue {
	private double[] speeds;
	private double[] angles;

	// intended to only be called by SwerveCalc
	SwerveCalcValue( double[] angles_, double[] speeds_ ) {
		angles = angles_;
		speeds = speeds_;
	}
	// retrieve speed and angle values
	// or perhaps allow for direct access to speeds and angles members
	double speed1() { return 0; }
}
