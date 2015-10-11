package org.usfirst.frc.team619.subsystems.sensor;

import java.util.ArrayList;

import org.usfirst.frc.team619.hardware.AnalogAccelerometer;
import org.usfirst.frc.team619.hardware.AnalogPotentiometer;
import org.usfirst.frc.team619.hardware.AnalogUltrasonic;
import org.usfirst.frc.team619.hardware.AthenaAccelerometer;
import org.usfirst.frc.team619.hardware.Camera;
import org.usfirst.frc.team619.hardware.DigitalEncoder;
import org.usfirst.frc.team619.hardware.I2CAccelerometer;
import org.usfirst.frc.team619.hardware.NetworkCamera;

public class SensorBase {	
	
	//no need for an ArrayList of Cameras because only a max of two can be put on the Athena
	protected Camera camera;
	protected NetworkCamera networkCamera;
	
	//there is only one I2C port on Athena so no need for ArrayList
	protected I2CAccelerometer i2cAccelerometer;
	
	//there is only one built in accelerometer on Athena, so no need for ArrayList
	protected AthenaAccelerometer athenaAccelerometer;
	
	protected ArrayList<AnalogUltrasonic> ultrasonicList;
	protected ArrayList<AnalogPotentiometer> potentiometerList;
	protected ArrayList<AnalogAccelerometer> accelerometerList;
	
	protected ArrayList<DigitalEncoder> encoderList;
	
	public SensorBase(){//be careful when using this as it bypasses creating the camera
		ultrasonicList = new ArrayList<>();
		potentiometerList = new ArrayList <>();
		accelerometerList = new ArrayList <>();
		encoderList = new ArrayList<>();
	}
	
	public SensorBase(AthenaAccelerometer accelerometer){
		
		athenaAccelerometer = accelerometer;
		
		ultrasonicList = new ArrayList<>();
		potentiometerList = new ArrayList <>();
		accelerometerList = new ArrayList <>();
		encoderList = new ArrayList<>();
		
	}
	
	public SensorBase(I2CAccelerometer accelerometer){
		
		i2cAccelerometer = accelerometer;
		
		ultrasonicList = new ArrayList<>();
		potentiometerList = new ArrayList <>();
		accelerometerList = new ArrayList <>();
		encoderList = new ArrayList<>();
		
	}
	
	public void startCamera(String cameraName){
		camera = new Camera(cameraName);
	}

	public void startNetworkCamera(){
		networkCamera = new NetworkCamera();
	}
	
	public void turnOffCamera(){
		camera = null;
	}
	
	public void turnOffNetworkCamera(){
		networkCamera = null;
	}
	
	public void addEncoder(DigitalEncoder sensor){
		encoderList.add(sensor);
	}
	
	public void addUltrasonicSensor(AnalogUltrasonic sensor){
		ultrasonicList.add(sensor);
	}
	
	public void addAccelerometer(AnalogAccelerometer sensor){
		accelerometerList.add(sensor);
	}
	
	public void addPotentiometer(AnalogPotentiometer sensor){
		potentiometerList.add(sensor);
	}

	public void addI2CAccelerometer(I2CAccelerometer i2cAccelerometer){
		this.i2cAccelerometer = i2cAccelerometer;
	}
	
	public void addAthenaAccelerometer(AthenaAccelerometer athenaAccelerometer){
		this.athenaAccelerometer = athenaAccelerometer;
	}
	
	public Camera getCamera(){
		return camera;
	}
	
	public NetworkCamera getNetworkCamera(){
		return networkCamera;
	}
	
	public AnalogUltrasonic getUltrasonicSensor(int channel){

		for(AnalogUltrasonic sensor : ultrasonicList){
			if(sensor.getChannel() == channel)
				return sensor;
		}
		
		return null;
		
	}
	
	public AnalogAccelerometer getAccelerometer(int channel){
		
		for(AnalogAccelerometer sensor : accelerometerList){
			if(sensor.getChannel() == channel)
				return sensor;
		}
		
		return null;
		
	}
	
	public AnalogPotentiometer getPotentiometer(int channel){
		
		for(AnalogPotentiometer sensor : potentiometerList){
			if(sensor.getChannel() == channel)
				return sensor;
		}
		
		return null;
		
	}
	
	public DigitalEncoder getEncoder(int channel){
		
		for(DigitalEncoder sensor : encoderList){
			if(sensor.getChannelA() == channel || sensor.getChannelB() == channel)
				return sensor;
		}
		
		return null;
		
	}

	public I2CAccelerometer getI2CAccelerometer(){
		return i2cAccelerometer;
	}
	
	public AthenaAccelerometer getAthenaAccelerometer(){
		return athenaAccelerometer;
	}
	
}
