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
	
	//Control
	
	
	int autonomous_angle = 0;
	int autonomous_count = 0;
	double autonomous_position = 0;
	int been_there_count = 0;
	boolean autonomous_initalized = false;
	long autonomous_start;
	double last_autonomous_pos = 0;
	
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
        
        left1 = new SwerveWheel( "left1", new Talon(0),new CANTalon(1),0.0,0);
        System.out.println(">>>----> pos=" + left1.rotateMotor.getPosition() + " / enc=" + left1.rotateMotor.getEncPosition( ));
        left2 = new SwerveWheel( "left2", new Talon(2),new CANTalon(3),0.0,0);
        right1 = new SwerveWheel( "right1", new Talon(1),new CANTalon(2),0.0,0);
        right2 = new SwerveWheel( "right2", new Talon(3),new CANTalon(4),0.0,0);
                
        //subsystems
        driveBase = new SwerveDriveBase( left1, right1, left2, right2, 21.0,32.0 );
    }

    /**
     *This function is called when autonomous is initialized
     */
    public void autonomousInit(){
    	threadManager.killAllThreads(); // DO NOT EVER REMOVE!!!
    	left1.zero( );
    	left2.zero( );
    	right1.zero( );
    	right2.zero( );
//    	System.out.println("starting zero of left1 >>>>>>>>>>>>>>>> " + left1.rotateMotor.getPosition( ));
//    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + left1.rotateMotor.getEncPosition( ));
//    	left1.zero( );
//    	System.out.println("finished zero of left1 >>>>>>>>>>>>>>>> " + left1.rotateMotor.getPosition( ));
//    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + left1.rotateMotor.getEncPosition( ));
//    	left1.rotateMotor.setPosition(0);
    	
    	
    	left1.setTargetAngle(45);
    	left2.setTargetAngle(45);
    	right1.setTargetAngle(45);
    	right2.setTargetAngle(45);
    	left1.goToAngle( );
    	left2.goToAngle( );
    	right1.goToAngle( );
    	left1.setSpeed(0.5);
    	left2.setSpeed(0.5);
    	right1.setSpeed(0);
    	right2.setSpeed(0);
    	left1.drive( );
    	left2.drive( );

    	autonomous_count = 0;
		autonomous_start = System.currentTimeMillis();
    	//left1.rotateMotor.set(left1.rotateMotor.getPosition( )+300);
    }

    /**
     * This function is called when teleop is initialized
     */
    public void teleopInit(){
    	threadManager.killAllThreads(); // DO NOT EVER REMOVE!!!
	
        driveThread = new SwerveDriveMappingThread(driveBase, driverStation, 15, threadManager);
        driveThread.start(); 
    }
    /**
     * This function is called periodically (about every 20 ms) during autonomous
     */
    public void autonomousPeriodic(){ 
    	autonomous_count += 1;
    	if ( last_autonomous_pos != left1.rotateMotor.getPosition() ) {
//    		System.out.println("position>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + left1.rotateMotor.getPosition( ));
    		last_autonomous_pos = left1.rotateMotor.getPosition( );
    	}
    	if ( autonomous_count == 300 ) {
//    		System.out.println("*******   going to -400   *******");
//    		left1.rotateMotor.set(-400);
    		System.out.println("=======>>>> going to 180deg");
    		left1.setTargetAngle(180);
    		left2.setTargetAngle(180);
    		right1.setTargetAngle(180);
    		right2.setTargetAngle(180);

    		left1.goToAngle( );
    		left2.goToAngle( );
    		right1.goToAngle( );
    		right2.goToAngle( );

    	}
    	if ( autonomous_count == 600 ) {
//    		System.out.println("*******   going to 400   *******");
//    		left1.rotateMotor.set(400);
    		System.out.println("=======>>>> going to 270deg");
    		left1.setTargetAngle(270);
    		left2.setTargetAngle(270);
    		right1.setTargetAngle(270);
    		right2.setTargetAngle(270);

    		left1.goToAngle( );
    		left2.goToAngle( );
    		right1.goToAngle( );
    		right2.goToAngle( );
    	}
    	if ( autonomous_count == 900 ) {
//    		System.out.println("*******   going to -400   *******");
//    		left1.rotateMotor.set(-400);
    		System.out.println("=======>>>> going to 0deg");
    		left1.setTargetAngle(0);
    		left2.setTargetAngle(0);
    		right1.setTargetAngle(0);
    		right2.setTargetAngle(0);

    		left1.goToAngle( );
    		left2.goToAngle( );
    		right1.goToAngle( );
    		right2.goToAngle( );
    	}
    	if ( autonomous_count == 1200 ) {
//    		System.out.println("*******   going to 800   *******");
//    		left1.rotateMotor.set(800);
    		System.out.println("=======>>>> going to 60deg");
    		left1.setTargetAngle(60);
    		left2.setTargetAngle(60);
    		right1.setTargetAngle(60);
    		right2.setTargetAngle(60);

    		left1.goToAngle( );
    		left2.goToAngle( );
    		right1.goToAngle( );
    		right2.goToAngle( );
    	}
    	if ( autonomous_count == 1500 ) {
//    		System.out.println("*******   going to -400   *******");
//    		left1.rotateMotor.set(-400);
    		System.out.println("=======>>>> going to 0deg");
    		left1.setTargetAngle(0);
    		left2.setTargetAngle(0);
    		right1.setTargetAngle(0);
    		right2.setTargetAngle(0);

    		left1.goToAngle( );
    		left2.goToAngle( );
    		right1.goToAngle( );
    		right2.goToAngle( );
    	}
    }

    public void autonomousPeriodic_DEBUG(){
    	try {
    		
    		SmartDashboard.putNumber("sensor value: ", left1.rotateMotor.getPosition( ));
    		if ( autonomous_position == left1.rotateMotor.getPosition( ) ) {
    			//System.out.println(">>>--been-there--> " + left1.rotateMotor.getEncPosition( ) + "     " + (System.currentTimeMillis() - autonomous_start));
    			been_there_count += 1;
    		} else {
    			autonomous_position = left1.rotateMotor.getPosition( );
    			/*if ( autonomous_initalized )*/ System.out.println(">>>--moving------> " + (System.currentTimeMillis() - autonomous_start) + "," + left1.rotateMotor.getPosition( ) );
    		}
    		if ( been_there_count > 200 ) {
    			autonomous_initalized = true;
    			been_there_count = 0;
    			autonomous_position = left1.rotateMotor.getPosition( );
    			double to_position = left1.rotateMotor.getPosition( ) + 1000.0;
    			left1.rotateMotor.set(to_position);
    			System.out.println(">>>--too-long----> " + left1.rotateMotor.getPosition( ) + " to " + to_position + "     " + (System.currentTimeMillis() - autonomous_start));
    		}

//    			left1.rotateMotor.set(600);
//    			Thread.sleep(400);
//    			left1.rotateMotor.set(900);
//    			Thread.sleep(400);
//    			left1.rotateMotor.set(1200);
//    			Thread.sleep(400);
    		
    	} catch(Exception e) { }

/***
    	//if ( autonomous_count == 500 ) {
        	SmartDashboard.putNumber("encoder value before: ", left1.getEncoderValue( ));
        	SmartDashboard.putNumber("current angle before: ", left1.getCurrentAngle( ));
        	left1.rotateMotor.set(autonomous_count % 500);
    		//left1.setTargetAngle(left1.getCurrentAngle( ) + 20);
    		//left1.goToAngle( );
        	SmartDashboard.putNumber(" encoder value after: ", left1.getEncoderValue( ));
        	SmartDashboard.putNumber(" current angle after: ", left1.getCurrentAngle( ));
    	//}
    	//autonomous_angle = (autonomous_angle + 2) % 360;
***/        	
//    		left1.setTargetAngle((double) autonomous_angle / 100.0);
//    		left1.goToAngle();
//    		left2.setTargetAngle(autonomous_angle);
//    		left2.goToAngle();
//    		right1.setTargetAngle(autonomous_angle);
//    		right1.goToAngle();
//    		right2.setTargetAngle(autonomous_angle);
//    		right2.goToAngle();

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
