/*
 *
 *This class is here for basic reference of the main class code layout and to be
 *able to just copy and paste the base code.
 *
 *
 */

package org.usfirst.frc.team619.robot;

//import org.usfirst.frc.team619.hardware.CanTalon;
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

import com.ctre.CANTalon;
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
    CANTalon driveRightRear;
    CANTalon steerRightRear;
    CANTalon driveRightFront;
    CANTalon steerRightFront;
	/**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	System.out.println("\n");// shows code is working
        System.out.println("//////////////////////////////////////////////////////");
        System.out.println("// Cavalier Robotics                     TEAM 619   //");
        System.out.println("// 20XX Swerve                                      //");
        System.out.println("//////////////////////////////////////////////////////\n");

        //Create all robot subsystems (i.e. stuff from/for org.usfirst.frc.team619.subsystems)
        //If you are creating something not from/for org.usfirst.frc.team619.subsystems, YER DOING IT WRONG

        //threadManager
        threadManager = new ThreadManager();

        //driver station
        driverStation = new DriverStation();

        leftFront = new SwerveWheel( "leftFront", new CANTalon(2), new CANTalon(5), 0.0 );
        leftRear = new SwerveWheel( "leftRear", new CANTalon(1), new CANTalon(6), 0.0 );
        driveRightFront = new CANTalon(3);
        steerRightFront = new CANTalon(7);
        rightFront = new SwerveWheel( "rightFront", driveRightFront, steerRightFront, 0.0 );
        driveRightRear = new CANTalon(4);
        steerRightRear = new CANTalon(8);
        rightRear = new SwerveWheel( "rightRear", driveRightRear, steerRightRear, 0.0 );

        //subsystems
        driveBase = new SwerveDriveBase( leftFront, rightFront, leftRear, rightRear, 9.0, 9.0 );
        wheelCalculator = new SwerveCalc(9.0, 9.0);
    }

    /**
     *This function is called when autonomous is initialized
     */
    public void autonomousInit(){
    	threadManager.killAllThreads(); // DO NOT EVER REMOVE!!!
    	
    	int num = 4;
//    	reset(rightFront, rightRear, leftFront, leftRear); //ONLY USE WHEN MOTORS CAN TURN
		rightRear.zero();
		rightFront.zero();
		leftRear.zero();
		leftFront.zero();
    	
    	for(int i = 0; i < num; i++){
    		for(int j = 0; j <= 360; j++) {
    			rotateAll(rightFront, rightRear, leftFront, leftRear, j);
    			delay(5);
    		}
    	}
    	
    	System.out.println("Right Rear Position = " + steerRightRear.getEncPosition() + " error:" + steerRightRear.getError());
		System.out.println("Right Front Position = " + steerRightFront.getEncPosition() + " error:" + steerRightFront.getError());
    	
    	autonomous_count = 0;
		autonomous_start = System.currentTimeMillis();
    }

    /**
     * This function is called when teleop is initialized
     */
    public void teleopInit(){
    	threadManager.killAllThreads(); // DO NOT EVER REMOVE!!!

        driveThread = new SwerveDriveMappingThread(leftFront, leftRear, rightFront, rightRear, driveBase, driverStation, 15, threadManager);
        driveThread.start();
    }
    /**
     * This function is called periodically (about every 20 ms) during autonomous
     */
    public void DUMMYautonomousPeriodic(){ }
    public void autonomousPeriodic( ) {
    	//status(rightFront);
    	//System.out.println("Right Front Steer Position"+steerRightFront.getPosition());
//    	SmartDashboard.putNumber("Enc value", leftFront.getEncoderValue());
//    	SmartDashboard.putNumber("Angle", leftFront.getCurrentAngle());
//    	if(rightRear.getCurrentAngle() == 90) {
//    		SmartDashboard.putBoolean("Is 90", true);
//    	}
//    	autonomous_count += 1;
//    	if ( autonomous_count % 100 == 0 ) {
//    		strafe += 5;
//    		strafe = strafe % 360;
//    		SwerveCalcValue info = wheelCalculator.getRobotCentric( 0, sin(toRadians(strafe)), 0);
//    		System.out.println("angle>>> " + info.FRAngle());
//    	}
    }
    public void FIXEDANGLEautonomousPeriodic(){
    	autonomous_count += 1;
//    	if ( last_autonomous_pos != left1.rotateMotor.getPosition() ) {
////    		System.out.println("position>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + left1.rotateMotor.getPosition( ));
//    		last_autonomous_pos = left1.rotateMotor.getPosition( );
//    	}
//    	if ( autonomous_count == 300 ) {
//    		System.out.println("=======>>>> going to 180deg");
//    		leftFront.setTargetAngle(180);
//    		leftRear.setTargetAngle(180);
//    		rightFront.setTargetAngle(180);
//    		rightRear.setTargetAngle(180);
//
//    		leftFront.goToAngle( );
//    		leftRear.goToAngle( );
//    		rightFront.goToAngle( );
//    		rightRear.goToAngle( );
//
//    	}
//    	if ( autonomous_count == 600 ) {
//    		System.out.println("=======>>>> going to 270deg");
//    		leftFront.setTargetAngle(270);
//    		leftRear.setTargetAngle(270);
//    		rightFront.setTargetAngle(270);
//    		rightRear.setTargetAngle(270);
//
//    		leftFront.goToAngle( );
//    		leftRear.goToAngle( );
//    		rightFront.goToAngle( );
//    		rightRear.goToAngle( );
//    	}
//    	if ( autonomous_count == 900 ) {
//    		System.out.println("=======>>>> going to 0deg");
//    		leftFront.setTargetAngle(0);
//    		leftRear.setTargetAngle(0);
//    		rightFront.setTargetAngle(0);
//    		rightRear.setTargetAngle(0);
//
//    		leftFront.goToAngle( );
//    		leftRear.goToAngle( );
//    		rightFront.goToAngle( );
//    		rightRear.goToAngle( );
//    	}
//    	if ( autonomous_count == 1200 ) {
//    		System.out.println("=======>>>> going to 60deg");
//    		leftFront.setTargetAngle(60);
//    		leftRear.setTargetAngle(60);
//    		rightFront.setTargetAngle(60);
//    		rightRear.setTargetAngle(60);
//
//    		leftFront.goToAngle( );
//    		leftRear.goToAngle( );
//    		rightFront.goToAngle( );
//    		rightRear.goToAngle( );
//    	}
//    	if ( autonomous_count == 1500 ) {
//    		System.out.println("=======>>>> going to 0deg");
//    		leftFront.setTargetAngle(0);
//    		leftRear.setTargetAngle(0);
//    		rightFront.setTargetAngle(0);
//    		rightRear.setTargetAngle(0);
//
//    		leftFront.goToAngle( );
//    		leftRear.goToAngle( );
//    		rightFront.goToAngle( );
//    		rightRear.goToAngle( );
//    	}
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
*/
    	SmartDashboard.putNumber("leftFront", leftFront.getVoltage());
    	SmartDashboard.putNumber("leftRear", leftFront.getVoltage());
    	SmartDashboard.putNumber("rightFront", leftFront.getVoltage());
    	SmartDashboard.putNumber("rightRear", leftFront.getVoltage());
    	SmartDashboard.putNumber("NavX Yaw: ", driveBase.getYaw());
    	SmartDashboard.putBoolean("Field centric: ", driveBase.getFieldCentric());
    }
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

    public void testRotate(SwerveWheel wheel, String name, double angle){
    	//wheel.zero();
    	wheel.setTargetAngle(angle);
    	wheel.setSpeed(0.1);
    	wheel.goToAngle();
    	delay(1000);
    	wheel.setTargetAngle(0);
    	wheel.setSpeed(0.1);
    	wheel.goToAngle();
    	SmartDashboard.putNumber(name, wheel.getCurrentAngle());
    	delay(1000);
    }
    
    public void testSteer(CANTalon steer, String name, double position){
    	steer.set(position);
    }
    
    public void testDrive(SwerveWheel wheel, String name){
    	wheel.zero( );
    	wheel.setSpeed(0.5);
    	wheel.drive( );
    }
    
    public void delay(int milliseconds){
    	try {
    		Thread.sleep(milliseconds);
    	} catch(InterruptedException e) {
    		Thread.currentThread().interrupt();
    	}
    }
    
    public void reset(SwerveWheel rightFront, SwerveWheel rightRear, SwerveWheel leftFront, SwerveWheel leftRear){
    	SwerveWheel[] wheels = {rightFront, rightRear, leftFront, leftRear};
    	for(int i = 0; i < 4; i++){
    		wheels[i].setTargetAngle(0);
    		wheels[i].setSpeed(0.1);
    		wheels[i].goToAngle();
    		wheels[i].zero();
    	}
    }
    
    public void status(SwerveWheel wheel){
    	CANTalon steer = wheel.rotateMotor;
    	System.out.println("Description: " + steer.getDescription() + 
    						"\nControl Mode:" + steer.getControlMode() + 
    						"\nPosition = " + steer.getEncPosition() +
    						"\nFirmware: " + steer.GetFirmwareVersion() +
    						"\nLast error: " + steer.getLastError() +
    						"\nerror:" + steer.getError());
    }
    
    public void rotateAll(SwerveWheel rightFront, SwerveWheel rightRear, SwerveWheel leftFront, SwerveWheel leftRear, int angle){
    	SwerveWheel[] wheels = {rightFront, rightRear, leftFront, leftRear};
    	for(int i = 0; i < 4; i++){
    		wheels[i].setTargetAngle(angle);
    		wheels[i].setSpeed(0.1);
    		wheels[i].goToAngle();
    	}
    }
    
}
