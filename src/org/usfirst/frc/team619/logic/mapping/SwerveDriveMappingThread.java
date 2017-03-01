package org.usfirst.frc.team619.logic.mapping;

import org.opencv.core.Rect;
import org.usfirst.frc.team619.hardware.AnalogUltrasonic;
import org.usfirst.frc.team619.logic.RobotThread;
import org.usfirst.frc.team619.logic.ThreadManager;
import org.usfirst.frc.team619.subsystems.DriverStation;
import org.usfirst.frc.team619.subsystems.drive.SwerveDriveBase;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.PowerDistributionPanel;


/**
 * Map joystick values to motor values
 * @author CaRobotics
 */
public class SwerveDriveMappingThread extends RobotThread {
    protected SwerveDriveBase driveBase;
    protected PowerDistributionPanel pdp;
    protected DigitalInput switch1;
    protected DigitalInput switch2;
    protected TargetThread vision;
    protected DriverStation driverStation;
    protected AnalogUltrasonic ultrasonic;
    protected CANTalon climberMotor1, climberMotor2;
    protected CANTalon intakeMotor, outakeMotor;
    protected CANTalon gearMotor;
    private boolean releasedSpeed;
    private double scalePercent;
    private double autoSpeed;
    boolean drift = true;
    boolean gearLaunched;
    boolean speedToggle;
    boolean jiggled, jiggledBack;
    
    public SwerveDriveMappingThread(DigitalInput switch1, DigitalInput switch2, AnalogUltrasonic ultrasonic, 
    		CANTalon climberMotor1, CANTalon climberMotor2, CANTalon intakeMotor, CANTalon outakeMotor, 
    		CANTalon gearMotor, TargetThread targetThread, PowerDistributionPanel pdp, SwerveDriveBase driveBase, 
    		DriverStation driverStation, int period, ThreadManager threadManager) {
        super(period, threadManager);
        
        this.pdp = pdp;
        this.switch1 = switch1;
        this.switch2 = switch2;
        this.vision = targetThread;
        this.driveBase = driveBase;
        this.driverStation = driverStation;
        this.climberMotor1 = climberMotor1;
        this.climberMotor2 = climberMotor2;
        this.intakeMotor = intakeMotor;
        this.outakeMotor = outakeMotor;
        this.ultrasonic = ultrasonic;
        this.gearMotor = gearMotor;
        
        driveBase.setDriftCompensation(true);
        driveBase.setDriftRange(2);
		releasedSpeed = true;
		gearLaunched = false;
		jiggled = false;
		jiggledBack = false;
		speedToggle = false;
		driveBase.zeroIMU();
		scalePercent = 0.6;
		autoSpeed = 0.35;
    }

    protected void cycle() {
		switch(driverStation.getLeftController().getPOV()) {
		default: 
			releasedSpeed = true;
			break;
		case 45:
		case 315:
		case 0:
			if(releasedSpeed && scalePercent <= 1.0) {
				scalePercent += 0.1;
			}
			releasedSpeed = false;
			break;
		case 135:
		case 225:
		case 180:
			if(releasedSpeed && scalePercent >= 0.1) {
				scalePercent -= 0.1;
			}
			releasedSpeed = false;
			break;
		}
		
		if(pdp.getCurrent(8) > 5)
			System.out.println("CURRENT IS " + pdp.getCurrent(8));
		
		int pov = driverStation.getRightController().getPOV();
        double xAxis = driverStation.getLeftController().getX(Hand.kRight);
        double yAxis = driverStation.getLeftController().getY(Hand.kRight);
        double zTurn = driverStation.getLeftController().getX(Hand.kLeft);

        double leftTrigger = driverStation.getLeftController().getTriggerAxis(Hand.kLeft);
        double rightTrigger = driverStation.getRightController().getTriggerAxis(Hand.kRight);
        
        //gets percentages (numbers from -1 to 1) from the joystick's axes used for driving
        double RY = -yAxis * scalePercent;
        double RX = xAxis * scalePercent;
        double LX = (zTurn * scalePercent)*0.75;
        
        RY = deadzone(RY);
        RX = deadzone(RX);
        LX = deadzone(LX);
        
        //left controller (drive)
        if (driverStation.getLeftController().getStartButton()) {
            driveBase.switchToRobotCentric();
        }else if (driverStation.getLeftController().getBackButton()) {
            driveBase.switchToFieldCentric();
        } else if(driverStation.getLeftController().getAButton()) {
        	driveBase.switchToGearCentric();
        } else if(driverStation.getLeftController().getBButton()) {
        	driveBase.goToZero();
        } else if(driverStation.getLeftController().getYButton()) {
        	driveBase.autoZeroWheels();
        } else if(driverStation.getLeftController().getBumper(Hand.kLeft)) {
        	//Set drive values to 60%
        	RY *= (0.6/scalePercent);
        	RX *= (0.6/scalePercent);
        	LX *= (0.6/scalePercent);
        } else if(rightTrigger > 0.05){
        	RY *= rightTrigger+1;
        	RX *= rightTrigger+1;
        } else if(leftTrigger > 0.05){
        	RY *= 1-leftTrigger;
        	RX *= 1-leftTrigger;
        }else if(driverStation.getLeftController().getStickButton(Hand.kLeft)) {
        	drift = !driveBase.isDriftCompensated();
        	driveBase.setDriftCompensation(drift);
        }else if(driverStation.getLeftController().getStickButton(Hand.kRight)) {
            driveBase.zeroIMU();
        }
        
        if(driverStation.getLeftController().getXButton()) {
        	boolean delivered = autoGear();
        	if(delivered)
        		System.out.println("LAUNCH GEAR NOW");
        	else
        		System.out.println("ALIGNING ROBOT......");
        }else if(driverStation.getLeftController().getBumper(Hand.kRight)) {
        	driveBase.move(0, 0, goToAngle());
            System.out.println("CurrentAngle: " +driveBase.getYaw());
            System.out.println("Turn amount: " +goToAngle());
        }else {
        	gearLaunched = false;
            driveBase.move(RY, RX, LX);
            autoSpeed = 0.35;
        }
    	
        //right controller (manipulators)
        switch(pov) {
        case 315:
        case 0:
        case 45:
        	climberMotor1.set(-1);
        	climberMotor2.set(-1);
        	break;
        case 135:
        case 180:
        case 225:
        	climberMotor1.set(1);
        	climberMotor2.set(1);
        	break;
        default:
        	climberMotor1.set(0);
        	climberMotor2.set(0);
        		
        }
        if(driverStation.getRightController().getBumper(Hand.kLeft)){
        	outakeMotor.set(0.5);
        }else if(driverStation.getRightController().getBumper(Hand.kRight)) {
        	intakeMotor.set(-1);
        }else if(driverStation.getRightController().getBButton()){
        	pdp.getCurrent(8);
        	gearMotor.set(-0.75);
        }else if(driverStation.getRightController().getYButton()) {
        	gearMotor.set(0.5);
        }else if(driverStation.getRightController().getXButton()) {
        	if(jiggled == false) {
        		jiggled = true;
        		gearMotor.set(-1);
        	}else if(jiggledBack == false) {
        		jiggledBack = true;
        		gearMotor.set(1);
        	}
        }else if(driverStation.getRightController().getAButton()){
        	outakeMotor.set(-0.5);
        }else {
        	jiggled = false;
        	jiggledBack = false;
        	intakeMotor.set(0);
        	outakeMotor.set(0);
        	gearMotor.set(0);
        }
    }
    
