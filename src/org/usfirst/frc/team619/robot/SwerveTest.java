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
import org.usfirst.frc.team619.subsystems.drive.SwerveCalc;
import org.usfirst.frc.team619.subsystems.drive.SwerveCalcValue;

import static java.lang.Math.*;

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
	SwerveWheel leftFront;
	SwerveWheel leftRear;
	SwerveWheel rightFront;
	SwerveWheel rightRear;

	//Control
	private SwerveCalc wheelCalculator;
	double strafe = 0;


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

        leftFront = new SwerveWheel( "leftFront", new Talon(3), new CANTalon(2,true), 0.0 );
        leftRear = new SwerveWheel( "leftRear", new Talon(0), new CANTalon(3,true), 0.0 );
        rightFront = new SwerveWheel( "rightFront", new Talon(2), new CANTalon(1), 0.0 );
        rightRear = new SwerveWheel( "rightRear", new Talon(1), new CANTalon(4), 0.0 );

        //subsystems
        driveBase = new SwerveDriveBase( leftFront, rightFront, leftRear, rightRear, 21.0,32.0 );
        wheelCalculator = new SwerveCalc(21,32);
    }

    /**
     *This function is called when autonomous is initialized
     */
    public void autonomousInit(){
        threadManager.killAllThreads(); // DO NOT EVER REMOVE!!!
        leftFront.zero( );
        leftRear.zero( );
        rightFront.zero( );
        rightRear.zero( );
//    	System.out.println("starting zero of left1 >>>>>>>>>>>>>>>> " + left1.rotateMotor.getPosition( ));
//    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + left1.rotateMotor.getEncPosition( ));
//    	left1.zero( );
//    	System.out.println("finished zero of left1 >>>>>>>>>>>>>>>> " + left1.rotateMotor.getPosition( ));
//    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + left1.rotateMotor.getEncPosition( ));
//    	left1.rotateMotor.setPosition(0);


    	leftFront.setTargetAngle(45);
    	leftRear.setTargetAngle(45);
    	rightFront.setTargetAngle(45);
    	rightRear.setTargetAngle(45);
    	leftFront.goToAngle( );
    	leftRear.goToAngle( );
    	rightFront.goToAngle( );
    	rightRear.goToAngle( );

    	leftFront.setSpeed(0.5);
    	leftRear.setSpeed(0.5);
    	rightFront.setSpeed(0.5);
    	rightRear.setSpeed(0.5);

    	leftFront.drive( );
    	leftRear.drive( );
    	rightFront.drive( );
    	rightRear.drive( );

    	autonomous_count = 0;
		autonomous_start = System.currentTimeMillis();
    }

    /**
     * This function is called when teleop is initialized
     */
    public void teleopInit(){
    	threadManager.killAllThreads(); // DO NOT EVER REMOVE!!!

        leftFront.zero( );
        leftRear.zero( );
        rightFront.zero( );
        rightRear.zero( );
        driveThread = new SwerveDriveMappingThread(leftFront, leftRear, rightFront, rightRear, driveBase, driverStation, 15, threadManager);
        driveThread.start();
    }
    /**
     * This function is called periodically (about every 20 ms) during autonomous
     */
    public void DUMMYautonomousPeriodic(){ }
    public void autonomousPeriodic( ) {
    	autonomous_count += 1;
    	if ( autonomous_count % 100 == 0 ) {
    		strafe += 5;
    		strafe = strafe % 360;
    		SwerveCalcValue info = wheelCalculator.getRobotCentric( 0, sin(toRadians(strafe)), 0);
    		System.out.println("angle>>> " + info.FRAngle());
    	}
    }
    public void FIXEDANGLEautonomousPeriodic(){
    	autonomous_count += 1;
//    	if ( last_autonomous_pos != left1.rotateMotor.getPosition() ) {
////    		System.out.println("position>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + left1.rotateMotor.getPosition( ));
//    		last_autonomous_pos = left1.rotateMotor.getPosition( );
//    	}
    	if ( autonomous_count == 300 ) {
    		System.out.println("=======>>>> going to 180deg");
    		leftFront.setTargetAngle(180);
    		leftRear.setTargetAngle(180);
    		rightFront.setTargetAngle(180);
    		rightRear.setTargetAngle(180);

    		leftFront.goToAngle( );
    		leftRear.goToAngle( );
    		rightFront.goToAngle( );
    		rightRear.goToAngle( );

    	}
    	if ( autonomous_count == 600 ) {
    		System.out.println("=======>>>> going to 270deg");
    		leftFront.setTargetAngle(270);
    		leftRear.setTargetAngle(270);
    		rightFront.setTargetAngle(270);
    		rightRear.setTargetAngle(270);

    		leftFront.goToAngle( );
    		leftRear.goToAngle( );
    		rightFront.goToAngle( );
    		rightRear.goToAngle( );
    	}
    	if ( autonomous_count == 900 ) {
    		System.out.println("=======>>>> going to 0deg");
    		leftFront.setTargetAngle(0);
    		leftRear.setTargetAngle(0);
    		rightFront.setTargetAngle(0);
    		rightRear.setTargetAngle(0);

    		leftFront.goToAngle( );
    		leftRear.goToAngle( );
    		rightFront.goToAngle( );
    		rightRear.goToAngle( );
    	}
    	if ( autonomous_count == 1200 ) {
    		System.out.println("=======>>>> going to 60deg");
    		leftFront.setTargetAngle(60);
    		leftRear.setTargetAngle(60);
    		rightFront.setTargetAngle(60);
    		rightRear.setTargetAngle(60);

    		leftFront.goToAngle( );
    		leftRear.goToAngle( );
    		rightFront.goToAngle( );
    		rightRear.goToAngle( );
    	}
    	if ( autonomous_count == 1500 ) {
    		System.out.println("=======>>>> going to 0deg");
    		leftFront.setTargetAngle(0);
    		leftRear.setTargetAngle(0);
    		rightFront.setTargetAngle(0);
    		rightRear.setTargetAngle(0);

    		leftFront.goToAngle( );
    		leftRear.goToAngle( );
    		rightFront.goToAngle( );
    		rightRear.goToAngle( );
    	}
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
