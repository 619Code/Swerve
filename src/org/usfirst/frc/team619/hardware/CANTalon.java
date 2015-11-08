package org.usfirst.frc.team619.hardware;

/**
 * 
 * The CANTalon object is only to be used with the Talon SRX (the small rectangular one)
 * as it is the only talon that uses CAN
 * 
 * @author Student
 */

public class CANTalon extends edu.wpi.first.wpilibj.CANTalon {
	public CANTalon(int canID){
		super(canID);
	}
}
