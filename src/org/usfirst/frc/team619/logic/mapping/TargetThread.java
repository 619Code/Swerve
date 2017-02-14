package org.usfirst.frc.team619.logic.mapping;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team619.logic.RobotThread;
import org.usfirst.frc.team619.logic.ThreadManager;
import org.usfirst.frc.team619.robot.SwerveTest;
import org.usfirst.frc.team619.subsystems.GripPipeline;
import org.usfirst.frc.team619.subsystems.drive.SwerveDriveBase;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TargetThread extends RobotThread {

	Mat source = new Mat();
	Mat output = new Mat();
	long time = 0;
	int value = 0;
	private double centerX = 0;
	private int height = 0;
	private int size = 0;
	private int xVal = 0;
	private final Object imgLock = new Object();
	private Rect r, r2;
	public final int IMG_WIDTH = 160;
	public final int IMG_HEIGHT = 120;
	
	int rect1Width = 0;
	int rect1Height = 0;
	
	int rect2Width = 0;
	int rect2Height = 0;
	
	int rect1X = 0;
	int rect1Y = 0;
	
	int rect2X = 0;
	int rect2Y = 0;
	
	GripPipeline grip;
	
	CvSink cvSink;
	CvSource outputStream;
	

	public TargetThread(int period, ThreadManager threadManager,
						CvSink cvSink, CvSource outputStream) {
		super(period, threadManager);
		grip  = new GripPipeline();
		this.cvSink = cvSink;
		this.outputStream = outputStream;
		start();
	}

	protected void cycle() {    	
		time = System.currentTimeMillis();
		cvSink.grabFrame(source);
	
		//contour vars
		ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
		ArrayList<MatOfPoint> filterContoursContours = findContoursOutput;
		ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();
		double filterContoursMinArea = 0.0;
		double filterContoursMinPerimeter = 0.0;
		double filterContoursMinWidth = 0.0;
		double filterContoursMaxWidth = 1000;
		double filterContoursMinHeight = 10;
		double filterContoursMaxHeight = 1000;
		double[] filterContoursSolidity = {0, 100};
		double filterContoursMaxVertices = 1000000;
		double filterContoursMinVertices = 0;
		double filterContoursMinRatio = 0;
		double filterContoursMaxRatio = 1000;
		
		double[] hue = {40.0, 100.0};
		double[] sat = {200.0, 255.0};
		double[] val = {20.0, 90.0};
		
		//change colors 0_o
		int blue = 1;
		int blueMod = 1;
		
		int green = 1;
		int greenMod = 1;
		
		int red = 1;
		int redMod = 1;
		try {
			grip.hsvThreshold(source, hue, sat, val, output);
			grip.desaturate(output, output);
			grip.findContours(output, false, findContoursOutput);
			grip.filterContours(filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter, filterContoursMinWidth, 
						filterContoursMaxWidth, filterContoursMinHeight, filterContoursMaxHeight, filterContoursSolidity, 
						filterContoursMaxVertices, filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio, filterContoursOutput);
            
			r = Imgproc.boundingRect(filterContoursOutput.get(0));
			r2 = Imgproc.boundingRect(filterContoursOutput.get(1));
			
			rect1Width = r.width;
			rect1Height = r.height;
			
			rect2Width = r2.width;
			rect2Height = r2.height;
			
			rect1X = r.x;
			rect1Y = r.y;
			
			rect2X = r2.x;
			rect2Y = r2.y;
			
		}catch(Exception e) {}
			
		//change colors 0_o
		if(blue >= 255 || blue <= 0){
			blueMod *= -1;
		}
		blue += 8*blueMod;
		
		if(green >= 255 || green <= 0){
			greenMod *= -1;
		}
		green += 12*greenMod;
		
		if(red >= 255 || red <= 0){
			redMod *= -1;
		}
		red += 16*redMod;
		size = 0;
		
		if(filterContoursOutput.size() != 0 && r != null && r2 != null) {
			synchronized(imgLock) {
				size = filterContoursOutput.size();
				centerX = (r.x+r2.x)/2;
				height = r.height;
			}
//			Imgproc.rectangle(source, new Point(r.x, r.y), new Point(r.x+r.width, r.y+r.height), new Scalar(0, 0, 255));
//			if(filterContoursOutput.size() > 1) {
//				Imgproc.rectangle(source, new Point(r2.x, r2.y), new Point(r2.x+r2.width, r2.y+r2.height), new Scalar(0, 0, 255));
//			}
			
			for(int i=0; i < filterContoursOutput.size(); i++) {
				Rect rect = Imgproc.boundingRect(filterContoursOutput.get(i));
				
				Imgproc.rectangle(source, new Point(rect.x, rect.y), new Point(rect.x+rect.width, rect.y+rect.height), new Scalar(0, 0, 255));
				int yesMan = rect.x + rect.width;
//				System.out.println("Width: " + yesMan);
//				System.out.println("Height: " + rect.height);
//				System.out.println("Size: " + size);
				Imgproc.putText(source, "(" + rect.x + ", " + rect.y + ") " + size, new Point(output.rows()/8,output.cols()/8), Core.FONT_ITALIC, 0.4, new Scalar(255,255,255), 1);
				Imgproc.putText(source, "h: " + rect.height + " w: " + rect.width, new Point(output.rows()/8,output.cols()/4), Core.FONT_ITALIC, 0.4, new Scalar(255,255,255), 1);
				
			}
		}
		time = System.currentTimeMillis() - time;
		double newTime = 1000/((double)time);
		//Imgproc.putText(source, "" + (int)newTime + " fps", new Point(output.rows()/8,output.cols()/4), Core.FONT_ITALIC, 0.4, new Scalar(255,255,255), 1);
    	System.out.println(output.rows()/8);
		outputStream.putFrame(source);
	}
	
    public int getHeight(int rectIndex) {
    	return height;
    }
    
    public int getSize() {
    	return size;
    }
    
    //return number of rectangles in an image
    public int getNumRects() {
    	return size;
    }
    
    public double getCenter() {
    	return centerX;
    }
    
    public boolean isRunning() {
    	return isRunning;
    }
    
    public int getRectWidth(int rectIndex) {
    	if(rectIndex == 0){
    		return rect1Width;
    	}else{
    		return rect2Width;
    	}
    }
    
    public int getRectHeight(int rectIndex) {
    	if(rectIndex == 0){
    		return rect1Height;
    	}else{
    		return rect2Height;
    	}
    }
    
    public int getRectX(int rectIndex) {
    	if(rectIndex == 0){
    		return rect1X;
    	}else{
    		return rect2X;
    	}
    }
    
    public int getRectY(int rectIndex) {
    	if(rectIndex == 0){
    		return rect1Y;
    	}else{
    		return rect2Y;
    	}
    }

}
