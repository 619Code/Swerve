package org.usfirst.frc.team619.subsystems.drive;

import com.kauailabs.nav6.frc.IMU;

import edu.wpi.first.wpilibj.SerialPort;
import static java.lang.Math.*;

public class SwerveCalc {
	double L;       //   wheelbase (distance from center of front wheel to center of rear wheel)
	double W;       //   trackwidth (distance from left wheel to right wheel)
	double R;       //   diagonal size
	
	SerialPort serial_port;
	IMU imu;                             //NavX
	
	// wheelbase = 
	//     track = distance from left wheel to right wheel
	public SwerveCalc( double wheelbase, double trackwidth ) {
		try {
			serial_port = new SerialPort(57600,SerialPort.Port.kMXP);
            imu = new IMU(serial_port);
            } catch(Exception ex) { }

		L = wheelbase;
		W = trackwidth;
		R = Math.sqrt( L*L + W*W );
	}
	
	// get robot centric
	public SwerveCalcValue getRobotCentric( double FWD, double STR, double RCW ) {
		double A = STR - RCW*(L/R);
		double B = STR + RCW*(L/R);
		double C = FWD - RCW*(W/R);
		double D = FWD + RCW*(W/R);
		// order of wheels is:
		//     { front_right, front_left, rear_left, rear_right }
		double[] angles = new double[]{ atan2(B,C)*180/PI,
										atan2(B,D)*180/PI,
										atan2(A,D)*180/PI,
										atan2(A,C)*180/PI };
		
		double[] speeds = new double[]{ sqrt(B*B+C*C),
										sqrt(B*B+D*D),
										sqrt(A*A+D*D),
										sqrt(A*A+C*C) };
		
		double max = speeds[0];
		if ( speeds[1] > max ) max = speeds[1];
		if ( speeds[2] > max ) max = speeds[2];
		if ( speeds[3] > max ) max = speeds[3];
		
		if ( max > 1 ) {
			speeds[0] /= max;
			speeds[1] /= max;
			speeds[2] /= max;
			speeds[3] /= max;
		}
		return new SwerveCalcValue( angles, speeds );
	}
	// get field centric
	public SwerveCalcValue getFieldCentric( double FWD, double STR, double RCW ) {
		//  imu.getYaw( ) returns angle between -180 and 180
		double theta = imu.getYaw( );
		theta = toRadians(theta < 0 ? theta+360 : theta);
		double temp = FWD*cos(theta) + STR*sin(theta);
		STR = -FWD*sin(theta) + STR*cos(theta);
		FWD = temp;
		return getRobotCentric(FWD,STR,RCW);
	}

}
