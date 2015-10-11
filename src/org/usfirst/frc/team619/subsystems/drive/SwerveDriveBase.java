package org.usfirst.frc.team619.subsystems.drive;

import org.usfirst.frc.team619.hardware.CANTalon;
import org.usfirst.frc.team619.hardware.Talon;

public class SwerveDriveBase {
	private Talon driveLeft, driveRight, driveLeft2, driveRight2;
	private CANTalon turnLeft, turnRight, turnLeft2, turnRight2;


    public SwerveDriveBase( Talon driveLeft_, Talon driveRight_, Talon driveLeft2_, Talon driveRight2_,
            				CANTalon turnLeft_, CANTalon turnRight_, CANTalon turnLeft2_, CANTalon turnRight2_ ) {
    	driveLeft = driveLeft_;
    	driveRight = driveRight_;
    	driveLeft2 = driveLeft2_;
    	driveRight2 = driveRight2_;
    	
    	turnLeft = turnLeft_;
    	turnRight = turnRight_;
    	turnLeft2 = turnLeft2_;
    	turnRight2 = turnRight2_;
    }

    public Talon getLeftTalon() {
        return driveLeft;
    }

    public Talon getRightTalon() {
        return driveRight;
    }
    
    public Talon getLeftTalon2(){
        return driveLeft2;
    }//end Talon getLeftTalon2
    
    public Talon getRightTalon2(){
        return driveRight2;
    }//end Talon getRightTalon2

    public void drive(double leftPercent, double rightPercent) {
        driveLeft.set(leftPercent);
        driveRight.set(rightPercent);
    }
    
}
