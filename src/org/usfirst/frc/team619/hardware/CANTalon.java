package org.usfirst.frc.team619.hardware;

import edu.wpi.first.wpilibj.CANTalon.ControlMode;

/**
 * 
 * The CANTalon object is only to be used with the Talon SRX (the small rectangular one)
 * as it is the only talon that uses CAN
 * 
 * @author Student
 */

public class CANTalon extends edu.wpi.first.wpilibj.CANTalon {
	public CANTalon(int canID){super(canID);}
/****
 ****   this commented out code implements the missing CANTalon.setPosition( )
 ****   by using an internal offset, but setPosition(...) is not be needed...
 ****   encoder uses integer values, while setPosition( )/getPosition( )
 ****   functions take a double. It is probably best to deal in the encoder
 ****   values...
 ****	
	private static boolean initalized = false;
	private static boolean setPositionBroken = false;
	
	// do_translation is true when Talon mode is position
	private boolean do_translation = false;
	// keep track of the offset between what getPosition( ) returns
	// and what has been set with setPosition( ) (when setPositionBroken)
	private double offset = 0;
	
	public CANTalon(int canID){
		super(canID);
		if ( initalized == false ) {
			initalized = true;
			ControlMode orig_mode = super.getControlMode( );
			super.changeControlMode(ControlMode.Position);
			double pos = super.getPosition( );
			super.setPosition( pos + 100 );
			if ( super.getPosition( ) == pos ) {
				setPositionBroken = true;
				System.out.println( "Ahh, the WPI elves gave us a lump of coal...." );
			} else {
				System.out.println( "Yay, the WPI elves have fixed the library....");
			}
			super.setPosition(pos);
			super.changeControlMode(orig_mode);
		}
	}
	
	public double getRawPosition( ) { return super.getPosition( ); }
	// if setPosition(...) from wpilibj CANTalon works, use it...
	// otherwise use an offset to manage broken wpilibj setPosition(...)
	public void setPosition( double pos ) {
		if ( setPositionBroken ) offset = pos - super.getPosition( );
		else super.setPosition(pos);
	}
	
	public double getPosition( ) {
		if ( setPositionBroken ) return super.getPosition( ) + offset;
		else return super.getPosition( );
	}
	
	public void set( double outputValue ) {
		if ( setPositionBroken && do_translation ) {
			super.set(outputValue - offset);
		}
	}
	
	public void changeControlMode(ControlMode controlMode) {
		if ( controlMode == ControlMode.Position)
			do_translation = true;
		else
			do_translation = false;
		
		super.changeControlMode(controlMode);
	}
******/
}
