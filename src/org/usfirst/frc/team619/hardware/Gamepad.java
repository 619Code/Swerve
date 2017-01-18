package org.usfirst.frc.team619.hardware;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class Gamepad {

	protected XboxController controller;
	/**
	 * Currently just a wrapper class. Adds nothing new to XboxController from wpilib
	 * This class exists in case we want to add something extra
	 */
	public Gamepad(int port) {
		controller = new XboxController(port);
	}
	
	/**
	* Get the X axis value of the controller.
	*
	* @param hand Side of controller whose value should be returned.
	* @return The X axis value of the controller.
	*/
	public double getX(Hand hand) {
		if(hand == Hand.kLeft) {
			return controller.getRawAxis(0);
		}else {
			return controller.getRawAxis(2);
		}
	}
	
	/**
	 * Get the Y axis value of the controller.
	 *
	 * @param hand Side of controller whose value should be returned.
	 * @return The Y axis value of the controller.
	 */
	public double getY(Hand hand) {
		if(hand == Hand.kLeft)
			return controller.getRawAxis(1);
		return controller.getRawAxis(3);
	}
	
	/**
	 * Read the value of the bumper button on the controller.
	 *
	 * @param hand Side of controller whose value should be returned.
	 * @return The state of the button.
	 */
	public boolean getBumper(Hand hand) {
		if(hand == Hand.kLeft)
			return controller.getRawButton(4);
		return controller.getRawButton(5);
	}
	
	/**
	 * Get the trigger axis value of the controller.
	 *
	 * @param hand Side of controller whose value should be returned.
	 * @return The trigger axis value of the controller.
	 */
	public double getTriggerAxis(Hand hand) {
		return controller.getTriggerAxis(hand);
	}
	
	/**
	 * Read the value of the A button on the controller.
	 *
	 * @return The state of the button.
	 */
	public boolean getAButton() {
		return controller.getRawButton(1);
	}
	
	/**
	 * Read the value of the B button on the controller.
	 *
	 * @return The state of the button.
	 */
	public boolean getBButton() {
		return controller.getRawButton(2);
	}
	
	/**
	 * Read the value of the X button on the controller.
	 *
	 * @return The state of the button.
	 */
	public boolean getXButton() {
		return controller.getRawButton(0);
	}
	
	/**
	 * Read the value of the Y button on the controller.
	 *
	 * @return The state of the button.
	 */
	public boolean getYButton() {
		return controller.getRawButton(3);
	}
	
	/**
	 * Read the value of the stick button on the controller.
	 *
	 * @param hand Side of controller whose value should be returned.
	 * @return The state of the button.
	 */
	public boolean getStickButton(Hand hand) {
		return controller.getStickButton(hand);
	}
	
	/**
	 * Read the value of the back button on the controller.
	 *
	 * @return The state of the button.
	 */
	public boolean getBackButton() {
		return controller.getRawButton(8);
	}
	
	/**
	 * Read the value of the start button on the controller.
	 *
	 * @return The state of the button.
	 */
	public boolean getStartButton() {
		return controller.getRawButton(9);
	}
	
	public int getPOV() {
		return controller.getPOV(0);
	}
}
