package org.usfirst.frc.team619.subsystems.drive;

import org.usfirst.frc.team619.hardware.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import org.usfirst.frc.team619.hardware.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SwerveWheel {

	public Talon driveMotor; //private Talon driveMotor;
	public CANTalon rotateMotor;
	private String label;
	
	private boolean zeroed = false;
	
	private double rAngle;
	private double rSpeed;
	private double speed;
	private int targetAngle;
	private int encoderUnitsPerRotation = 1660;//was 1665
	private double speedModifier = 0.3;//This sets robot default speed to 75%, sniper and turbo mode changes these numbers
	private int encoderAtLimit = 0;
	private int limitToZero = 0;
	
	private double output = 0.3;
	
	public static final double p=10, i=0, d=0;

	// limitToZero - position (encoder) increments (positive or negative) from limit (forward limit switch) position
	//               to zero postion. To initialize (zero( )):
	//                         o  we drive the wheel until the limit is reached
	//                         o  we then drive the wheel limitToZero (i.e. rotateMotor.getPosition( ) + limitToZero)
	//                         o  finally, we call rotateMotor.setPosition(0) to set the zero position to zero; now
	//                            all angles (and positions) will be relative to "zero"
	public SwerveWheel(String label_, Talon driveMotor_, CANTalon rotateMotor_, double rotateAngle, int limitToZero_){
		
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
		limitToZero = limitToZero_;

	}
	
	
	public void zero( ) {
		System.out.println( "ENTER   zerozerozerozerozerozerozerozerozerozerozerozerozerozerozerozerozerozerozerozerozero" );
		rotateMotor.changeControlMode(CANTalon.ControlMode.PercentVbus);
		if ( false ) {                // enable when limit switch is available
			while( ! rotateMotor.isFwdLimitSwitchClosed( ) ) {
				rotateMotor.set(.15);
			}
		}
		encoderAtLimit = rotateMotor.getEncPosition( );
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

		zeroed = true;
		System.out.println( "EXIT    zerozerozerozerozerozerozerozerozerozerozerozerozerozerozerozerozerozerozerozerozero" );
	}

	private void initalizePosition( double rotateAngle, int distanceFromZero ) {
		rAngle = rotateAngle;
		limitToZero = distanceFromZero;
	}
	
	public double getDeltaTheta(){
		double deltaTheta = getTargetAngle() - getCurrentAngle();
		
		while ((deltaTheta < -90) || (deltaTheta > 90)){
			if(deltaTheta > 90){
				deltaTheta -= 180;
				speed *= -1;
			}else if(deltaTheta < -90){
				deltaTheta += 180;
				speed *= -1;
			}
		}
		
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
	
	public double getRSpeed(){
		return rSpeed;
	}
	public void setRAngle(double ra){
		rAngle = ra;
	}
	
	public void setRSpeed(double rs){
		rSpeed = rs;
	}
	
	
	
	public void goToAngle(){
		System.out.println(label + "           goToAngle.getCurrentAngle: " + getCurrentAngle( ));
		System.out.println(label + "          goToAngle.getTargetAngle(): " + getTargetAngle( ));
		System.out.println(label + "             goToAngle.getDeltaTheta: " + getDeltaTheta());
		System.out.println(label + "           goToAngle.getEncoderValue: " + getEncoderValue());
		System.out.println(label + " goToAngle.angleToEncoderUnit(delta): " + angleToEncoderUnit(getDeltaTheta()));
		System.out.println(label + "                     goToAngle.speed: " + speed );
//		rotateMotor.set(getEncoderValue() + angleToEncoderUnit(getDeltaTheta()));
//		rotateMotor.set(getEncoderValue() + encoderAtLimit + angleToEncoderUnit(getDeltaTheta()));
		rotateMotor.set(rotateMotor.getPosition( ) + angleToEncoderUnit(getDeltaTheta()));
	}
	
	public void setSpeed(double magnitude){
		speed = magnitude;
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
    
    public void drive(){
    	SmartDashboard.putNumber("Speed: " + this.toString(), speed*speedModifier);
    	driveMotor.set(speed*speedModifier);
    }
    
	public void goToHome(){
		SmartDashboard.putNumber("Encoder At Home: ", encoderAtLimit);
		rotateMotor.changeControlMode(CANTalon.ControlMode.PercentVbus);
		while(!rotateMotor.isFwdLimitSwitchClosed()){
			rotateMotor.set(.15);
		}
		encoderAtLimit = rotateMotor.getEncPosition();
		SmartDashboard.putNumber("Encoder At Home: ", encoderAtLimit);
		rotateMotor.set(0);
		rotateMotor.enableLimitSwitch(false, false);
		rotateMotor.changeControlMode(CANTalon.ControlMode.Position);
	}
	
	private void goToZero(){
		goToHome();
		rotateMotor.setP(5);
		rotateMotor.setD(8);
		rotateMotor.set(encoderAtLimit - limitToZero);
	}
	
	public void setSpeedModifier(double speed){
		speedModifier = speed;
	}
    
    public void test(){
    	double current = rotateMotor.getOutputCurrent();
    	rotateMotor.changeControlMode(CANTalon.ControlMode.PercentVbus);
    	
    	if(current > 0.95){
    		output -= 0.05;
    	} else if(current < 0.7){
    		output += 0.05;
    	}
    	
    	if(output > 0.3){
    		output = 0.3;
    	}
    	
    	if(output < 0){
    		output = 0;
    	}
    	
    	rotateMotor.set(output);
    	SmartDashboard.putNumber("Output to Motor", output);
    	SmartDashboard.putNumber("Current", rotateMotor.getOutputCurrent());
    }
    
    public void disable(){
    	rotateMotor.set(0);
    }

	
}
