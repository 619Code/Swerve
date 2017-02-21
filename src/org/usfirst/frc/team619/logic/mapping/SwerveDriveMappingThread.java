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
    protected CANTalon climberMotor1, climberMotor2;
    protected CANTalon intakeMotor, outakeMotor;
    protected CANTalon gearMotor;
    private boolean releasedSpeed;
    private double scalePercent;
    boolean drift = true;
    boolean jiggled, jiggledBack;
    
    public SwerveDriveMappingThread(CANTalon climberMotor1, CANTalon climberMotor2, CANTalon intakeMotor, CANTalon outakeMotor, 
    		CANTalon gearMotor, SwerveDriveBase driveBase, DriverStation driverStation, int period, ThreadManager threadManager) {
        super(period, threadManager);
        this.driveBase = driveBase;
        this.driverStation = driverStation;
        this.climberMotor1 = climberMotor1;
        this.climberMotor2 = climberMotor2;
        this.intakeMotor = intakeMotor;
        this.outakeMotor = outakeMotor;
        this.gearMotor = gearMotor;
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
		
		int pov = driverStation.getRightController().getPOV();
        double xAxis = driverStation.getLeftController().getX(Hand.kRight);
        double yAxis = driverStation.getLeftController().getY(Hand.kRight);
        double zTurn = driverStation.getLeftController().getX(Hand.kLeft);

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
        }else if(driverStation.getLeftController().getStickButton(Hand.kRight)) {
        	drift = !drift;
//        	driveBase.setDriftCompensation(drift);
        }
        if(drift == true) {}
//        	driveBase.compensateDrift(RY, RX, LX);
        else
        	driveBase.move(RY, RX, LX);
    	
        //right controller (manipulators)
        if (pov == 315 || pov == 0 || pov == 45) {
        	climberMotor1.set(-1);
        	climberMotor2.set(-1);
        }else if(pov == 135 || pov == 180 || pov == 225) {
        	climberMotor1.set(0.5);
        	climberMotor2.set(0.5);
        } else if(driverStation.getRightController().getBumper(Hand.kRight)){
        	intakeMotor.set(-1);
        }else if(driverStation.getRightController().getBumper(Hand.kLeft)){
        	outakeMotor.set(-1);
        }else if(driverStation.getRightController().getBButton()){
        	gearMotor.set(-1);
        }else if(driverStation.getRightController().getXButton()) {
        	gearMotor.set(0.5);
        }else if(driverStation.getRightController().getYButton()) {
        	if(jiggled == false) {
        		jiggled = true;
        		gearMotor.set(-1);
        	}else if(jiggledBack == false) {
        		jiggledBack = true;
        		gearMotor.set(1);
        	}
        }else {
        	jiggled = false;
        	jiggledBack = false;
//        	climberMotor1.set(0);
//        	climberMotor2.set(0);
//        	intakeMotor.set(0);
//        	outakeMotor.set(0);
//        	gearMotor.set(0);
        }
    }
    
    private double deadzone(double val) {
    	if(Math.abs(val) < 0.02)
    		return 0;
    	return val;
    }
}
