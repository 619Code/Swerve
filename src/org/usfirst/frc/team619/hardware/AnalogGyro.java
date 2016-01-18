package org.usfirst.frc.team619.hardware;

public class AnalogGyro extends edu.wpi.first.wpilibj.AnalogGyro {
	
	AnalogGyro gyro;
	
	public AnalogGyro(int channel) {
		super(channel);
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
