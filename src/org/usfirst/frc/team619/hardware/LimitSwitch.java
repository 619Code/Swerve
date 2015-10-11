package org.usfirst.frc.team619.hardware;

public class LimitSwitch {
	
	DigitalInput limitSwitch;
	
	public LimitSwitch(int channel){
		limitSwitch = new DigitalInput(channel);
	}
	
	public boolean get() {
        return limitSwitch.get();
    }
	
}
