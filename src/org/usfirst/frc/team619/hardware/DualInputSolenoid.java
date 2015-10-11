package org.usfirst.frc.team619.hardware;


public class DualInputSolenoid {
	
	private edu.wpi.first.wpilibj.Solenoid solLeft, solRight;
	
	private long lastSetTime;
	
	public DualInputSolenoid(int channelLeft, int channelRight/*where solenoid plugs into on pneumatic module*/){
		solLeft = new edu.wpi.first.wpilibj.Solenoid(channelLeft);
		solRight = new edu.wpi.first.wpilibj.Solenoid(channelRight);
		lastSetTime = System.currentTimeMillis();
	}
	
	public boolean getLeft(){
		return solLeft.get();
	}
	
	public boolean getRight(){
		return solRight.get();
	}
	
	public void set(boolean on){
		solLeft.set(on);
		solRight.set(!on);
		lastSetTime = System.currentTimeMillis();
	}
	
	public long getLastSetTime(){
		return lastSetTime;
	}
	
}
