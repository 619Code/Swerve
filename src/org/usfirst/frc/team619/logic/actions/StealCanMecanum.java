package org.usfirst.frc.team619.logic.actions;

import org.usfirst.frc.team619.logic.ThreadManager;
import org.usfirst.frc.team619.subsystems.sensor.SensorBase;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class StealCanMecanum extends Action{

	private boolean isComplete = false;
	
	private SensorBase sensorBase;
	
	public StealCanMecanum(int waitForDependenciesPeriod, int runPeriod, ThreadManager threadManager, SensorBase sensorBase) {
		super(waitForDependenciesPeriod, runPeriod, threadManager);
		this.sensorBase = sensorBase;
	}

	@Override
	public boolean isComplete() {
		return isComplete;
	}

	@Override
	protected void cycle() {
		SmartDashboard.putNumber("Arm Sonic Sensor", sensorBase.getUltrasonicSensor(5).getDistanceCM());
		isComplete = true;
	}

}
