package org.usfirst.frc.team619.hardware;

import edu.wpi.first.wpilibj.PIDSource;

public class PIDInputObject implements PIDSource {
	
	private double value = 0;
	
	public void setValue(double input){
		value = input;
	}
	
	public double pidGet(){
		return value;
	}
}
