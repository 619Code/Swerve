package org.usfirst.frc.team619.logic.mapping;

import org.opencv.core.Rect;
import org.usfirst.frc.team619.hardware.AnalogUltrasonic;
import org.usfirst.frc.team619.logic.RobotThread;
import org.usfirst.frc.team619.logic.ThreadManager;
import org.usfirst.frc.team619.subsystems.drive.SwerveDriveBase;

import com.ctre.CANTalon;

public class AutoThread extends RobotThread {
	
	private Object imgLock;
	private SwerveDriveBase driveBase;
	private TargetThread vision;
	boolean stop = false;
	boolean stopMoving = false;
	
	//movement vars
	double turn = 0;
	double moveX = 0;
	double moveY = 0;
	
	//Rectangles
	Rect rightangle, leftangle, centangle;
	
	//current points
	int heightRect1, heightRect2;
	int widthRect1, widthRect2;
	
	int rectX1, rectX2;
	int rectY1, rectY2;
	
	int rightCenter, leftCenter;
	
	//other
	double centerX;
	int numRects;
	double distance;
	
	//target ranges
	int leastX = 10;
	int greatestX = 30;
	
	int leastY = 50;
	int greatestY = 100;
	
	int leastHeight = 65;
	int greatestHeight = 70;
	
	int leastWidth = 30;
	int greatestWidth = 40;

	boolean isRunning;
	boolean gearLaunched;
	//Hardware
	CANTalon gearOutake;
	AnalogUltrasonic ultrasonic;
	
	public AutoThread(TargetThread vision, Object syncLock, SwerveDriveBase driveBase, int period, ThreadManager threadManager, CANTalon gearOutakeMotor, AnalogUltrasonic ultrasanic) {
		super(period, threadManager);
		driveBase.switchToGearCentric();
		imgLock = syncLock;
		this.driveBase = driveBase;
		this.vision = vision;
		this.driveBase.switchToGearCentric();
		gearOutake = gearOutakeMotor;
		isRunning = false;
		gearLaunched = false;
		ultrasonic = ultrasanic;
		start();
	}

	protected void cycle() {
		rightangle = vision.getRightangle();
		leftangle = vision.getLeftangle();
		centangle = vision.getCentangle();
		
		distance = ultrasonic.getDistanceIn();
		System.out.println("AND THE DISTANCE IS: " + distance);
		numRects = vision.getNumRects();
		//try { Thread.sleep(5000); }catch(Exception e){}
		if(numRects > 1 && !gearLaunched) {
			System.out.println("IM SETTING ISRUNNING TO TRUE ANY MINUTE NOW");
			isRunning = true;
			centerX = (centangle.x + (centangle.x+centangle.width))/2;
//			System.out.println(centangle.height);
			//if the height and width are less than target values
			if(centangle.height < leastHeight) {
				//move forward
				moveY = 0.2;
//				System.out.println(centangle.height);
//				System.out.println("Going forward!");
			//if the height and width are greater than target values
			}else if(centangle.height > greatestHeight) {
				//move backwards
				moveY = -0.2;
//				System.out.println("Going backward!");
			}else	
				moveY = 0;
			//both targets are in view
			int imgCenter = vision.IMG_WIDTH/2;
			System.out.println("Center: "+centerX);
			if(imgCenter-10 < centerX && centerX < imgCenter+10 && numRects > 1) {
				moveX = 0;
//				if(centerX > imgCenter+10)
//					System.out.println("MOVE TO THE RIGHT " + centerX);
//				if(centerX < imgCenter-10)
//					System.out.println("MOVE TO THE LEFT " + centerX);
				//moveX *= -0.8; //speed multiplier. Speed changes linearly but may need adjusting
			}else{
				moveX = (centerX-imgCenter)/imgCenter; //Left of screen is -1, middle is 0, right is 1
				moveX *= 0.9;
				if(moveX > 0.3){
					moveX = 0.3;
				} else if(moveX < -0.3){
					moveX = -0.3;
				}
				System.out.println("ADJUSTED VALUE: " + moveX);
			} //Both targets are not in view, dont change position
				//moveX = 0;
			System.out.println(moveY);
			
			driveBase.move(moveY, moveX, 0);
		}else if(!gearLaunched) {
			System.out.println("GEAR ISNT LAUNCHED YET HUEUEUEUEU");
			driveBase.move(0,0,0);
			System.out.println("I THINK ISRUNNING IS: " + isRunning);
			if(isRunning == true && distance < 76) {
				System.out.println("LAUNCH GEAR NOW");
				gearOutake.set(-1);
				driveBase.move(-0.25, 0, 0);
				try{ Thread.sleep(100); }catch(Exception e) { }
				gearLaunched = true;
			}
			if(gearLaunched && isRunning == true){
				System.out.println("WHY AM I IN HERE FSHJSFHSDFHDFHJSDFHFFFDHDFHDFJFJFDSJDJJFJHSDFJDFJDFJDFJJHSDFJKSDFJDJDFJFJHDFFDFJHFDFDFJFFDFDFDFSHJFSDHJSDFHJSFDHJSDFJHSDFJHDSFJHKLFJKFSDH");
				gearOutake.set(0);
				isRunning = false;
				try { Thread.sleep(1150); }catch(Exception e) {}
				driveBase.move(0, 0, 0);
//				driveBase.move(-0.25, 0, 0);
//				gearOutake.set(-0.2);
			}
		}
//		}else {
//			try { Thread.sleep(200); }catch(Exception e) {}
//			gearOutake.set(0);
//			try { Thread.sleep(1000); }catch(Exception e){}
//			driveBase.move(0, 0, 0);
//		}
	}
	
//	protected void cycleOld() {
//		double turn = 0;
//		double move = 0;
//		double offset = 0;
//		int m_size = 0;
//		int m_height = 0;
//	    synchronized (imgLock) {
//	    	m_height = vision.getHeight();
//	    	turn = vision.getCenter();
//	    	m_size = vision.getSize();
//	    }
//	    if (m_size > 1) {
//	        System.out.println("Target size:  " + m_height);
//	        if(m_height <= 45)
//	        	move = 0.2;
//	        if(m_height >= 50)
//	        	move = -0.2;
//	        	
//			turn -= (vision.IMG_WIDTH/2);
//			turn += 20;
//			turn /= vision.IMG_WIDTH;
//			//Speed multiplier
//			turn *= 5;
//			if(turn > 0.15)
//				turn = 0.15;
//			if(turn < -0.15)
//				turn = -0.15;
//			if(Math.abs(turn) < 0.05)
//				turn = 0;
//	   }else if(m_size == 1) {
//		   //checking axes
//	       	System.out.println("Target size:  " + m_height);
//	       	if(m_height <= 47)
//	       		move = 0.2;
//	       	if(m_height >= 50)
//	       		move = -0.2;
//	        	
//			turn -= (vision.IMG_WIDTH/2);
//			turn += 20;
//			turn /= vision.IMG_WIDTH;
//			//Speed multiplier
//			turn *= 5;
//			if(turn > 0.15)
//				turn = 0.15;
//			if(turn < -0.15)
//				turn = -0.15;
//			if(Math.abs(turn) < 0.05)
//				turn = 0;
//	  }else{
//		  	turn = 0;
//	    	move = 0;
//        }
//    	SmartDashboard.putNumber("Actual Turn", turn);
//    	SmartDashboard.putNumber("Move value", move);
//    	//driveBase.move(0,0,turn);
//    	driveBase.move(move, 0, turn);
//	}

}
