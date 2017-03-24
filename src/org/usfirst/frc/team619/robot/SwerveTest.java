/*
 *
 *This class is here for basic reference of the main class code layout and to be
 *able to just copy and paste the base code.
 *
 *
 */

package org.usfirst.frc.team619.robot;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

import org.usfirst.frc.team619.hardware.AnalogUltrasonic;
import org.usfirst.frc.team619.logic.ThreadManager;
import org.usfirst.frc.team619.logic.mapping.AutoThread;
import org.usfirst.frc.team619.logic.mapping.TargetThread;
import org.usfirst.frc.team619.logic.mapping.SwerveDriveMappingThread;
import org.usfirst.frc.team619.subsystems.DriverStation;
import org.usfirst.frc.team619.subsystems.GripPipeline;
import org.usfirst.frc.team619.subsystems.drive.SwerveDriveBase;
import org.usfirst.frc.team619.subsystems.drive.SwerveWheel;
import org.usfirst.frc.team619.subsystems.drive.SwerveCalc;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
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
	DriverStation  driverStation;
	PowerDistributionPanel pdp;

	//Logic
	SwerveDriveMappingThread driveThread;
	AutoThread autoThread;

	//Subsystems
	SwerveDriveBase driveBase;

	//Hardware
	SwerveWheel leftFront;
	SwerveWheel leftRear;
	SwerveWheel rightFront;
	SwerveWheel rightRear;
	
    CANTalon driveRightRear;
    CANTalon steerRightRear;
    CANTalon driveRightFront;
    CANTalon steerRightFront;
    CANTalon driveLeftRear;
    CANTalon steerLeftRear;
    CANTalon driveLeftFront;
    CANTalon steerLeftFront;
    
    CANTalon climberMotor1;
    CANTalon climberMotor2;
    CANTalon intakeMotor;
    CANTalon outakeMotor;
    CANTalon gearOutakeMotor;
    
    AnalogUltrasonic leftUltrasonic, rightUltrasonic;
    DigitalInput switch1, switch2, switch3;
    
	//Control
	private SwerveCalc wheelCalculator;
	double strafe = 0;
	int currentAngle = 0;

	//vision
	public final int IMG_WIDTH = 160;
	public final int IMG_HEIGHT = 120;
	
	UsbCamera camera;
	UsbCamera topCamera;
	UsbCamera gearCamera;
	CvSink cvSink;
	CvSource outputStream;
	GripPipeline grip;
// pie
	int autonomous_angle = 0;
	int autonomous_count = 0;
	double autonomous_position = 0;
	int been_there_count = 0;
	boolean autonomous_initalized = false;
	long autonomous_start;
	double last_autonomous_pos = 0;
	
	//test
    private double testPeriodicSpeed = 0.1;
    private boolean testPeriodicForward = true;
    private int testPeriodicCount = 0;
	
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
        
        //Hardware
        rightUltrasonic = new AnalogUltrasonic(0);
//        leftUltrasonic = new AnalogUltrasonic(1);
        pdp = new PowerDistributionPanel(62);
        
        switch1 = new DigitalInput(0);
        switch2 = new DigitalInput(1);
        
