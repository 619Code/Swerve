package org.usfirst.frc.team619.subsystems.drive;

import org.usfirst.frc.team619.hardware.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import org.usfirst.frc.team619.hardware.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SwerveWheel {

	public Talon driveMotor; //private Talon driveMotor;
	public CANTalon rotateMotor;
	private String label;
		
	private double rAngle;
	private double speed;
	private int targetAngle;
	private int encoderUnitsPerRotation = 1660;//was 1665
	private double speedModifier = 0.3;//This sets robot default speed to 75%, sniper and turbo mode changes these numbers
	private boolean rolling = false;
	
	public static final double p=10, i=0, d=0;

	// limitToZero - position (encoder) increments (positive or negative) from limit (forward limit switch) position
	//               to zero postion. To initialize (zero( )):
	//                         o  we drive the wheel until the limit is reached
	//                         o  we then drive the wheel limitToZero (i.e. rotateMotor.getPosition( ) + limitToZero)
	//                         o  finally, we call rotateMotor.setPosition(0) to set the zero position to zero; now
	//                            all angles (and positions) will be relative to "zero"
	public SwerveWheel(String label_, Talon driveMotor_, CANTalon rotateMotor_, double rotateAngle){
		
		label = label_;
		driveMotor = driveMotor_;
		rotateMotor = rotateMotor_;

        rotateMotor.changeControlMode(CANTalon.ControlMode.Position);
        rotateMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
        rotateMotor.enableLimitSwitch(false, false);
        rotateMotor.enableBrakeMode(false);
        rotateMotor.reverseSensor(true);
		rotateMotor.setPID(p,i,d);

        targetAngle = 0;
		rAngle = rotateAngle;

	}
	
	public void zero( ) {
		System.out.println("new zero routine");
		rotateMotor.changeControlMode(CANTalon.ControlMode.PercentVbus);
		if ( false ) {                // enable when limit switch is available
			while( rotateMotor.isFwdLimitSwitchClosed( ) ) {
				rotateMotor.set(.15);
			}
		}
		rotateMotor.enableLimitSwitch(false, false);
		rotateMotor.changeControlMode(CANTalon.ControlMode.Position);
		rotateMotor.setPosition(0);
		for ( int count=0; count < 75 && rotateMotor.getPosition( ) != 0; ++count ) { 
			try { Thread.sleep(3); }
			catch (Exception e) { continue; }
		}
		System.out.println(label + " position " + rotateMotor.getPosition( ));
		if ( rotateMotor.getPosition( ) != 0.0 )
			System.err.println( "wheel " + label + " failed to store zero position" );
	}

	public void zero_old( ) {
		rotateMotor.changeControlMode(CANTalon.ControlMode.PercentVbus);
		if ( false ) {                // enable when limit switch is available
			while( rotateMotor.isFwdLimitSwitchClosed( ) ) {
				rotateMotor.set(.15);
			}
		}
		rotateMotor.enableLimitSwitch(false, false);
		rotateMotor.changeControlMode(CANTalon.ControlMode.Position);
		rotateMotor.setPosition(0);
        // initially supported rotating all wheels to an offset from the
		// limit position (i.e. where the limit switch trips), but I don't
		// think it matters as long as all wheels are zeroed at the same location
//
//		if ( limitToZero != 0 ) {
//			double last_position = 0;
//			int failure_count = 0;
//			rotateMotor.set(limitToZero);
//			do {
//				try {
//					last_position = rotateMotor.getPosition( );
//					Thread.sleep(50);
//				} catch(Exception e) {
//					if ( failure_count < 50 ) {
//						++failure_count;
//						continue;
//					} else { break; }
//				}
//			} while ( last_position != rotateMotor.getPosition( ));
//		}
//		
//		rotateMotor.setPosition(0);

	}
	
	public double getDeltaTheta(){
		double deltaTheta = getTargetAngle() - getCurrentAngle();
		
		while ((deltaTheta < -90) || (deltaTheta > 90)){
			if ( label.equals("leftFront") )
				System.out.println( "                          --> " + deltaTheta + "/" + speed );
			if(deltaTheta > 90){
				deltaTheta -= 180;
				speed *= -1;
			}else if(deltaTheta < -90){
				deltaTheta += 180;
				speed *= -1;
			}
		}
		if ( label.equals("leftFront") )
			System.out.println( "                          --> " + deltaTheta + "/" + speed );
		
		return deltaTheta;		
	}
	
	public void setTargetAngle(double angle){		
		if(angle < 0){
			angle += 360;
		}else if(angle >=360){
			angle -= 360;
		}
		targetAngle = (int)angle ;
	}
	
	public int getTargetAngle(){
		return targetAngle;
	}
	
	public int getCurrentAngle(){
		return encoderUnitToAngle(getEncoderValue());
	}

	public int getRAngle(){
		return (int)rAngle;
	}
	
	public void setRAngle(double ra){
		rAngle = ra;
	}
		
	public void goToAngle(){
		rotateMotor.set(rotateMotor.getPosition( ) + angleToEncoderUnit(getDeltaTheta()));
		if ( rolling ) driveMotor.set(speed*speedModifier);
		if ( label.equals("leftFront") ) {
			System.out.println(label + "                             rolling: " + rolling );
			System.out.println(label + "           goToAngle.getCurrentAngle: " + getCurrentAngle( ));
			System.out.println(label + "          goToAngle.getTargetAngle(): " + getTargetAngle( ));
			System.out.println(label + "             goToAngle.getDeltaTheta: " + getDeltaTheta());
			System.out.println(label + "           goToAngle.getEncoderValue: " + getEncoderValue());
			System.out.println(label + " goToAngle.angleToEncoderUnit(delta): " + angleToEncoderUnit(getDeltaTheta()));
			System.out.println(label + "                     goToAngle.speed: " + speed );
		}
	}
	
	public void setSpeed(double magnitude){
		speed = magnitude;
		if ( rolling ) driveMotor.set(speed*speedModifier);
	}
	public double getSpeed(){
		return speed;
	}
	public double getEncoderValue(){
		return rotateMotor.getPosition( );
	}
	
    private int encoderUnitToAngle(double e){
    	double angle = 0;
    	if (e >= 0){
    		angle = (e * (360.0/encoderUnitsPerRotation));
    		angle = angle % 360;
    	} else if (e < 0){
    		angle = (e * (360.0/encoderUnitsPerRotation));
    		angle = angle % 360 + 360;
    	}
    	return (int)angle;
    }
    
    public int angleToEncoderUnit(double angle){       //Only pass in deltaTheta
    	
    	double deltaEncoder;
    	deltaEncoder = angle*(encoderUnitsPerRotation/360.0); 
    	
    	return (int)deltaEncoder;
    }
    
    public void drive( ) {
    	SmartDashboard.putNumber("Speed: " + this.toString(), speed*speedModifier);
    	driveMotor.set(speed*speedModifier);
    	rolling = true;
    }
    
    public void stop( ) {
    	driveMotor.set(0);
    	rolling = false;
    }
	
	public void setSpeedModifier(double speed){
		speedModifier = speed;
	}

}
