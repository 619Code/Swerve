package org.usfirst.frc.team619.hardware;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class PIDInputObject implements PIDSource {
	
	private double value = 0;
	
	public void setValue(double input){
		value = input;
	}
	
	public double pidGet(){
		return value;
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return null;
	}
}
