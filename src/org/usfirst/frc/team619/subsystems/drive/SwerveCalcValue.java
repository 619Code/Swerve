package org.usfirst.frc.team619.subsystems.drive;

public class SwerveCalcValue {
	// order of wheels is:
	//     { front_right, front_left, rear_left, rear_right }
	private double[] speeds;
	private double[] angles;

	// intended to only be called by SwerveCalc
	SwerveCalcValue( double[] angles_, double[] speeds_ ) {
		angles = angles_;
		speeds = speeds_;
	}
	// retrieve speed and angle values
	// or perhaps allow for direct access to speeds and angles members
	public double FRAngle( ) { return angles[0]; } // front right angle
	public double FRSpeed( ) { return speeds[0]; } // front right speed
	public double FLAngle( ) { return angles[1]; } // front left angle
	public double FLSpeed( ) { return speeds[1]; } // front left speed
	public double BLAngle( ) { return angles[2]; } // back left angle
	public double BLSpeed( ) { return speeds[2]; } // back left speed
	public double BRAngle( ) { return angles[3]; } // back right angle
	public double BRSpeed( ) { return speeds[3]; } // back right speed
}
