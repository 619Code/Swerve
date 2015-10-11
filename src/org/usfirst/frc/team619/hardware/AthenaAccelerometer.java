package org.usfirst.frc.team619.hardware;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;

/**
 * 
 * The Athena Accelerometer is the accelerometer that is built in
 * to the RoboRio/Athena
 * 
 * It can give x and y values as well as z values (turning axis)
 * 
 * This means that you don't need to use an accelerometer and/or gyro on the robot
 * unless you're having them keep track of different planes that Athena is not on.
 * 
 * @author Student
 */

public class AthenaAccelerometer {

	BuiltInAccelerometer accelerometer;
	
	public AthenaAccelerometer(){
		accelerometer = new BuiltInAccelerometer();
	}
	
	public double getX(){
		return accelerometer.getX();
	}
	
	public double getY(){
		return accelerometer.getY();
	}
	
	public double getZ(){
		return accelerometer.getZ();
	}
	
	public void setRange(Range range){
		accelerometer.setRange(range);
	}
	
}
