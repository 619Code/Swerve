package org.usfirst.frc.team619.hardware;

import edu.wpi.first.wpilibj.Gyro;

public class AnalogGyro {
	
	Gyro gyro;
	
	public AnalogGyro(int channel) {
		gyro = new Gyro(channel);
	}
	
	public double getAngle(){
		return gyro.getAngle();
	}
	
	public double getRate(){
		return gyro.getRate();
	}
	
	public double pidGet(){
		return gyro.pidGet();
	}
	
	public void initGyro(){
		 gyro.initGyro();
	}
	
	public void reset(){
		gyro.reset();
	}
	
}