    private boolean autoGear() {
		Rect centangle = vision.getCentangle();
		Rect leftangle = vision.getLeftangle();
		Rect rightangle = vision.getRightangle();

		double center = 0;
		int imgCenter = vision.IMG_WIDTH/2;
		int leastHeight = 70;
		int greatestHeight = 75;
		int numRects = vision.getNumRects();
		double moveY, moveX;
		double distance = ultrasonic.getDistanceIn();
		
		if(numRects > 0)
			center = (leftangle.x + (leftangle.x+leftangle.width))/2;
		if(numRects > 1 && !gearLaunched) {
			if(centangle.height > 25)
				autoSpeed = 0.2;
			double centerX = (centangle.x + (centangle.x+centangle.width))/2;
			
			if(centangle.height < leastHeight) {
				//move forward
				moveY = autoSpeed;
			}else if(centangle.height > greatestHeight) {
				//move backwards
				moveY = -autoSpeed;
			}else	
				moveY = 0;
			
			//both targets are in view
			System.out.println("Center: "+centerX);
			if(imgCenter-10 < centerX && centerX < imgCenter+10 && numRects > 1) {
				moveX = 0;
			}else{
				moveX = (centerX-imgCenter)/imgCenter; //Left of screen is -1, middle is 0, right is 1
				moveX *= 0.9;
				if(moveX > 0.2){
					moveX = 0.2;
				} else if(moveX < -0.2){
					moveX = -0.2;
				}
			}
			driveBase.move(moveY, moveX, 0);
		}
		if(!gearLaunched && distance < 85) {
			if(rightangle == null && leftangle != null) {
				System.out.println("Correcting angle.........");
				if(center < 80)
					driveBase.move(0, 0.15, 0);
				else
					driveBase.move(0, -0.15, 0);
			}else {
				driveBase.move(0, 0, 0);
				System.out.println("LAUNCH GEAR NOW");
				gearLaunched = true;
			}
		}else if(numRects < 2 && !gearLaunched) {
			if(numRects == 1 && imgCenter-10 > center)
				moveX = 0.15;
			else if(numRects == 1 && imgCenter+10 < center)
				moveX = -0.15;
			else
				moveX = 0;
			driveBase.move(autoSpeed, moveX, 0);
		}
		return gearLaunched;
    }
    

	private double goToAngle() {
		double currentAngle = driveBase.getYaw();
		double speed = 0;
		if(switch1.get() && !switch2.get()) {
			int targetAngle = -30;
			System.out.println("Target Angle: " + targetAngle);
			speed = sin(toRadians(targetAngle - currentAngle));
		}else if(switch2.get() && !switch1.get()) {
			int targetAngle = 30;
			System.out.println("Target Angle: " + targetAngle);
			speed = sin(toRadians(targetAngle - currentAngle));
		}
		speed *= 1.3;
		if(speed > 0.4)
			speed = 0.4;
		if(speed < -0.4)
			speed = -0.4;
		return speed;
	}
    
    private double deadzone(double val) {
    	if(Math.abs(val) < 0.05)
    		return 0;
    	return val;
    }
    
    private double abs(double val) {
    	return Math.abs(val);
    }
    
    private double toRadians(double val) {
    	return Math.toRadians(val);
    }
    
    private double sin(double val) {
    	return Math.sin(val);
    }
}
