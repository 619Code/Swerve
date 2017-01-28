/*
 *
 *This class is here for basic reference of the main class code layout and to be
 *able to just copy and paste the base code.
 *
 *
 */

package org.usfirst.frc.team619.robot;

import org.opencv.core.Core;
//vision 1/21/17
import java.util.ArrayList;

import javafx.scene.Camera;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.VisionThread;

import org.usfirst.frc.team619.logic.ThreadManager;
import org.usfirst.frc.team619.logic.mapping.SwerveDriveMappingThread;
import org.usfirst.frc.team619.subsystems.DriverStation;
import org.usfirst.frc.team619.subsystems.GripPipeline;
import org.usfirst.frc.team619.subsystems.drive.SwerveDriveBase;
import org.usfirst.frc.team619.subsystems.drive.SwerveWheel;
import org.usfirst.frc.team619.subsystems.drive.SwerveCalc;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.CANTalon;
import com.sun.prism.paint.Color;
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
	
    CANTalon driveRightRear;
    CANTalon steerRightRear;
    CANTalon driveRightFront;
    CANTalon steerRightFront;
    CANTalon driveLeftRear;
    CANTalon steerLeftRear;
    CANTalon driveLeftFront;
    CANTalon steerLeftFront;
    GripPipeline grip;

	//Control
	private SwerveCalc wheelCalculator;
	double strafe = 0;

	//vision
	private static final int IMG_WIDTH = 160;
	private static final int IMG_HEIGHT = 120;
	
	private VisionThread visionThread;
	private double centerX = 0;
	private int size = 0;
	
	private Rect r;
	
	private final Object imgLock = new Object();

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
        System.out.println("// 20XX Swerve                                      //");
        System.out.println("//////////////////////////////////////////////////////\n");

        //Create all robot subsystems (i.e. stuff from/for org.usfirst.frc.team619.subsystems)
        //If you are creating something not from/for org.usfirst.frc.team619.subsystems, YER DOING IT WRONG 

        //threadManager
        threadManager = new ThreadManager();

        //driver station
        driverStation = new DriverStation();
        
        driveLeftFront = new CANTalon(2);
        steerLeftFront = new CANTalon(5);
        leftFront = new SwerveWheel( "leftFront", driveLeftFront, steerLeftFront, 0.0 );
        driveLeftRear = new CANTalon(1);
        steerLeftRear = new CANTalon(6);
        leftRear = new SwerveWheel( "leftRear", driveLeftRear, steerLeftRear, 0.0 );
        driveRightFront = new CANTalon(3);
        steerRightFront = new CANTalon(7);
        rightFront = new SwerveWheel( "rightFront", driveRightFront, steerRightFront, 0.0 );
        driveRightRear = new CANTalon(4);
        steerRightRear = new CANTalon(8);
        rightRear = new SwerveWheel( "rightRear", driveRightRear, steerRightRear, 0.0 );

        //subsystems
        driveBase = new SwerveDriveBase( leftFront, rightFront, leftRear, rightRear, 9.0, 9.0 );
        wheelCalculator = new SwerveCalc(9.0, 9.0);
        
        //vision
        //filteredVisionInit();
        //actuallyTheBestWorkingIdea();
        System.out.println("Calling Soren");
        sorenIdea();
    }

    /**
     * This function is called to initialize vision
     */
