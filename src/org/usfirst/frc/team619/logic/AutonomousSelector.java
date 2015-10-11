package org.usfirst.frc.team619.logic;

import java.util.ArrayList;

import org.usfirst.frc.team619.logic.actions.Action;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class AutonomousSelector {

	private SendableChooser autoChooser;
	
	private Action chosen;
	private ArrayList<Action> autonomi;
	
	public AutonomousSelector(){
		autonomi = new ArrayList<>();
		autoChooser = new SendableChooser();
	}
	
	public void addAutonomous(String name, Action autonomous){
		autonomi.add(autonomous);
		autoChooser.addObject(name, autonomous);
	}
	
	public void startChoice(){
		chosen = (Action) autoChooser.getSelected();
		chosen.start();
	}
	
	public void startAutonomous(int index){
		autonomi.get(index).start();
	}
	
	public Action getChoice(){
		return (Action) autoChooser.getSelected();
	}
	
	public SendableChooser getAutoChooser(){
		return autoChooser;
	}
	
}
