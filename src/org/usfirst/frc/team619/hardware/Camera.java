package org.usfirst.frc.team619.hardware;

import edu.wpi.first.wpilibj.CameraServer;

public class Camera {

	CameraServer camera;
	
	String cameraName;
	
	/*this can be found by accessing the webdashboard with the camera plugged into Athena*/
	public Camera(String cameraName){
		this.cameraName = cameraName;
		camera = CameraServer.getInstance();
		camera.setQuality(50);
		camera.startAutomaticCapture(cameraName);
	}
	
	public boolean isOn(){
		return camera.isAutoCaptureStarted();
	}
	
	public int getQuality(){
		return camera.getQuality();
	}
	
	public String getName(){
		return cameraName;
	}
	
	public CameraServer getInstance(){
		return CameraServer.getInstance();
	}
	
	public void setOn(){
		camera.startAutomaticCapture(cameraName);
	}
	
	public void setQuality(int quality){
		camera.setQuality(quality);
	}
	
}