//    public void visionInit(){
//    	CameraServer cam = CameraServer.getInstance();
//    	UsbCamera camera = cam.startAutomaticCapture();
//    	camera.setBrightness(0);
//    	
//        visionThread = new VisionThread(camera, new GripPipeline(), pipeline -> {
//            if (!pipeline.filterContoursOutput().isEmpty()) {
//                r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
//                synchronized (imgLock) {
//                	
//                    centerX = r.x + (r.width / 2);
//                    width = r.width;
//                    size = pipeline.findContoursOutput().size();
//                }
//                SmartDashboard.putNumber("Targets Found", pipeline.filterContoursOutput().size());
//            }else
//            	SmartDashboard.putNumber("Targets Found", 0);
//        });
//    	visionThread.start();
//    }
    
    public void sorenIdea() {
    	grip = new GripPipeline();
  
    	new Thread(() -> {
    		System.out.println("Inside sorenIdea");
            UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
            camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
            System.out.println("After set Resolution");
            //camera.setFPS(30);
            CvSink cvSink = CameraServer.getInstance().getVideo();
            CvSource outputStream = CameraServer.getInstance().putVideo("Wilson", IMG_WIDTH, IMG_HEIGHT);
            
            Mat source = new Mat();
            Mat output = new Mat();
            long time = 0;
            
            while(!Thread.interrupted()) {
            	time = System.currentTimeMillis();
                cvSink.grabFrame(source);
                //Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
                //contour vars
        		ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
        		ArrayList<MatOfPoint> filterContoursContours = findContoursOutput;
        		ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();
        		double filterContoursMinArea = 100.0;
        		double filterContoursMinPerimeter = 200.0;
        		double filterContoursMinWidth = 0.0;
        		double filterContoursMaxWidth = 1000;
        		double filterContoursMinHeight = 0;
        		double filterContoursMaxHeight = 1000;
        		double[] filterContoursSolidity = {0, 100};
        		double filterContoursMaxVertices = 1000000;
        		double filterContoursMinVertices = 0;
        		double filterContoursMinRatio = 0;
        		double filterContoursMaxRatio = 1000;
        		
    			double[] hue = {40.0, 100.0};
    			double[] sat = {0.0, 255.0};
    			double[] val = {200.0, 255.0};
        		
    			//change colors 0_o
    			int blue = 1;
    			int blueMod = 1;
    			
    			int green = 1;
    			int greenMod = 1;
    			
    			int red = 1;
    			int redMod = 1;
    			
        			grip.hsvThreshold(source, hue, sat, val, output);
        			grip.desaturate(output, output);
        			grip.findContours(output, false, findContoursOutput);
        			grip.filterContours(filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter, filterContoursMinWidth, 
        						filterContoursMaxWidth, filterContoursMinHeight, filterContoursMaxHeight, filterContoursSolidity, 
        						filterContoursMaxVertices, filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio, filterContoursOutput);
                    
        			try{
        			r = Imgproc.boundingRect(filterContoursOutput.get(0));
        			
        			}catch(Exception e) {}
        			
        			//change colors 0_o
        			if(blue >= 255 || blue <= 0){
        				blueMod *= -1;
        			}
        			blue += 8*blueMod;
        			
        			if(green >= 255 || green <= 0){
        				greenMod *= -1;
        			}
        			green += 12*greenMod;
        			
        			if(red >= 255 || red <= 0){
        				redMod *= -1;
        			}
        			red += 16*redMod;
        			
        			if(filterContoursOutput.size() != 0) {
//        				turn = (r.x+(r.width/2))-(IMG_WIDTH/2);
//        				turn /= IMG_WIDTH;
//        				if(turn > 0.2)
//        					turn = 0.2;
//        				if(turn < -0.2)
//        					turn = -0.2;
        				Imgproc.rectangle(source, new Point(r.x, r.y), new Point(r.x+r.width, r.y+r.height), new Scalar(0, 0, 255));
        			}
        		Imgproc.putText(source, "v1.3", new Point(output.rows()/8,output.cols()/8), Core.FONT_ITALIC, 0.5, new Scalar(255,255,255), 1);
        		time = System.currentTimeMillis() - time;
        		double newTime = 1000/((double)time);
        		Imgproc.putText(source, "" + (int)newTime + " fps", new Point(output.rows()/8,output.cols()/4), Core.FONT_ITALIC, 0.4, new Scalar(255,255,255), 1);
            		
                outputStream.putFrame(source);
            }
           
    	}).start();
    }
    
    
    public void actuallyTheBestWorkingIdea() {
    	grip = new GripPipeline();
    	new Thread(() -> {
    		CameraServer camera = CameraServer.getInstance();
    		UsbCamera cam = camera.startAutomaticCapture();
    		cam.setFPS(5);
    		CvSink cvSink = camera.getVideo();
    		int identity = 1;
    		CvSource outputStream = camera.putVideo(identity+"", 160, 120);
    		
    		Mat source = new Mat();
    		Mat output = new Mat();
    		
    		//contour vars
    		ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
    		ArrayList<MatOfPoint> filterContoursContours = findContoursOutput;
    		ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();
    		double filterContoursMinArea = 100.0;
    		double filterContoursMinPerimeter = 200.0;
    		double filterContoursMinWidth = 0.0;
    		double filterContoursMaxWidth = 1000;
    		double filterContoursMinHeight = 0;
    		double filterContoursMaxHeight = 1000;
    		double[] filterContoursSolidity = {0, 100};
    		double filterContoursMaxVertices = 1000000;
    		double filterContoursMinVertices = 0;
    		double filterContoursMinRatio = 0;
    		double filterContoursMaxRatio = 1000;
    		
			double[] hue = {40.0, 100.0};
			double[] sat = {0.0, 255.0};
			double[] val = {200.0, 255.0};
    		
			//change colors 0_o
			int brown = 1;
			int brownMod = 1;
			
			int green = 1;
			int greenMod = 1;
			
			int red = 1;
			int redMod = 1;
			long time = 0;
//			
    		while(!Thread.interrupted()){
    			time = System.currentTimeMillis();
    			cvSink.grabFrame(source);
//    			grip.hsvThreshold(source, hue, sat, val, output);
//    			grip.desaturate(output, output);
//    			grip.findContours(output, false, findContoursOutput);
//    			grip.filterContours(filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter, filterContoursMinWidth, 
//    						filterContoursMaxWidth, filterContoursMinHeight, filterContoursMaxHeight, filterContoursSolidity, 
//    						filterContoursMaxVertices, filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio, filterContoursOutput);
//                
//    			try{
//    			r = Imgproc.boundingRect(filterContoursOutput.get(0));
//    			
//    			}catch(Exception e) {}
//    			
//    			//change colors 0_o
//    			if(brown >= 255 || brown <= 0){
//    				brownMod *= -1;
//    			}
//    			brown += 8*brownMod;
//    			
//    			if(green >= 255 || green <= 0){
//    				greenMod *= -1;
//    			}
//    			green += 12*greenMod;
//    			
//    			if(red >= 255 || red <= 0){
//    				redMod *= -1;
//    			}
//    			red += 16*redMod;
//    			
//    			if(filterContoursOutput.size() != 0) {
////    				turn = (r.x+(r.width/2))-(IMG_WIDTH/2);
////    				turn /= IMG_WIDTH;
////    				if(turn > 0.2)
////    					turn = 0.2;
////    				if(turn < -0.2)
////    					turn = -0.2;
//    				Imgproc.rectangle(source, new Point(r.x, r.y), new Point(r.x+r.width, r.y+r.height), new Scalar(brown, green, red));
//    			}
//                synchronized (imgLock) {
//                	
//                    centerX = r.x + (r.width / 2);
//                    size = findContoursOutput.size();
//                }
    			//Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
    			time = System.currentTimeMillis() - time;
    			System.out.println(time);
    			outputStream.putFrame(source);
    		}
    	}).start();
    }
    
