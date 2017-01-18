package org.usfirst.frc.team619.subsystems;

import org.usfirst.frc.team619.hardware.Gamepad;
import org.usfirst.frc.team619.hardware.Joystick;

/**
 *
 * @author CaRobotics
 */
public class DriverStation {
    protected Joystick leftJoystick, rightJoystick;
    protected Gamepad leftController, rightController;

    public DriverStation(){
        leftController = new Gamepad(1);
        rightController = new Gamepad(2);
    }

    public DriverStation(int leftJoystickID, int rightJoystickID){
        leftJoystick = new Joystick(leftJoystickID);
        rightJoystick = new Joystick(rightJoystickID);
    }
    
    public Gamepad getLeftController() {
    	return leftController;
    }
    
    public Gamepad getRightController() {
    	return rightController;
    }

    public Joystick getLeftJoystick() {
        return leftJoystick;
    }

    public Joystick getRightJoystick() {
        return rightJoystick;
    }
}
