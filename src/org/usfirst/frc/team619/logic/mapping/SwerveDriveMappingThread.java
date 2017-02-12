package org.usfirst.frc.team619.logic.mapping;

import org.usfirst.frc.team619.logic.RobotThread;
import org.usfirst.frc.team619.logic.ThreadManager;
import org.usfirst.frc.team619.subsystems.DriverStation;
import org.usfirst.frc.team619.subsystems.drive.SwerveDriveBase;
import org.usfirst.frc.team619.subsystems.drive.SwerveWheel;

import com.ctre.CANTalon;

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
    CANTalon climberMotor1 = new CANTalon(9);
    CANTalon climberMotor2 = new CANTalon(10);
    CANTalon intakeMotor = new CANTalon(8);
    CANTalon outakeMotor = new CANTalon(11);
    CANTalon gearOutakeMotor = new CANTalon(12);
    boolean gearCentric = false;
    boolean gearCentricPress = false;
    
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
		scalePercent = 0.4;
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
			if(releasedSpeed && scalePercent >= 0.1) {
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
        double RY = -yAxis * scalePercent;
        double RX = xAxis * scalePercent;
        double LX = (zTurn * scalePercent)*0.75;
        
        //left controller (drive)
        if (driverStation.getLeftController().getStartButton()) {
            driveBase.switchToRobotCentric();
        }else if (driverStation.getLeftController().getBackButton()) {
            driveBase.switchToFieldCentric();
            driveBase.zeroIMU();
        } else if(driverStation.getLeftController().getAButton()) {
        	driveBase.switchToGearCentric();
        } else if(driverStation.getLeftController().getBButton()) {
        	driveBase.goToZero();
        } else if(driverStation.getLeftController().getYButton()) {
        	driveBase.autoZeroWheels();
        } else if(driverStation.getLeftController().getTriggerAxis(Hand.kRight) > 0.05){
        	RY *= driverStation.getLeftController().getTriggerAxis(Hand.kRight)+1;
        	RX *= driverStation.getLeftController().getTriggerAxis(Hand.kRight)+1;
        } else if(driverStation.getLeftController().getTriggerAxis(Hand.kLeft) > 0.05){
        	RY *= 1-driverStation.getLeftController().getTriggerAxis(Hand.kLeft);
        	RX *= 1-driverStation.getLeftController().getTriggerAxis(Hand.kLeft);
        }
    	driveBase.move(RY, RX, LX);
        
        //right controller (manipulators)
        if (driverStation.getRightController().getPOV() == 0) {
        	System.out.println("CLIMB");
        	climberMotor1.set(-1);
        	climberMotor2.set(-1);
        }else if(driverStation.getRightController().getPOV() == 180) {
        	climberMotor1.set(0.5);
        	climberMotor2.set(0.5);
        } else if(driverStation.getRightController().getBumper(Hand.kRight)){
        	intakeMotor.set(-0.5);
        }else if(driverStation.getRightController().getBumper(Hand.kLeft)){
        	outakeMotor.set(0.5);
        }else if(driverStation.getRightController().getBButton()){
        	gearOutakeMotor.set(-1);
        }else if(driverStation.getRightController().getYButton()){
        	gearOutakeMotor.set(1);
        } else {
        	climberMotor1.set(0);
        	climberMotor2.set(0);
        	intakeMotor.set(0);
        	outakeMotor.set(0);
        	gearOutakeMotor.set(0);
        }
    }
}