//        //Practice Bot
//        driveLeftFront = new CANTalon(7);
//    	driveLeftFront.changeControlMode(TalonControlMode.Position);
//    	driveLeftFront.setFeedbackDevice(FeedbackDevice.QuadEncoder);
//    	driveLeftFront.enableControl();
//        steerLeftFront = new CANTalon(5);
//        leftFront = new SwerveWheel( "leftFront", driveLeftFront, steerLeftFront, 0.0 );
//        driveLeftRear = new CANTalon(6);
//        steerLeftRear = new CANTalon(4);
//        leftRear = new SwerveWheel( "leftRear", driveLeftRear, steerLeftRear, 0.0 );
//        driveRightFront = new CANTalon(0);
//        //driveRightFront.changeControlMode(TalonControlMode.Position);
//        steerRightFront = new CANTalon(2);
//        rightFront = new SwerveWheel( "rightFront", driveRightFront, steerRightFront, 0.0 );
//        driveRightRear = new CANTalon(1);
//        steerRightRear = new CANTalon(3);
//        rightRear = new SwerveWheel( "rightRear", driveRightRear, steerRightRear, 0.0 );
//        
//        climberMotor1 = new CANTalon(9);
//        climberMotor2 = new CANTalon(10);
//        intakeMotor = new CANTalon(8);
//        outakeMotor = new CANTalon(11);
//        gearOutakeMotor = new CANTalon(12);
//        //Unused = new CANTalon(11)
        
        
        //Real robot
        //CanTalons
        driveLeftFront = new CANTalon(1);
        steerLeftFront = new CANTalon(5);
        leftFront = new SwerveWheel( "leftFront", driveLeftFront, steerLeftFront, 0.0 );
        driveLeftRear = new CANTalon(12);
        steerLeftRear = new CANTalon(8);
        leftRear = new SwerveWheel( "leftRear", driveLeftRear, steerLeftRear, 0.0 );
        driveRightFront = new CANTalon(0);
        steerRightFront = new CANTalon(6);
        rightFront = new SwerveWheel( "rightFront", driveRightFront, steerRightFront, 0.0 );
        driveRightRear = new CANTalon(13);
        steerRightRear = new CANTalon(7);
        rightRear = new SwerveWheel( "rightRear", driveRightRear, steerRightRear, 0.0 );
        
        climberMotor1 = new CANTalon(2);
        climberMotor2 = new CANTalon(3);
        intakeMotor = new CANTalon(9);
        outakeMotor = new CANTalon(10);
        gearOutakeMotor = new CANTalon(4);
        //Unused = new CANTalon(11)

        //subsystems
        driveBase = new SwerveDriveBase( leftFront, rightFront, leftRear, rightRear, 17.0, 19.0 );
        wheelCalculator = new SwerveCalc(19.0, 17.0);
        
        //vision
        setUpCamera(camera, 0, true);
		cvSink = CameraServer.getInstance().getVideo();
		outputStream = CameraServer.getInstance().putVideo("v1.4", IMG_WIDTH, IMG_HEIGHT);
		
        setUpCamera(topCamera, 1, false);
        //setUpCamera(gearCamera, 3, false);
		
        //basicThread();
        //startCamera();
        //CameraServer.getInstance().startAutomaticCapture();
		
		//Vision thread
    }
    
    public void setUpCamera(UsbCamera camera, int index, boolean visionCamera){
        camera = CameraServer.getInstance().startAutomaticCapture(index);
		camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
		if(visionCamera){
			camera.setBrightness(0);
			camera.setExposureManual(0);
		}
    }
    
    /**
     * This function is called when auto is initialized
     */    
    public void autonomousInit() {
    	threadManager.killAllThreads(); // DO NOT EVER REMOVE!!!
    	TargetThread targetThread = new TargetThread(pdp, cvSink, outputStream, 3, threadManager);
    	if(switch1.get() || switch2.get())
    		autoThread = new AutoThread(switch1, switch2, targetThread, driveBase, 3, threadManager, gearOutakeMotor, rightUltrasonic);
//    	threadManager.addThread(autoThread);
//    	autoThread.run();
//    	driveBase.switchToGearCentric();
//    	new Thread(() -> {
//    		while(!Thread.interrupted()) {
//	    		double turn = 0;
//	    		double move = 0;
//	    		int m_size = 0;
//	    		int m_height = 0;
//	            synchronized (imgLock) {
//	            	m_height = height;
//	                turn = centerX;
//	                m_size = size;
//	            }
//	            if (m_size > 1) {
//	            	SmartDashboard.putNumber("Height", m_height);
//	            	if(m_height <= 25)
//	            		move = -0.15;
//	            	if(m_height >= 35)
//	            		move = 0.15;
//	            	
//					turn -= (IMG_WIDTH/2);
//					turn /= IMG_WIDTH/3;
//					if(turn > 0.15)
//						turn = 0.15;
//					if(turn < -0.15)
//						turn = -0.15;
//					if(Math.abs(turn) < 0.05)
//						turn = 0;
//	            }else {
//	            	turn = 0;
//	            	move = 0;
//	            }
//		    	SmartDashboard.putNumber("Actual Turn", turn);
//		    	SmartDashboard.putNumber("Move value", move);
//		    	//driveBase.move(0,0,turn);
//		    	driveBase.move(move, 0, turn);
//
//    		}	
//    	}).start();
    }

    public void startCamera() {
    	new Thread(() -> {
    		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
    		CvSource outputStream = CameraServer.getInstance().putVideo("Beep Boop Robot is Here", IMG_WIDTH, IMG_HEIGHT);
    	}).start();
    }
    
