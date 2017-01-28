package org.usfirst.frc.team619.subsystems;

import org.usfirst.frc.team619.hardware.Gamepad;
import org.usfirst.frc.team619.hardware.Joystick;
import org.usfirst.frc.team619.hardware.XboxController;

/**
 *
 * @author CaRobotics
 */
public class DriverStation {
    protected Gamepad leftGamepad, rightGamepad;
    protected XboxController leftController, rightController;

    /**
     * For the controller mode X aka Gamepad F310 Controller. 
     * Each trigger is treated as an axis
     *
     */
    public DriverStation(){
        leftController = new XboxController(1);
        rightController = new XboxController(2);
    }

    /**
     * For the controller mode D aka Logitech Dual Action USB.
     * Each trigger is treated as a I/O button
     * 
     * @param leftJoystickID
     * @param rightJoystickID
     */
    public DriverStation(int leftJoystickID, int rightJoystickID){
        leftGamepad = new Gamepad(leftJoystickID);
        rightGamepad = new Gamepad(rightJoystickID);
    }
    
    public XboxController getLeftController() {
    	return leftController;
    }
    
    public XboxController getRightController() {
    	return rightController;
    }

    public Gamepad getLeftJoystick() {
        return leftGamepad;
    }

    public Gamepad getRightJoystick() {
        return rightGamepad;
    }
}
