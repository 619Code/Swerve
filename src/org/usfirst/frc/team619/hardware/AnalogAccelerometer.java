package org.usfirst.frc.team619.hardware;

public class AnalogAccelerometer {

	private edu.wpi.first.wpilibj.AnalogAccelerometer accelerometer;

	private int channel;
	
	public AnalogAccelerometer(int channel/*where accelerometer is plugged into athena*/){
		accelerometer = new edu.wpi.first.wpilibj.AnalogAccelerometer(channel);
		this.channel = channel;
	}
	
	public double getAcceleration(){
		return accelerometer.getAcceleration();
	}
	
	public int getChannel(){
		return channel;
	}
	
}
