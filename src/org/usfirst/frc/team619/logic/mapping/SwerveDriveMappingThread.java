package org.usfirst.frc.team619.logic.mapping;

import org.usfirst.frc.team619.hardware.Joystick;
import org.usfirst.frc.team619.hardware.CANTalon;
import org.usfirst.frc.team619.hardware.Talon;
import org.usfirst.frc.team619.logic.RobotThread;
import org.usfirst.frc.team619.logic.ThreadManager;
import org.usfirst.frc.team619.subsystems.DriverStation;

/**
 * Map joystick values to motor values
 * @author CaRobotics
 */
public class SwerveDriveMappingThread extends RobotThread {
    protected Talon leftTalon;
    protected Talon rightTalon;
    protected Talon leftTalon2, rightTalon2;
    protected CANTalon leftTalonTurn;
    protected CANTalon rightTalonTurn;
    protected CANTalon leftTalonTurn2, rightTalonTurn2;
    protected DriverStation driverStation;
    private boolean firstError = true;
    private final static boolean DEBUG = true;

    public SwerveDriveMappingThread(DriverStation driverStation, int period, ThreadManager threadManager) {
        super(period, threadManager);
        this.driverStation = driverStation;
    }

    protected void cycle() {
        double scalePercent = driverStation.getLeftJoystick().getAxis(Joystick.Axis.AXIS_Z);
        
        if(scalePercent < 0.2){
            scalePercent = 0.2;
        }
        
        double xAxis = driverStation.getLeftJoystick().getAxis(Joystick.Axis.AXIS_X);
        double yAxis = driverStation.getLeftJoystick().getAxis(Joystick.Axis.AXIS_Y);
        //when turning Saitek Aviator Joystick that is the throttle axis as determined using the Joystick Explorer UI developed by FIRST
        //double xRightAxis = driverStation.getRightJoystick().getAxis(Joystick.Axis.AXIS_X);
        
        //gets percentages (numbers from -1 to 1) from the joystick's axes used for driving
        double drivePercent = yAxis * scalePercent;
        //double spinPercent = xRightAxis * scalePercent;
        double turnPercent = xAxis * scalePercent;
        
        
        //
        //TODO: figure out logic regarding each wheel when rotating
        //double leftPercent = spinPercent;
        //double rightPercent = spinPercent;
        //double leftPercent2 = spinPercent;
        //double rightPercent2 = spinPercent;
        
        try {
            if(xAxis > -0.3 && xAxis < 0.3 && yAxis > -0.3 && yAxis < 0.3){
                leftTalonTurn.set(0);
                rightTalonTurn.set(0);
                leftTalonTurn2.set(0);
                rightTalonTurn2.set(0);
            }else if(driverStation.getLeftJoystick().getButton(Joystick.Button.BUTTON5)){
            	leftTalonTurn.set(turnPercent);
            }else if(driverStation.getLeftJoystick().getButton(Joystick.Button.BUTTON6)){
            	rightTalonTurn.set(turnPercent);
            }else if(driverStation.getLeftJoystick().getButton(Joystick.Button.BUTTON7)){
            	leftTalonTurn2.set(turnPercent);
            }else if(driverStation.getLeftJoystick().getButton(Joystick.Button.BUTTON8)){
            	rightTalonTurn2.set(turnPercent);          	
            }else{
/*                turnBase.getLeftTalon().set(drivePercent * 0.25);
                turnBase.getRightTalon().set(-drivePercent * 0.25);
                turnBase.getLeftTalon2().set(drivePercent * 0.25);
                turnBase.getRightTalon2().set(-drivePercent * 0.25);
*/                leftTalonTurn.set(turnPercent);
                rightTalonTurn.set(-turnPercent);
                leftTalonTurn2.set(turnPercent);
                rightTalonTurn2.set(-turnPercent);
                }
        
            //Orientation
            //0-----1 ^
            //|     | |
            //|     |
            //2-----3
            
        } catch (Exception e) {
            if (firstError || DEBUG) {
                e.printStackTrace();
            }
        }
        
        if(DEBUG){
        //Write debug code    
        }
            
    }

}
