package org.usfirst.frc.team619.logic.mapping;

import org.usfirst.frc.team619.hardware.Joystick;
import org.usfirst.frc.team619.hardware.CANTalon;
import org.usfirst.frc.team619.hardware.Talon;
import org.usfirst.frc.team619.logic.RobotThread;
import org.usfirst.frc.team619.logic.ThreadManager;
import org.usfirst.frc.team619.subsystems.DriverStation;
import org.usfirst.frc.team619.subsystems.drive.SwerveDriveBase;
import org.usfirst.frc.team619.subsystems.drive.SwerveWheel;
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
    private final static boolean DEBUG = true;

    public SwerveDriveMappingThread(SwerveWheel leftFront, SwerveWheel leftRear, SwerveWheel rightFront, SwerveWheel rightRear, SwerveDriveBase driveBase, DriverStation driverStation, int period, ThreadManager threadManager) {
        super(period, threadManager);
        this.driveBase = driveBase;
        this.driverStation = driverStation;
        this.leftFront = leftFront;
        this.leftRear = leftRear;
        this.rightFront = rightFront;
        this.rightRear = rightRear;
    }

    protected void cycle() {
        double scalePercent = driverStation.getLeftJoystick().getAxis(Joystick.Axis.AXIS_Z);

        if(scalePercent < 0.3){
            scalePercent = 0.3;
        }

        double xAxis = driverStation.getLeftJoystick().getAxis(Joystick.Axis.AXIS_X);
        double yAxis = driverStation.getLeftJoystick().getAxis(Joystick.Axis.AXIS_Y);
        double zTwist = driverStation.getLeftJoystick().getAxis(Joystick.Axis.AXIS_TWIST);

        //gets percentages (numbers from -1 to 1) from the joystick's axes used for driving
        double LY = yAxis * scalePercent;
        double LX = -xAxis * scalePercent;
        double RX = -zTwist * scalePercent;

        if (driverStation.getLeftJoystick().getButton(Joystick.Button.BUTTON2)) {
            leftFront.zero( );
            leftRear.zero( );
            rightFront.zero( );
            rightRear.zero( );
        }else if (driverStation.getLeftJoystick().getButton(Joystick.Button.BUTTON5)) {
            driveBase.switchToRobotCentric();
        }else if (driverStation.getLeftJoystick().getButton(Joystick.Button.BUTTON6)) {
            driveBase.switchToFieldCentric();
            driveBase.zeroIMU();
        } else {
            driveBase.move(LY, LX, RX);
        }
    }

}
