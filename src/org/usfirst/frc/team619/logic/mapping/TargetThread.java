package org.usfirst.frc.team619.logic.mapping;

// java
import java.util.ArrayList;



// robot
import org.usfirst.frc.team619.logic.RobotThread;
import org.usfirst.frc.team619.logic.ThreadManager;
import org.usfirst.frc.team619.subsystems.GripPipeline;


// camera
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;


import edu.wpi.first.wpilibj.PowerDistributionPanel;

// opencv
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Core;

//import org.usfirst.frc.team619.subsystems.GripPipeline;

public class TargetThread extends RobotThread {
	
	CvSink cvSink;
	PowerDistributionPanel pdp;
	CvSource outputStream;
	GripPipeline grip;
	
	ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();
	
	public final int IMG_WIDTH = 160;
	public final int IMG_HEIGHT = 120;
	
	Rect rightangle;
	Rect centangle;
	Rect leftangle;
	int centerX;
	int numRects;
	Mat output;
	
	public final static double[] brightGreenHue = {60.0, 80.0};
	public final static double[] brightGreenSat = {225.0, 255.0};
	public final static double[] brightGreenVal = {85.0, 200.0};
	
	ArrayList<MatOfPoint> findContoursOutput;
	Mat source;
	Mat original;
	
	public TargetThread(PowerDistributionPanel pdp, CvSink cvSink, CvSource outputStream, int period, ThreadManager threadManager) {
		super(period, threadManager);
		grip  = new GripPipeline();
		source = new Mat();
		output = new Mat();
		original = new Mat();
		this.pdp = pdp;
		this.cvSink = cvSink;
		this.outputStream = outputStream;
		start();
	}
	
	protected void cycle() {
		// Targeting cycle
		getRectangles();
		processRectangles();
		displayTargeting();
	}
	
	/**
	 * Returns -1 for r1 being on the left, 0 as overlap, 1 as r1 on the right
	 * @param r1 Rectangle 1
	 * @param r2 Rectangle 2
	 * @return Position of rightangle
	 */
	protected int compareRectangles(Rect r1, Rect r2){
		//  Compare two rectangles:
		//  r1 to the left of r2 returns -1
		//  r1 overlaps r2 returns 0
		//  r1 to the right of r2 returns 1		
		
		int r1Max = r1.x + r1.width;
		int r2Max = r2.x + r2.width;
		
		// Compare X axis
//		if (r1.x <= r2.x){
//		  // r1 is left of r2
//		  if (r1Max >= r2.x){
//			  // Overlapping rectangles
//			  return 0;
//		  } else {
//			  // rectangle 1 is on the left
//			  return -1;
//		  }
//		} else {
//			// r1 to the right of r2
//			if(r2Max >= r1.x){
//				// overlap
//				return 0;
//			} else {
//				/// to the right
//				return 1;
//			}
//		}
		if (r1.x < r2.x) {
			// r1 is left of r2
			if (r1Max < r2.x) //r1 is to the left of r2
				return -1;
		} else if (r1.x > r2.x) { 
			// r1 to the right of r2
			if(r2Max < r1.x) // r2 is to the right of r1
				return 1;
		}
		//Overlapping/default param
		return 0;
	}
	
	protected void getRectangles(){
		// Get the bounding rectangles from the current image
		// store all the rectangles in the findContoursOutput variable
		
		cvSink.grabFrame(source);
		original = source;
		
		
		//contour vars
		findContoursOutput = new ArrayList<MatOfPoint>();
		
		// why are we doing this?
		ArrayList<MatOfPoint> filterContoursContours = findContoursOutput;
		
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
		
//		double[] hue = {40.0, 100.0};
//		double[] sat = {200.0, 255.0};
//		double[] val = {20.0, 90.0};
		double[] hue = brightGreenHue;
		double[] sat = brightGreenSat;
		double[] val = brightGreenVal;
		
		//change colors 0_o
//		int blue = 1;
//		int blueMod = 1;
//		
//		int green = 1;
//		int greenMod = 1;
//		
//		int red = 1;
//		int redMod = 1;
		//System.out.println("Target Thread Synced");
		grip.hsvThreshold(source, hue, sat, val, output);
		grip.desaturate(output, output);
		grip.findContours(output, false, findContoursOutput);
		grip.filterContours(filterContoursContours, filterContoursMinArea, 
							filterContoursMinPerimeter, filterContoursMinWidth, 
							filterContoursMaxWidth, filterContoursMinHeight, 
							filterContoursMaxHeight, filterContoursSolidity, 
							filterContoursMaxVertices, filterContoursMinVertices, 
							filterContoursMinRatio, filterContoursMaxRatio, 
							filterContoursOutput);
	}
	
