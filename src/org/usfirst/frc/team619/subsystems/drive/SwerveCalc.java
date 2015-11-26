package org.usfirst.frc.team619.subsystems.drive;

public class SwerveCalc {
	double L;       //   wheelbase (distance from center of front wheel to center of rear wheel)
	double W;       //   trackwidth (distance from left wheel to right wheel)
	double R;       //   diagonal size
	// wheelbase = 
	//     track = distance from left wheel to right wheel
	SwerveCalc( double wheelbase, double trackwidth ) {
		L = wheelbase;
		W = trackwidth;
		R = Math.sqrt( L*L + W*W );
	}
	
	//  theta = Gyro angle...
	void update( double theta ) { }

}
