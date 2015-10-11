package org.usfirst.frc.team619.hardware;

public class AnalogPotentiometer {

	private edu.wpi.first.wpilibj.AnalogPotentiometer pot;
	
	private int channel;
	
	public AnalogPotentiometer(int channel/*where potentiometer is plugged in*/){
		pot = new edu.wpi.first.wpilibj.AnalogPotentiometer(channel);
		this.channel = channel;
	}
	
	public double get(){
		return pot.get();
	}
	
	public int getChannel(){
		return channel;
	}
	
}