	protected void processRectangles(){
		// Iterates through the rectangles
		// identifies which rectangle is left and right
		// merges other rectangles together into the left or right rectangle
		numRects = filterContoursOutput.size();
		//none, one or both rectangles are visible
//		numRects = totalRectangles;
//		if(numRects > 2)
//			numRects = 2;
//		System.out.println("Found " + numRects + " rectangles");
		
//		for (int i = 0; i < numRects; i++){
//			Rect boundingRectangle = Imgproc.boundingRect(filterContoursOutput.get(i));
//			System.out.println("Rect#"+i+":"+rectangleDescription(boundingRectangle));
//			
//			// Check if leftangle is defined
//			if (leftangle == null){
//				// Assign it to the leftangle
//				leftangle = boundingRectangle;
//			} else {
//				switch(compareRectanglesLeftOrRight(leftangle, boundingRectangle)){
//				case -1:
//					// leftRectangle is on the far left of bounding rectangle
//					if (rightangle == null) {
//						rightangle = boundingRectangle;
//					} else {
//						rightangle = mergeRectangles(rightangle, boundingRectangle);
//					}
//					break;
//				case 0:
//					System.out.println("lr merge of br");
//					// Left Rectangle overlaps rectangle
//					// Merge the rectangles
//					rightangle = boundingRectangle;
//					break;
//				case 1:
//					// left rectangle is to the right of bounding rectangle
//					// swap rectangles
//					System.out.println("swap");
//					if (leftangle == null){
//						rightangle = leftangle;
//						leftangle = boundingRectangle;
//					} else {
//						// Weird edge case
//						// Right Rectangle already there
//						// re target to the left?
//						// need to test
//						rightangle = leftangle;
//						leftangle = boundingRectangle;
//					}
//					break;
//				}
//			}
//		}
			if(numRects >= 1)
				leftangle = Imgproc.boundingRect(filterContoursOutput.get(0));
			else
				leftangle = null;
			if(numRects >= 2)
				rightangle = Imgproc.boundingRect(filterContoursOutput.get(1));
			else
				rightangle = null;
			if(leftangle != null && rightangle != null) {
				switch(compareRectangles(leftangle, rightangle)) {
				case -1:
					//Leftangle is in the right place
					break;
				case 0:
					//overlap, careful with this
					break;
				case 1:
					//rectangles are switched
					Rect temp = leftangle;
					leftangle = rightangle;
					rightangle = temp;
					break;
				}
				centangle = mergeRectangles(leftangle, rightangle);
			}else
				centangle = null;
//		for (int i = 0; i < totalRectangles; i++){
//			Rect boundingRectangle = Imgproc.boundingRect(filterContoursOutput.get(i));
//			
//			// Check if leftRectangle is defined
//			if (leftRectangle == null){
//				// Assign it to the leftRectangle
//				leftRectangle = boundingRectangle;
//			} else {
//				switch(compareRectanglesLeftOrRight(leftRectangle, boundingRectangle)){
//				case -1:
//					// leftRectangle is on the far left of bounding rectangle
//					if (rightRectangle == null) {
//						rightRectangle = boundingRectangle;
//					} else {
//						rightRectangle = mergeRectangles(rightRectangle, boundingRectangle);
//					}
//				case 0:
//					// Left Rectangle overlaps rectangle
//					// Merge the rectangles
//					leftRectangle = mergeRectangles(leftRectangle, boundingRectangle);
//				case 1:
//					// left rectangle is to the right of bounding rectangle
//					// swap rectangles
//					if (rightRectangle == null){
//						rightRectangle = leftRectangle;
//						leftRectangle = boundingRectangle;
//					} else {
//						// Weird edge case
//						// Right Rectangle already there
//						// re target to the left?
//						// need to test
//						rightRectangle = leftRectangle;
//						leftRectangle = boundingRectangle;
//					}
//				}
//			}
		//Find the center between both rectangles
//		try {
//		centerX = (rightangle.x + (centangle.x+centangle.width))/2;
//		}catch(NullPointerException e) { System.out.println(" NULL  RETCNAGLE BUT IGNORE PLS NOT IMPORTANT"); }
	}
	
	
	protected Rect mergeRectangles(Rect r1, Rect r2){
		// Take the minimum values for x,y coordinates for top left
		// Take the maximum values for x,y coordinates for bottom right
		
		return new Rect(new Point((double) Math.min(r1.x, r2.x), (double) Math.min(r1.y, r2.y)), 
						new Point((double) Math.max(r1.x + r1.width, r2.x + r2.width), (double) Math.max(r1.y + r1.height, r2.y + r2.height)));
	}
	
