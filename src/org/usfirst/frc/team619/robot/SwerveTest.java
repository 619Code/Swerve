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
import org.usfirst.frc.team619.subsystems.drive.SwerveWheel;

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
	SwerveWheel left1;
	SwerveWheel left2;
	SwerveWheel right1;
	SwerveWheel right2;
	
	int autonomous_angle = 0;
	int autonomous_count = 0;
	
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
        
        left1 = new SwerveWheel(new Talon(0),new CANTalon(1),0.0,0);
        left2 = new SwerveWheel(new Talon(2),new CANTalon(3),0.0,0);
        right1 = new SwerveWheel(new Talon(1),new CANTalon(2),0.0,0);
        right2 = new SwerveWheel(new Talon(3),new CANTalon(4),0.0,0);
                
        //subsystems
        driveBase = new SwerveDriveBase( left1, right1, left2, right2, 32.0,32.0 );
    }

    /**
     *This function is called when autonomous is initialized
     */
    public void autonomousInit(){
    	threadManager.killAllThreads(); // DO NOT EVER REMOVE!!!
    	
    	// this is the right thing to do, but not until
    	// we have a sensor to detect "home"...
    	//left1.goToZero();
    	//left2.goToZero();
    	//right1.goToZero();
    	//right2.goToZero();
    	autonomous_angle = 0;
    	left1.setTargetAngle(autonomous_angle);
    	left2.setTargetAngle(autonomous_angle);
    	right1.setTargetAngle(autonomous_angle);
    	right2.setTargetAngle(autonomous_angle);

    	autonomous_angle = left1.getCurrentAngle();
    	SmartDashboard.putNumber("autonomous angle: ", autonomous_angle);

        left1.driveMotor.set(0.25);
        right1.driveMotor.set(-0.10);
        left2.driveMotor.set(0.25);
        right2.driveMotor.set(-0.10);
        
        SmartDashboard.putNumber("autonomous from: ", 0);
        SmartDashboard.putNumber("  autonomous to: ", 0);
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
    	if ( (autonomous_count % 100) == 0) {
    		// do this once every 100 times through
    		if ( autonomous_count == 1000 ) {
    			SmartDashboard.putNumber("autonomous from: ", autonomous_angle);
    			left1.setTargetAngle(autonomous_angle + 20);
    			left1.goToAngle( );
    			SmartDashboard.putNumber("  autonomous to: ", autonomous_angle+20);
    		}
    		autonomous_angle = (autonomous_angle + 2) % 360;
        	
//    		left1.setTargetAngle((double) autonomous_angle / 100.0);
//    		left1.goToAngle();
//    		left2.setTargetAngle(autonomous_angle);
//    		left2.goToAngle();
//    		right1.setTargetAngle(autonomous_angle);
//    		right1.goToAngle();
//    		right2.setTargetAngle(autonomous_angle);
//    		right2.goToAngle();
    	}
    	autonomous_count += 1;
    	SmartDashboard.putNumber("autonomous count: ", autonomous_count);
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
