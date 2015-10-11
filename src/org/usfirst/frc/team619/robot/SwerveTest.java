/*
 * 
 *This class is here for basic reference of the main class code layout and to be
 *able to just copy and paste the base code. 
 * 
 * 
 */

package org.usfirst.frc.team619.robot;

import org.usfirst.frc.team619.hardware.CANTalon;
import org.usfirst.frc.team619.hardware.Talon;
import org.usfirst.frc.team619.logic.ThreadManager;
import org.usfirst.frc.team619.logic.mapping.SwerveDriveMappingThread;
import org.usfirst.frc.team619.subsystems.DriverStation;
import org.usfirst.frc.team619.subsystems.drive.SwerveDriveBase;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class SwerveTest extends IterativeRobot {
    
	//declare all variables and objects here
	
	//Basics
	ThreadManager threadManager;
	DriverStation driverStation;

	//Logic
	SwerveDriveMappingThread driveThread;
	
	//Subsystems
	SwerveDriveBase driveBase;
	
	//Hardware	
	CANTalon turnLeft;
	CANTalon turnRight;
	CANTalon turnLeft2;
	CANTalon turnRight2;
	
	Talon driveLeft;
	Talon driveRight;
	Talon driveLeft2;
	Talon driveRight2;
	
	/**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	System.out.println("\n");// shows code is working
        System.out.println("//////////////////////////////////////////////////////");
        System.out.println("// Cavalier Robotics                     TEAM 619   //");
        System.out.println("// 2015 Jenga                                       //");
        System.out.println("//////////////////////////////////////////////////////\n");
        
        //Create all robot subsystems (i.e. stuff from/for org.usfirst.frc.team619.subsystems)
        //If you are creating something not from/for org.usfirst.frc.team619.subsystems, YER DOING IT WRONG
        
        //threadManager
        threadManager = new ThreadManager();
        
        //driver station
        driverStation = new DriverStation(1, 2);
        
        //TalonCan        
        turnLeft = new CANTalon(1);
        turnRight = new CANTalon(2);
        turnLeft2 = new CANTalon(3);
        turnRight2 = new CANTalon(4);
        
        //Talon
        driveLeft = new Talon(0);
        driveRight = new Talon(1);
        driveLeft2 = new Talon(2);
        driveRight2 = new Talon(3);
        
        //subsystems
        driveBase = new SwerveDriveBase( driveLeft, driveRight, driveLeft2, driveRight2,
                                         turnLeft, turnRight, turnLeft2, turnRight2 );
    }

    /**
     *This function is called when autonomous is initialized
     */
    public void autonomousInit(){
    	threadManager.killAllThreads(); // DO NOT EVER REMOVE!!!
        turnLeft.set(0.25);
        turnRight.set(0.25);
        turnLeft2.set(0.25);
        turnRight2.set(0.25);
        
        driveLeft.set(0.25);
        driveRight.set(0.25);
        driveLeft2.set(0.25);
        driveRight2.set(0.25);
    }

    /**
     * This function is called when teleop is initialized
     */
    public void teleopInit(){
    	threadManager.killAllThreads(); // DO NOT EVER REMOVE!!!
	
        driveThread = new SwerveDriveMappingThread(driverStation, 15, threadManager);
        driveThread.start(); 
    }
    /**
     * This function is called periodically (about every 20 ms) during autonomous
     */
    public void autonomousPeriodic(){

    }
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
//        SmartDashboard.putNumber("FLTurn: ", driveBase.getLeftTurn().getSpeed());
//        SmartDashboard.putNumber("FRTurn: ", driveBase.getRightTurn().getSpeed());
//        SmartDashboard.putNumber("BLTurn: ", driveBase.getLeftTurn2().getSpeed());
//        SmartDashboard.putNumber("BRTurn: ", driveBase.getRightTurn2().getSpeed());
/*        SmartDashboard.putNumber("FL: ", driveBase.getLeftTalon().getSpeed());
        SmartDashboard.putNumber("FR: ", driveBase.getRightTalon().getSpeed());
        SmartDashboard.putNumber("BL: ", driveBase.getLeftTalon2().getSpeed());
        SmartDashboard.putNumber("BR: ", driveBase.getRightTalon2().getSpeed());
*/    }
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    public void disabledInit(){
    	threadManager.killAllThreads(); // DO NOT EVER REMOVE!!!
    }
    
    public void disabledPeriodic(){}
    
    public void disabledContinuous(){}
    
}
