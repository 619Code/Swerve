package org.usfirst.frc.team619.hardware;

public class Solenoid {

	private edu.wpi.first.wpilibj.Solenoid solenoid;
	
	private long lastSetTime;//used to help keep the pneumatics from "bouncing" (opening and closing too quickly at the small push of a button)
	
	public Solenoid(int channel/*where solenoid plugs into pneumatic module*/){
		solenoid = new edu.wpi.first.wpilibj.Solenoid(channel);
		lastSetTime = System.currentTimeMillis();
	}
	
	public boolean get(){
		return solenoid.get();
	}
	
	public void set(boolean on){
		solenoid.set(on);
		lastSetTime = System.currentTimeMillis();
	}
	
	public long getLastSetTime(){
		return lastSetTime;
	}
	
}
