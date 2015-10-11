package org.usfirst.frc.team619.hardware;

import edu.wpi.first.wpilibj.Encoder;

public class DigitalEncoder {

	protected Encoder encoder;
	
	protected int channelA;
	protected int channelB;
	
	public DigitalEncoder(int channel1, int channel2 /*channels on the DIO section of Athena*/){
		encoder = new Encoder(channel1, channel2);
		
		channelA = channel1;
		channelB = channel2;
	}
	
	public DigitalEncoder(int channel1, int channel2, boolean reversed){
		encoder = new Encoder(channel1, channel2, reversed);
		
		channelA = channel1;
		channelB = channel2;
		
	}
	
	public int get(){
		return encoder.get();
	}
	
	public double getRate(){
		return encoder.getRate();		
	}
	
	public boolean getStopped(){
		return encoder.getStopped();
	}
	
	public int getEncodingScale(){
		return encoder.getEncodingScale();
	}
	
	public int getChannelA(){
		return channelA;
	}
	
	public int getChannelB(){
		return channelB;
	}
	
}
