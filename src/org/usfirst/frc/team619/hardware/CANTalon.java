package org.usfirst.frc.team619.hardware;

/**
 *
 * The CANTalon object is only to be used with the Talon SRX (the small rectangular one)
 * as it is the only talon that uses CAN
 *
 * @author Student
 */

public class CANTalon extends edu.wpi.first.wpilibj.CANTalon {
	// left1 wheel goes like mad... perhaps the initialization of
	// our setPosition(...) state is the problem...
	// so, for now, let's assume that
	//    (1) we've already checked whether setPosition(...) works
	//    (2) and it does not work...
	private static boolean initalized = true;
	private static boolean setPositionBroken = false;

	// do_translation is true when Talon mode is position
	private boolean do_translation = false;
	// keep track of the offset between what getPosition( ) returns
	// and what has been set with setPosition( ) (when setPositionBroken)
	private double offset = 0;
	private boolean debug = false;

	public CANTalon(int canID){
		super(canID);
		initialize( );
	}
	public CANTalon(int canID, boolean debug_){
		super(canID);
		debug = debug_;
		initialize( );
	}

	private void initialize( ) {
		if ( initalized == false ) {
			initalized = true;
			TalonControlMode orig_mode = super.getControlMode( );
			super.changeControlMode(TalonControlMode.Position);
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
			if ( debug ) { System.out.println("set(" + outputValue + ") -> super.set(" + (outputValue-offset) + ")"); }
			super.set(outputValue - offset);
		} else { super.set(outputValue); }
	}

	public void changeControlMode(TalonControlMode controlMode) {
		if ( controlMode == TalonControlMode.Position)
			do_translation = true;
		else
			do_translation = false;

		super.changeControlMode(controlMode);
	}

}
