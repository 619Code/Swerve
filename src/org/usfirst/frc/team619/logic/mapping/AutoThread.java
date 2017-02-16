package org.usfirst.frc.team619.logic.mapping;

import org.opencv.core.Rect;
import org.usfirst.frc.team619.logic.RobotThread;
import org.usfirst.frc.team619.logic.ThreadManager;
import org.usfirst.frc.team619.subsystems.drive.SwerveDriveBase;

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
	Rect rightRectangle, leftRectangle;
	
	//current points
	int heightRect1, heightRect2;
	int widthRect1, widthRect2;
	
	int rectX1, rectX2;
	int rectY1, rectY2;
	
	int rightCenter, leftCenter;
	
	//other
	int centerX;
	int numRects;
	int camOffset = 0;
	
	//target ranges
	int leastX = 10;
	int greatestX = 30;
	
	int leastY = 50;
	int greatestY = 100;
	
	//dont know range
	int leastHeight = 20;
	int greatestHeight = 40;
	
	//dont know range
	int leastWidth = 20;
	int greatestWidth = 35;

	public AutoThread(TargetThread vision, Object syncLock, SwerveDriveBase driveBase, int period, ThreadManager threadManager) {
		super(period, threadManager);
		driveBase.switchToGearCentric();
		imgLock = syncLock;
		this.driveBase = driveBase;
		this.vision = vision;
		this.driveBase.switchToGearCentric();
		start();
	}

	protected void cycle() {
		long time = System.currentTimeMillis();
		
		synchronized (imgLock){
			rightRectangle = vision.getRightangle();
			leftRectangle = vision.getCentangle();
			
			rightCenter = rightRectangle.x + rightRectangle.width;
			leftCenter =  + leftRectangle.width;
			
			centerX = (int)vision.getCenter();
			
			numRects = vision.getNumRects();
		}
		
		if(numRects > 0) {
		//if the height and width are less than target values
			if(heightRect1 < leastHeight) {
				//move forward
				moveY = 0.3;
				System.out.println(heightRect1);
				System.out.println("Going forward!");
			//if the height and width are greater than target values
			}else if(heightRect1 > greatestHeight) {
				//move backwards
				moveY = -0.3;
				System.out.println("Going backward!");
			}else{
				System.out.println("STOPPING");
				stopMoving = true;
				moveY = 0;
			}
			//both targets are in view
			if(70 + camOffset < leftCenter || leftCenter < 90 + camOffset && numRects > 1) {
				if(leftCenter < 70)
					System.out.println("MOVE TO THE LEFT");
				if(leftCenter > 90)
					System.out.println("MOVE TO THE RIGHT");
				moveX = (leftCenter - vision.IMG_WIDTH)/2; //Left of screen is -1, middle is 0, right is 1
				moveX += camOffset; //Add camera's offset, maybe unneeded depending on mounting
				moveX *= 0.5; //speed multiplier. Speed changes linearly but may need adjusting
			}else //Both targets are not in view, dont change position
				moveX = 0;
			if(!stopMoving) {
				System.out.println("I AM MOVING");
				driveBase.move(moveY, moveX, 0);
			}
			
		//if height and width are in range of target values
			//move right until there is only one rectangle
		}
		
		//time taken for one cycle
		time = System.currentTimeMillis();
		System.out.println(time);
		//if there is one rectangle
			//if x value is less than target value
				//move left
			//if x value sis greater than target value
				//move right
			//else the target value is in target range
			//if y value is less than target value
				//move forward
			//if y value is greater than target value
				//move back
			//else then the robot is in target range
				//shoot gear
				//stop robot
		//else no rectangles
			//stop robot
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
