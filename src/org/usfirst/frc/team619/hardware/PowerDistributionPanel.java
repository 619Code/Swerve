package org.usfirst.frc.team619.hardware;

public class PowerDistributionPanel {

	private edu.wpi.first.wpilibj.PowerDistributionPanel pdb;
	
	public PowerDistributionPanel(){
		pdb = new edu.wpi.first.wpilibj.PowerDistributionPanel();
	}
	
	public double getCurrent(int channel/*which spot on the pdb that you want to see the electrical current of*/){
		return pdb.getCurrent(channel);
	}
	
	public double getTemperature(){
		return pdb.getTemperature();
	}
	
	public double getTotalCurrent(){
		return pdb.getTotalCurrent();
	}
	
	public double getTotalEnergy(){
		return pdb.getTotalEnergy();
	}
	
	public double getTotalPower(){
		return pdb.getTotalPower();
	}
	
	public double getVoltage(){
		return pdb.getVoltage();
	}
	
}