	protected String rectangleDescription(Rect r){
		if (r == null){
			return "(null)";
		} else {
			return "(" + r.x + "," + r.y + ") h:" + r.height + " w:" + r.width;
		}
	}
	
	protected void drawRectangle(Mat source, Rect rect, Scalar color) {
		// Draw a red rectangle from the top left (tl) to the bottom right (br) of the rect on the source image
		if (rect != null) {
			Imgproc.rectangle(source,rect.tl(), rect.br(), color);
		}else {
			System.out.println("THE THING IS NULL " + color);
			source = original;
		}
	}
	
	protected void displayTargeting(){
//		try {
//			if(filterContoursOutput.get(0) != null)
//				drawRectangle(source, rightangle, 0);
//			if(filterContoursOutput.get(1) != null) {
//				drawRectangle(source, centangle, 1);
//				drawRectangle(source, leftangle, 2);
//			}
//		}catch(IndexOutOfBoundsException e) { System.out.println("INDEX OUT OF BOUNDS IGNORE THIS"); }
		
		if (centangle != null) {
			drawRectangle(source, centangle, new Scalar(255, 0, 0));
			int center = (centangle.x + (centangle.x+centangle.width))/2;
			Imgproc.line(source, new Point(center, 0), new Point(center, 120), new Scalar(255, 255, 255));
		}
		
		if (leftangle != null){
			drawRectangle(source, leftangle, new Scalar(0,255,0));
		}
		
		if (rightangle != null){
			drawRectangle(source, rightangle, new Scalar(0,0,255));
		}
		
		Imgproc.line(source, new Point(80,0), new Point(80, 120), new Scalar(200, 200, 0));
		
		Imgproc.putText(source, rectangleDescription(leftangle), 
				new Point(40,20), 
				Core.FONT_ITALIC, 0.4, 
				new Scalar(255,255,255), 1);
		
//		Imgproc.putText(source, rectangleDescription(centangle), 
//						new Point(40,40),
//						Core.FONT_ITALIC, 0.4,
//						new Scalar(255,255,255), 1);
	
		Imgproc.putText(source, rectangleDescription(rightangle), 
				new Point(40,100), 
				Core.FONT_ITALIC, 0.4, 
				new Scalar(255,255,255), 1);

		//outputStream.putFrame(source);
	}
    
    //return number of rectangles in an image
    public int getNumRects() {
    	return numRects;
    }
    
    public Rect getLeftangle() {
    	return leftangle;
    }
    
    public Rect getCentangle() {
    	return centangle;
    }
    
    public Rect getRightangle() {
    	return rightangle;
    }
    
    public double getCenter() {
    	return centerX;
    }
    
    public boolean isRunning() {
    	return isRunning;
    }
//    
//    public int getRectWidth(int rectIndex) {
//    	int width = 0;
//    	if(rectIndex == 0 && rightangle != null)
//    		width = rightangle.width;
//    	else if(centangle != null)
//    		width = centangle.width;
//    	return width;
//    }
//    
//    public int getRectHeight(int rectIndex) {
//    	int height = 0;
//    	if(rectIndex == 0 && rightangle != null)
//    		height = rightangle.height;
//    	else if(centangle != null)
//    		height = centangle.height;
//    	return height;
//    }
//    
//    public int getRectX(int rectIndex) {
//    	int x = 0;
//    	if(rectIndex == 0 && rightangle != null)
//    		x = rightangle.x;
//    	else if(centangle != null)
//    		x = centangle.x;
//    	return x;
//    }
//    
//    public int getRectY(int rectIndex) {
//    	int y = 0;
//    	if(rectIndex == 0 && rightangle != null)
//    		y = rightangle.y;
//    	else if(centangle != null)
//    		y = centangle.y;
//    	return y;
//    }
}