//    public void XbasicThread() {
//    	GripPipeline grip  = new GripPipeline();
//    	UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
//    	
//    	camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
//    	camera.setBrightness(0);
//    	camera.setExposureManual(0);
//    	
//    	CvSink cvSink = CameraServer.getInstance().getVideo();
//    	CvSource outputStream = CameraServer.getInstance().putVideo("v1.1", IMG_WIDTH, IMG_HEIGHT);
//    	
//    	new Thread(() -> {
//    		
//	    	Mat source = new Mat();
//	    	Mat output = new Mat();
//	    	long time = 0;
//	    	int value = 0;
//	    	try {
//	    		Thread.sleep(4000);
//	    	}catch(Exception e){}
//	    	
//	    	while(!Thread.interrupted()) {
//	    		time = System.currentTimeMillis();
//	    		cvSink.grabFrame(source);
//	    		if(source == null) {
//	    			System.out.println(" ERROR VERY BAD");
//	    			continue;
//	    		}
//	    		
//	    		//contour vars
//        		ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
//        		ArrayList<MatOfPoint> filterContoursContours = findContoursOutput;
//        		ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();
//        		double filterContoursMinArea = 0.0;
//        		double filterContoursMinPerimeter = 0.0;
//        		double filterContoursMinWidth = 0.0;
//        		double filterContoursMaxWidth = 1000;
//        		double filterContoursMinHeight = 10;
//        		double filterContoursMaxHeight = 1000;
//        		double[] filterContoursSolidity = {0, 100};
//        		double filterContoursMaxVertices = 1000000;
//        		double filterContoursMinVertices = 0;
//        		double filterContoursMinRatio = 0;
//        		double filterContoursMaxRatio = 1000;
//        		
//    			double[] hue = {40.0, 100.0};
//    			double[] sat = {200.0, 255.0};
//    			double[] val = {20.0, 90.0};
//        		
//    			//change colors 0_o
//    			int blue = 1;
//    			int blueMod = 1;
//    			
//    			int green = 1;
//    			int greenMod = 1;
//    			
//    			int red = 1;
//    			int redMod = 1;
//    			try {
//        			grip.hsvThreshold(source, hue, sat, val, output);
//        			grip.desaturate(output, output);
//        			grip.findContours(output, false, findContoursOutput);
//        			grip.filterContours(filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter, filterContoursMinWidth, 
//        						filterContoursMaxWidth, filterContoursMinHeight, filterContoursMaxHeight, filterContoursSolidity, 
//        						filterContoursMaxVertices, filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio, filterContoursOutput);
//                    
//        			r = Imgproc.boundingRect(filterContoursOutput.get(0));
//        			r2 = Imgproc.boundingRect(filterContoursOutput.get(1));
//        			
//        		}catch(Exception e) {}
//        			
//        		//change colors 0_o
//        		if(blue >= 255 || blue <= 0){
//        			blueMod *= -1;
//        		}
//        		blue += 8*blueMod;
//        		
//        		if(green >= 255 || green <= 0){
//        			greenMod *= -1;
//        		}
//        		green += 12*greenMod;
//        		
//        		if(red >= 255 || red <= 0){
//        			redMod *= -1;
//        		}
//        		red += 16*redMod;
//        		int size = 0;
//        		Rect r, r2;
//        		
//        		if(filterContoursOutput.size() != 0 && r != null && r2 != null) {
//        			synchronized(imgLock) {
//        				size = filterContoursOutput.size();
//        				int centerX = (r.x+r2.x)/2;
//        				int height = r.height;
//        			}
//        			Imgproc.rectangle(source, new Point(r.x, r.y), new Point(r.x+r.width, r.y+r.height), new Scalar(0, 0, 255));
//        			if(filterContoursOutput.size() > 1) {
//        				Imgproc.rectangle(source, new Point(r2.x, r2.y), new Point(r2.x+r2.width, r2.y+r2.height), new Scalar(0, 0, 255));
//        			}
//        			for(int i=0; i < filterContoursOutput.size(); i++) {
//        				Rect rect = Imgproc.boundingRect(filterContoursOutput.get(i));
//        				Imgproc.rectangle(source, new Point(rect.x, rect.y), new Point(rect.x+rect.width, rect.y+rect.height), new Scalar(0, 0, 255));
//        				int yesMan = rect.x + rect.width;
//        				System.out.println("Width: " + yesMan);
//        				System.out.println("Height: " + rect.height);
//        			}
//        		}
//        		Imgproc.putText(source, "v1.3", new Point(output.rows()/8,output.cols()/8), Core.FONT_ITALIC, 0.5, new Scalar(255,255,255), 1);
//        		time = System.currentTimeMillis() - time;
//        		double newTime = 1000/((double)time);
//        		Imgproc.putText(source, "" + (int)newTime + " fps", new Point(output.rows()/8,output.cols()/4), Core.FONT_ITALIC, 0.4, new Scalar(255,255,255), 1);
//            	
//        		outputStream.putFrame(source);
//	    	}
//    	}).start();
//    }

    /**
     * This function is called when teleop is initialized
     */
    public void teleopInit(){
    	threadManager.killAllThreads(); // DO NOT EVER REMOVE!!!
    	TargetThread targetThread = new TargetThread(pdp, cvSink, outputStream, 3, threadManager);
        driveThread = new SwerveDriveMappingThread(switch1, switch2, leftUltrasonic, rightUltrasonic, climberMotor1, climberMotor2, intakeMotor, 
        		outakeMotor, gearOutakeMotor, targetThread, pdp, driveBase, driverStation, 15, threadManager);
        driveThread.start();
        
    }
    
    /**
     * This function is called periodically (about every 20 ms) during autonomous
     */
    public void autonomousPeriodic( ) {
    	
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
    	System.out.println((driveRightFront.getPosition()));
    }
    
    

    public void testInit() {
    	threadManager.killAllThreads(); // DO NOT EVER REMOVE!!!
//    	driveRightFront.reverseOutput(true);
    	testPeriodicSpeed = 0.1;
    	testPeriodicForward = true;
    	testPeriodicCount = 0;
//    	while(this.isEnabled()) {
    	driveLeftFront.set(0);
    	delay(1000);
    	System.out.println("\n    0");
    	System.out.println("Get pos     " + driveLeftFront.getPosition());
    	System.out.println("Get Enc pos " +driveLeftFront.getEncPosition());
    	driveLeftFront.set(90);
    	delay(1000);
    	System.out.println("\n  90");
    	System.out.println("Get pos     " + driveLeftFront.getPosition());
    	System.out.println("Get Enc pos " +driveLeftFront.getEncPosition());
//    	}
//    	
//    	while(!Thread.interrupted()) {
//    		testPeriodicCount += 2;
//    		driveRightFront.set(testPeriodicCount);
//        	System.out.println("Enc Position = " + driveRightFront.getEncPosition());
//    		delay(150);
//    	}
//    	testRotate(rightFront, "right Steer", 90.0);
//    	testDrive(leftFront, "left Front");
//    	testRotate(leftFront, "left Steer", 90.0);
//    	testDrive(leftRear, "left Rear");
//    	testRotate(leftRear, "left Rear", 90.0);
//    	testDrive(rightRear, "right Rear");
//    	testRotate(rightRear, "right Rear", 90.0);
    	
//    	testPeriodicSpeed = 0.1;
//    	testPeriodicForward = true;
//    	testPeriodicCount = 0;
//		SmartDashboard.putBoolean("Limit switch", steerLeftFront.isFwdLimitSwitchClosed());
		
//    	rightRear.zero();
//		rightFront.zero();
//		leftRear.zero();
//		leftFront.zero();
//    	
//		driveLeftFront.setFeedbackDevice(CANTalon.FeedbackDevice.EncFalling);
//		leftFront.setSpeed(testPeriodicSpeed);
//    	leftFront.drive( );
    }
    /**
     * This function is called periodically during test mode
     */
      public void testPeriodic() {
//      	if(Math.abs(driveRightFront.getEncPosition()) > 10){
//      		System.out.println("ZEROING Current encoder position is "+driveRightFront.getEncPosition());
//      		driveRightFront.set(0);
//      		delay(1000);
//      	}
      }
