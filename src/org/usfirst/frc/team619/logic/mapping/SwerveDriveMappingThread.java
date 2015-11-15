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
    protected SwerveDriveBase driveBase;
    protected DriverStation driverStation;
    private final static boolean DEBUG = true;

    public SwerveDriveMappingThread(DriverStation driverStation, int period, ThreadManager threadManager) {
        super(period, threadManager);
        this.driveBase = driveBase;
        this.driverStation = driverStation;
    }

    protected void cycle() {
        double scalePercent = driverStation.getLeftJoystick().getAxis(Joystick.Axis.AXIS_Z);
        
        if(scalePercent < 0.2){
            scalePercent = 0.2;
        }
        
        double xAxis = driverStation.getLeftJoystick().getAxis(Joystick.Axis.AXIS_X);
        double yAxis = driverStation.getLeftJoystick().getAxis(Joystick.Axis.AXIS_Y);
        double zTwist = driverStation.getRightJoystick().getAxis(Joystick.Axis.AXIS_TWIST);
        
        //gets percentages (numbers from -1 to 1) from the joystick's axes used for driving
        double LY = yAxis * scalePercent;
        double LX = xAxis * scalePercent;
        double RX = zTwist * scalePercent;
        
        driveBase.move(LY, LX, RX);
        
        
        if(DEBUG){
        //Write debug code    
        }
            
    }

}
