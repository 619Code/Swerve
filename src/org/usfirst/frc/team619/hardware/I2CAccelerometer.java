package org.usfirst.frc.team619.hardware;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;

public class I2CAccelerometer {

	Accelerometer accelerometer;
	
	public I2CAccelerometer(){
		accelerometer = new ADXL345_I2C(I2C.Port.kOnboard, Accelerometer.Range.k4G);
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
