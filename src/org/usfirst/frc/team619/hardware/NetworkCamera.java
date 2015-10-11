package org.usfirst.frc.team619.hardware;

import edu.wpi.first.wpilibj.image.HSLImage;
import edu.wpi.first.wpilibj.image.MonoImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.vision.AxisCamera;
import edu.wpi.first.wpilibj.vision.AxisCamera.WhiteBalance;

public class NetworkCamera {

	private AxisCamera camera;
	
	private String cameraHost = "10.6.19.3";
	
	public NetworkCamera(){
		camera = new AxisCamera(cameraHost);
	}
	
	public int getColorLevel(){
		return camera.getColorLevel();
	}
	
	public int getBrightness(){
		return camera.getBrightness();
	}
	
	public int getCompression(){
		return camera.getCompression();
	}
	
	public int getExposurePriority(){
		return camera.getExposurePriority();
	}
	
	public WhiteBalance getWhiteBalance(){
		return camera.getWhiteBalance();
	}
	
	public HSLImage getImage() throws NIVisionException{
		return camera.getImage();
	}
	
	public MonoImage getGreenPlane() throws NIVisionException{
		return camera.getImage().getGreenPlane();
	}
	
}
