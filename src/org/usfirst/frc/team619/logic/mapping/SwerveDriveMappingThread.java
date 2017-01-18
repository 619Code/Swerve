package org.usfirst.frc.team619.logic.mapping;

import org.usfirst.frc.team619.logic.RobotThread;
import org.usfirst.frc.team619.logic.ThreadManager;
import org.usfirst.frc.team619.subsystems.DriverStation;
import org.usfirst.frc.team619.subsystems.drive.SwerveDriveBase;
import org.usfirst.frc.team619.subsystems.drive.SwerveWheel;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * Map joystick values to motor values
 * @author CaRobotics
 */
public class SwerveDriveMappingThread extends RobotThread {
    protected SwerveDriveBase driveBase;
    protected DriverStation driverStation;
    protected SwerveWheel leftFront;
    protected SwerveWheel leftRear;
    protected SwerveWheel rightFront;
    protected SwerveWheel rightRear;
    private boolean releasedSpeed;
    private double scalePercent;
    
    public SwerveDriveMappingThread(SwerveWheel leftFront, SwerveWheel leftRear, SwerveWheel rightFront, SwerveWheel rightRear, 
    		SwerveDriveBase driveBase, DriverStation driverStation, int period, ThreadManager threadManager) {
        super(period, threadManager);
        this.driveBase = driveBase;
        this.driverStation = driverStation;
        this.leftFront = leftFront;
        this.leftRear = leftRear;
        this.rightFront = rightFront;
        this.rightRear = rightRear;
		releasedSpeed = true;
		scalePercent = 0.7;
    }

    protected void cycle() {
		switch(driverStation.getLeftController().getPOV()) {
		case -1: 
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
			if(releasedSpeed && scalePercent >= 0.2) {
				scalePercent -= 0.1;
			}
			releasedSpeed = false;
			break;
		default:
			break;
		}

        double xAxis = driverStation.getLeftController().getX(Hand.kRight);
        double yAxis = driverStation.getLeftController().getY(Hand.kRight);
        double zTurn = driverStation.getLeftController().getX(Hand.kLeft);

        //gets percentages (numbers from -1 to 1) from the joystick's axes used for driving
        double LY = -yAxis * scalePercent;
        double LX = xAxis * scalePercent;
        double RX = zTurn * scalePercent;

        //BUTTONS ARE NOT ASSIGNED CORRECTLY
        //X = A
        //A = B
        //B == X
        
        if (driverStation.getLeftController().getAButton()) {
        	System.out.println();
        	System.out.println("HITTING THE A BUTTON");
            leftFront.zero( );
            leftRear.zero( );
            rightFront.zero( );
            rightRear.zero( );
        }else if (driverStation.getLeftController().getStartButton()) {
            driveBase.switchToRobotCentric();
        }else if (driverStation.getLeftController().getBackButton()) {
            driveBase.switchToFieldCentric();
            driveBase.zeroIMU();
        }else if (driverStation.getLeftController().getBButton()){
        	driveBase.move(scalePercent, 0, 0);
        } else if (driverStation.getLeftController().getBButton()){
        	System.out.println("HITTING THE B BUTTON");
        } else if (driverStation.getLeftController().getYButton()){
        	leftFront.setTargetAngle(0);
        	leftRear.setTargetAngle(0);
        	rightFront.setTargetAngle(0);
        	rightRear.setTargetAngle(0);
        	
        	leftFront.goToAngle();
        	leftRear.goToAngle();
        	rightFront.goToAngle();
        	rightRear.goToAngle();
        //	delay(1000);
        } else {
            driveBase.move(LY, LX, RX);
        }
    }
    public void delay(int milliseconds){
    	try {
    		Thread.sleep(milliseconds);
    	} catch(InterruptedException e) {
    		Thread.currentThread().interrupt();
    	}
    }
}
