package org.usfirst.frc.team619.logic.actions;

import org.opencv.core.Rect;
import org.usfirst.frc.team619.hardware.AnalogUltrasonic;
import org.usfirst.frc.team619.logic.mapping.TargetThread;
import org.usfirst.frc.team619.robot.SwerveTest;
import org.usfirst.frc.team619.subsystems.drive.SwerveDriveBase;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;

public class Autonomous {
	
	DigitalInput switch1, switch2;
	AnalogUltrasonic ultraLeft, ultraRight;
	CANTalon gearMotor;
	SwerveDriveBase driveBase;
	SwerveTest main;
	TargetThread vision;
	
	public Autonomous(DigitalInput switch1, DigitalInput switch2, AnalogUltrasonic ultrasanic, SwerveTest main, 
			SwerveDriveBase driveBase, CANTalon gearOutakeMotor, TargetThread vision) {
		this.switch1 = switch1;
		this.switch2 = switch2;
		this.ultraRight = ultrasanic;
		this.gearMotor = gearOutakeMotor;
		this.driveBase = driveBase;
		this.vision = vision;
		this.main = main;
	}
	
	public boolean run() {
		driveBase.switchToFieldCentric();
		int targetAngle = 0;
		if(switch1.get() && !switch2.get())
			targetAngle = 60;
		if(!switch1.get() && switch2.get())
			targetAngle = -60;
		
		//Move forward and turn to the angle
		while(Math.abs(targetAngle - driveBase.getYaw()) > 5) {
			if(isDisabled())
				return false;
			alignAndTurn(targetAngle);
		}
		
		//Continue moving out until the target is visible
		while(vision.getNumRects() == 0) {
			if(isDisabled())
				return false;
			driveBase.move(0.3, 0, 0);
		}
		delay(250);
		
		//Move to be x feet away and in the center(ish)
		driveBase.switchToGearCentric();
		while(ultraRight.getDistanceIn() > 250)	{
			if(isDisabled())
				return false;
			initialAdjustment();
		}
		delay(250);
		
		//Stop and adjust left and right for the best placement
		boolean finishedAdjust = false;
		while(!finishedAdjust) {
			if(isDisabled())
				return false;
			delay(100);
			finishedAdjust = finalAdjustment();
			delay(200);
			driveBase.move(0, 0, 0);
		}
		delay(250);
		
		//place the gear
		boolean gearPlaced = false;
		while(!gearPlaced) {
			if(isDisabled())
				return false;
			placeGear();
		}
		
		return true;
	}
	
	private void delay(long millis) {
		try {
			Thread.sleep(millis);
		}catch(InterruptedException e) {
			System.out.println("SOMETHING WENT WRONG!!");
			System.out.println(e.getStackTrace());
		}
	}
	
	private boolean isDisabled() {
		return main.isDisabled();
	}
	
	private void alignAndTurn(int targetAngle) {
		//Move forward and turn to the angle in field centric
		
		double currentAngle = driveBase.getYaw();
		double angleRatio = 0;
		double speed = 0;
		
		angleRatio = (targetAngle - currentAngle)/180;
		speed = (2/3)*Math.pow(angleRatio, 2/3);
		
		if(speed > 0.3)
			speed = 0.3;
		if(speed < -0.3);
			speed = -0.3;
		driveBase.move(0, 0.3, speed);
	}
	
	private void initialAdjustment() {
		//move forward and line up with target in gearCentric
		
		Rect centangle = vision.getCentangle();
		int imgCenter = vision.IMG_WIDTH/2;
		double strafeSpeed = 0;
		double centangleMid = (centangle.x + (centangle.x+centangle.width))/2;
		
		if(centangleMid+10 < imgCenter || imgCenter < centangleMid-10) {
			strafeSpeed = (centangleMid-imgCenter)/imgCenter;
			strafeSpeed *= 0.6;
			
			if(strafeSpeed > 0.3)
				strafeSpeed = 0.3;
			if(strafeSpeed < -0.3)
				strafeSpeed = -0.3;
		}
		
		driveBase.move(strafeSpeed, 0.25, 0);
	}
	
	private boolean finalAdjustment() {
		//stop, make sure robot is centered and target is x feet away
		Rect centangle = vision.getCentangle();
		int imgCenter = vision.IMG_WIDTH/2;
		double speed = 0;
		double centangleMid = (centangle.x + (centangle.x+centangle.width))/2;
		
		//move to the right
		if(centangleMid+3 < imgCenter) {
			driveBase.move(0.2, 0, 0);
		}else if(centangleMid-3 > imgCenter) {
			driveBase.move(-0.2, 0, 0);
		}else
			return true;
		
		return false;
	}
	
	private void placeGear() {
		//move slightly forward, place the gear and move back
		if(ultraRight.getDistanceIn() > 78) {
			driveBase.move(0, 0.2, 0);
		}else {
			gearMotor.set(-1);
			delay(500);
			gearMotor.set(0);
			delay(500);
			driveBase.move(0, -0.35, 0);
			delay(500);
			gearMotor.set(1);
			delay(250);
			gearMotor.set(0);
			driveBase.move(0, 0, 0);
			driveBase.switchToFieldCentric();
		}
	}

}