//    public void testPeriodic() {
//    	testPeriodicCount += 1;
//    	//leftFront.autoZero();
//		SmartDashboard.putBoolean("Limit switch", steerLeftFront.isFwdLimitSwitchClosed());
//    	if (++testPeriodicCount < 200) {
//    		SmartDashboard.putNumber("test periodic speed: ", testPeriodicSpeed);
//    		SmartDashboard.putNumber("hall count: ", driveLeftFront.getEncPosition());
//    		
////    		if (testPeriodicSpeed >= 0.4) {
////    			testPeriodicSpeed = 0.4;
////    			testPeriodicForward = false;	
////    		} else if (testPeriodicSpeed <= 0.0) {
////    			testPeriodicSpeed = 0.0;
////    			testPeriodicForward = true;
////    		}
////    		if (testPeriodicForward)
////    			testPeriodicSpeed += 0.05;
////    		else
////    			testPeriodicSpeed -= 0.05;
////    		leftFront.setSpeed(testPeriodicSpeed);
////    		leftFront.drive( );
//    	}
//    	
//		currentAngle += 4;
//		leftFront.setTargetAngle(currentAngle);
//		leftFront.setSpeed(1);
//		leftFront.goToAngle();
//		//System.out.println(currentAngle);
//		System.out.println(steerLeftFront.isFwdLimitSwitchClosed());
//		if(testPeriodicCount%500 == 0){
//			delay(500);
////			int correctionAngle = currentAngle;
////			System.out.println(steerLeftFront.isFwdLimitSwitchClosed());
////			while(!steerLeftFront.isFwdLimitSwitchClosed()){
////				correctionAngle--;
////				leftFront.setTargetAngle(correctionAngle);
////				leftFront.goToAngle();
////				delay(20);
////			}
////			System.out.println("IT DOESNT ESCAPE");
////			delay(1000);
////			currentAngle = 100;
////			leftFront.setTargetAngle(currentAngle);
////			leftFront.goToAngle();
////			delay(500);
//			leftFront.autoZero();
//			delay(1000);
//			leftFront.setTargetAngle(100);
//			leftFront.goToAngle();
//			delay(2000);
//		}
//		delay(10);
//    }
    public void disabledInit(){
    	threadManager.killAllThreads(); // DO NOT EVER REMOVE!!!
    	CameraServer.getInstance().removeCamera("USB Camera 0");
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
    
    public void testEncoder(CANTalon wheel, String name, Double location){
    	//System.out.println("Encoder for " + name + " is at "+wheel.driveMotor.getEncPosition());
    	wheel.set(0);
    	//System.out.println("Encoder for " + name + " is at "+wheel.driveMotor.getEncPosition());
    	delay(500);
    	wheel.set(location);
    	System.out.println("Enc Position = " + wheel.getEncPosition());
    	//System.out.println("Encoder for " + name + " is at "+wheel.driveMotor.getEncPosition());
    	delay(500);
    	wheel.set(0);
    }
    
    public void testDrive(SwerveWheel wheel, String name){
    	//wheel.zero( );
    	wheel.setSpeed(0.3);
    	wheel.drive( );
    	delay(1000);
    	wheel.stop();
    	delay(1000);
    	wheel.setSpeed(-0.3);
    	wheel.drive();
    	delay(1000);
    	wheel.stop();
    	delay(1000);
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
