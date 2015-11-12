package org.usfirst.frc.team619.hardware;

import edu.wpi.first.wpilibj.PIDOutput;

public class PIDOutputObject implements PIDOutput{
	private double value;
	
	public void pidWrite(double output){
		value = output;
	}
	
	public double getPIDValue(){
		return value;
	}
	
}
