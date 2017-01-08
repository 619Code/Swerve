package org.usfirst.frc.team619.hardware;

import edu.wpi.first.wpilibj.CameraServer;

public class NetworkCamera {

	private CameraServer cameraServer;
	
	
	public NetworkCamera(){
		cameraServer = CameraServer.getInstance();
		
		try {
			cameraServer.startAutomaticCapture();
		}catch(Exception e) {}
	}
}
