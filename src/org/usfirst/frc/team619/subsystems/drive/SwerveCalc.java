package org.usfirst.frc.team619.subsystems.drive;

import com.kauailabs.nav6.frc.IMU;
import com.kauailabs.nav6.frc.IMUAdvanced;

import edu.wpi.first.wpilibj.SerialPort;

public class SwerveCalc {
	double L;       //   wheelbase (distance from center of front wheel to center of rear wheel)
	double W;       //   trackwidth (distance from left wheel to right wheel)
	double R;       //   diagonal size
	
	SerialPort serial_port;
	IMU imu;                             //NavX
	
	// wheelbase = 
	//     track = distance from left wheel to right wheel
	SwerveCalc( double wheelbase, double trackwidth ) {
		try {
			serial_port = new SerialPort(57600,SerialPort.Port.kMXP);
            imu = new IMU(serial_port);
            } catch(Exception ex) { }

		L = wheelbase;
		W = trackwidth;
		R = Math.sqrt( L*L + W*W );
	}
	
	// get robot centric
	SwerveCalcValue getRobotCentric( double FWD, double STR, double RCW ) {
		return new SwerveCalcValue( new double[]{0,0,0,0}, new double[] {0,0,0,0} );
	}
	// get field centric
	SwerveCalcValue getFieldCentric( double FWD, double STR, double RCW ) {
		//  imu.getYaw( ) returns angle between -180 and 180
		double theta = imu.getYaw( );
		return new SwerveCalcValue( new double[] {0,0,0,0}, new double[] {0,0,0,0} );
	}

}