//    public void bestIdea() {
//    	grip = new GripPipeline();
//    	new Thread(() -> {
//    		CameraServer camera = CameraServer.getInstance();
//    		UsbCamera cam = camera.startAutomaticCapture();
//    		CvSink cvSink = camera.getVideo();
//    		CvSource outputStream = camera.putVideo("Working Frame", 160, 120);
//    		
//    		Mat source = new Mat();
//    		Mat output = new Mat();
//    		
//    		ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
//    		ArrayList<MatOfPoint> filterContoursContours = findContoursOutput;
//    		ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();
//    		
//    		//contour vars
//    		double filterContoursMinArea = 100.0;
//    		double filterContoursMinPerimeter = 200.0;
//    		double filterContoursMinWidth = 0.0;
//    		double filterContoursMaxWidth = 1000;
//    		double filterContoursMinHeight = 0;
//    		double filterContoursMaxHeight = 1000;
//    		double[] filterContoursSolidity = {0, 100};
//    		double filterContoursMaxVertices = 1000000;
//    		double filterContoursMinVertices = 0;
//    		double filterContoursMinRatio = 0;
//    		double filterContoursMaxRatio = 1000;
//    		
//			double[] hue = {40.0, 100.0};
//			double[] sat = {0.0, 255.0};
//			double[] val = {200.0, 255.0};
//			
//    		while(!Thread.interrupted()) {
//    			cvSink.grabFrame(source);
//    			grip.hsvThreshold(source, hue, sat, val, output);
//    			grip.desaturate(output, output);
//    			grip.findContours(output, false, findContoursOutput);
//    			grip.filterContours(filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter, filterContoursMinWidth, 
//    						filterContoursMaxWidth, filterContoursMinHeight, filterContoursMaxHeight, filterContoursSolidity, 
//    						filterContoursMaxVertices, filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio, filterContoursOutput);
//    			try{
//    			r = Imgproc.boundingRect(filterContoursOutput.get(0));
//    			}catch(Exception e) {}
//    			Imgproc.rectangle(source, new Point(r.x, r.y), new Point(r.x+r.width, r.y+r.height), new Scalar(0, 0, 255));
////    			output = source;
////    			Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
//    			outputStream.putFrame(source);
//    		}
//    	}).start();
//    }
//    
//    public void filteredVisionInit() {
//    	grip = new GripPipeline();
//    	new Thread(() -> {
////    		UsbCamera cam = CameraServer.getInstance().startAutomaticCapture(); 
//    		CameraServer cam = CameraServer.getInstance();
//    		UsbCamera camera = cam.startAutomaticCapture();
//    		
////    		camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
//    		CvSink cvSink = CameraServer.getInstance().getVideo();
//    		CvSource outputStream = CameraServer.getInstance().putVideo("Frame", IMG_WIDTH, IMG_HEIGHT);
//    	
//    		Mat source = new Mat();
//    		Mat output = new Mat();
//    		
//    		ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
//    		ArrayList<MatOfPoint> filterContoursContours = findContoursOutput;
//    		ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();
//    		
//    		//contour vars
//    		double filterContoursMinArea = 100.0;
//    		double filterContoursMinPerimeter = 200.0;
//    		double filterContoursMinWidth = 0.0;
//    		double filterContoursMaxWidth = 1000;
//    		double filterContoursMinHeight = 0;
//    		double filterContoursMaxHeight = 1000;
//    		double[] filterContoursSolidity = {0, 100};
//    		double filterContoursMaxVertices = 1000000;
//    		double filterContoursMinVertices = 0;
//    		double filterContoursMinRatio = 0;
//    		double filterContoursMaxRatio = 1000;
//    		
//			double[] hue = {40.0, 100.0};
//			double[] sat = {0.0, 255.0};
//			double[] val = {200.0, 255.0};
//			
//    		while(!Thread.interrupted()) {
//    			cvSink.grabFrame(source);
//    			//Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
//    			grip.hsvThreshold(source, hue, sat, val, output);
//    			grip.desaturate(output, output);
//    			grip.findContours(output, false, findContoursOutput);
//    			grip.filterContours(filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter, filterContoursMinWidth, 
//    						filterContoursMaxWidth, filterContoursMinHeight, filterContoursMaxHeight, filterContoursSolidity, 
//    						filterContoursMaxVertices, filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio, filterContoursOutput);
//    			try{
//    			r = Imgproc.boundingRect(filterContoursOutput.get(0));
//    			}catch(Exception e) {}
//    			Imgproc.rectangle(source, new Point(r.x, r.y), new Point(r.x+r.width, r.y+r.height), new Scalar(0, 0, 255));
//    			outputStream.putFrame(source);
//    		}
//    	}).start();
//    }
    
    private void error() {
    	System.out.println(" ERROR NOT HERE ");
    }
    
    /**
     *This function is called when autonomous is initialized
     */
    public void autonomousInit(){
    	threadManager.killAllThreads(); // DO NOT EVER REMOVE!!!
    	new Thread(() -> {
    		while(!Thread.interrupted()) {
	    		double turn = 0;
	    		int m_size = 0;
	            synchronized (imgLock) {
	            	
	                turn = centerX;
	                m_size = size;
	            }
	            if (size != 0) {
					turn -= (IMG_WIDTH/2);
					turn /= IMG_WIDTH;
					if(turn > 0.2)
						turn = 0.2;
					if(turn < -0.2)
						turn = -0.2;
	            }else {
	            	turn = 0;
	            }
		    	SmartDashboard.putNumber("Actual Turn", turn);
		    	driveBase.move(0,0,turn);
    		}
	    }).start();
	    	
    	
    	
    	//drive test
//    	int num = 4;
////    	reset(rightFront, rightRear, leftFront, leftRear); //ONLY USE WHEN MOTORS CAN TURN
//		rightRear.zero();
//		rightFront.zero();
//		leftRear.zero();
//		leftFront.zero();
//    	
//    	for(int i = 0; i < num; i++){
//    		for(int j = 0; j <= 360; j++) {
//    			rotateAll(rightFront, rightRear, leftFront, leftRear, j);
//    			delay(5);
//    		}
//    	}
//    	
////    	System.out.println("Right Rear Position = " + steerRightRear.getEncPosition() + " error:" + steerRightRear.getError());
////		System.out.println("Right Front Position = " + steerRightFront.getEncPosition() + " error:" + steerRightFront.getError());
//    	
//    	autonomous_count = 0;
//		autonomous_start = System.currentTimeMillis();
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
    	SmartDashboard.putNumber("NavX Yaw: ", driveBase.getYaw());
    	SmartDashboard.putBoolean("Field centric: ", driveBase.getFieldCentric());
    }
    
    
    private double testPeriodicSpeed = 0.1;
    private boolean testPeriodicForward = true;
    private int testPeriodicCount = 0;
    public void testInit() {
    	threadManager.killAllThreads(); // DO NOT EVER REMOVE!!!
    	testPeriodicSpeed = 0.1;
    	testPeriodicForward = true;
    	testPeriodicCount = 0;
    	
    	rightRear.zero();
		rightFront.zero();
		leftRear.zero();
		leftFront.zero();
    	
		driveLeftFront.setFeedbackDevice(CANTalon.FeedbackDevice.EncFalling);
		leftFront.setSpeed(testPeriodicSpeed);
    	leftFront.drive( );
    }
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	testPeriodicCount += 1;
    	if (++testPeriodicCount % 200 == 0) {
    		SmartDashboard.putNumber("test periodic speed: ", testPeriodicSpeed);
    		SmartDashboard.putNumber("hall count: ", driveLeftFront.getEncPosition());
    		if (testPeriodicSpeed >= 0.4) {
    			testPeriodicSpeed = 0.4;
    			testPeriodicForward = false;	
    		} else if (testPeriodicSpeed <= 0.0) {
    			testPeriodicSpeed = 0.0;
    			testPeriodicForward = true;
    		}
    		if (testPeriodicForward)
    			testPeriodicSpeed += 0.05;
    		else
    			testPeriodicSpeed -= 0.05;
    		leftFront.setSpeed(testPeriodicSpeed);
    		leftFront.drive( );
    	}
    }
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
