  package org.usfirst.frc.team619.subsystems.drive;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

import com.ctre.CANTalon;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SwerveDriveBase  {

    //must also use a wheel class

    CANTalon driveLFMotor;
    CANTalon rotateLFMotor;
    //Front Right Wheel
    CANTalon driveRFMotor;
    CANTalon rotateRFMotor;
    //Back Left Wheel
    CANTalon driveLBMotor;
    CANTalon rotateLBMotor;
    //Back Right Wheel
    CANTalon driveRBMotor;
    CANTalon rotateRBMotor;


    //NavX
    AHRS imu;

    int encoderUnitsPerRotation = 1660;//was 1665

    double orientationOffset;

    //Used to switch between control modes
    boolean isRobotCentric = false;
    boolean isFieldCentric = true;
    boolean isGearCentric = false;
    boolean isObjectCentric = false;
    boolean isHookCentric = false;
    boolean compensateDrift = false;

    double radius = 55;
    double headerAngle;
    int maxDrift = 5;

    SwerveWheel[] wheelArray;

    SwerveWheel frontLeft;
    SwerveWheel frontRight;
    SwerveWheel backLeft;
    SwerveWheel backRight;

    double angleToDiagonal = 0;
    double L;       //   wheelbase (distance from center of front wheel to center of rear wheel)
    double W;       //   trackwidth (distance from left wheel to right wheel)
    double R;       //   diagonal size

    private double[] speeds;
    private double[] angles;


    //Used for Autonomous control
    /***************************************<DRS:Autonomous>*******************************************************
    PIDOutputObject rotatePIDOutput = new PIDOutputObject();
    PIDInputObject rotatePIDInput = new PIDInputObject();
    PIDController rotationPID = new PIDController(5, 0, 8, rotatePIDInput, rotatePIDOutput);// p=5 i=0 d=10
    ***************************************************************************************************************/

    /*give dimensions between the wheels both width and length,
     * width is the distance between left wheels and right wheels,
     *  length is the distance between front wheels and back wheels*/
    /*give dimensions between the wheels both width and length,
     * width is the distance between left wheels and right wheels,
     * @param width Width of the robot, distance(inches) between left and right wheels
     *      int backRightRotateID, double width, double length){
     */

    /**
     * Initializes one SwerveControl Object
     * @param frontLeftDriveChannel PWM Port for Talon that drives the Front Left Swerve Wheel
     * @param frontLeftRotateID CANBus ID for Front Left Swerve Wheel
     * @param frontRightDriveChannel PWM Port for Talon that drives the Front Left Right Wheel
     * @param frontRightRotateID CANBus ID for Front Right Swerve Wheel
     * @param backLeftDriveChannel  PWM Port for Talon that drives the Back Left Swerve Wheel
     * @param backLeftRotateID CANBus ID for Back Left Swerve Wheel
     * @param backRightDriveChannel  PWM Port for Talon that drives the Back Right Swerve Wheel
     * @param backRightRotateID  CANBus ID for Back Right Swerve Wheel
     * @param width Width of the robot, distance(inches) between left and right wheels
     * @param length Length of the robot, distance(inches) between front and back wheels
     */

    public SwerveDriveBase(SwerveWheel frontLeft_, SwerveWheel frontRight_,
            SwerveWheel backLeft_, SwerveWheel backRight_, double width, double length){

        W = width;
        L = length;
        R = Math.sqrt( L*L + W*W );

        //used to establish rotation angles for all four wheels
        angleToDiagonal = Math.toDegrees(Math.atan2(length, width));


        frontLeft = frontLeft_;
        frontRight = frontRight_;
        backLeft = backLeft_;
        backRight = backRight_;

//      frontLeft.initalizePosition((270 - angleToDiagonal), 205);
//      frontRight.initalizePosition((angleToDiagonal + 90), 205);
//      backLeft.initalizePosition((angleToDiagonal + 270), 0);
//      backRight.initalizePosition((90 - angleToDiagonal), 0);


        /*
        FLWheel = new SwerveWheel(frontLeftDriveChannel, frontLeftRotateID, p, i, d, (180 - angleToDiagonal), 0);
        FRWheel = new SwerveWheel(frontRightDriveChannel, frontRightRotateID, p, i, d, (angleToDiagonal), 0);
        BLWheel = new SwerveWheel(backLeftDriveChannel, backLeftRotateID, p, i, d, (angleToDiagonal + 180), 0);
        BRWheel = new SwerveWheel(backRightDriveChannel, backRightRotateID, p, i, d, (0 - angleToDiagonal), 0);
        */

        wheelArray = new SwerveWheel[]{ frontRight, frontLeft, backLeft, backRight };

        try {
            /* Communicate w/navX-MXP via the MXP SPI Bus.                                     */
            /* AlternativeRY:  I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB     */
            /* See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/ for details. */
            imu = new AHRS(SPI.Port.kMXP);
        } catch (RuntimeException ex ) {
            System.out.println("Error instantiating navX-MXP:  " + ex.getMessage());
        }
        headerAngle = imu.getAngle();

        initPIDControllers();
    }


    /***********************
     *  Autonomous Methods *
     ***********************/

    /**
     * Used in Autonomous Mode onRY, Rotates robot a certain angle from the current position
     * @param targetAngle Angle(degrees) from current position of robot, which robot will rotate to
     */

    public void initPIDControllers(){
        //rotationPID.enable();
        //rotationPID.setInputRange(-180, 180); //sets input range from 0 to 360(degrees)
        //rotationPID.setOutputRange(-0.5, 0.5); //sets output range from -1 to 1(max rotation values)
        //rotationPID.setContinuous();
        updatePIDControllers();
    }

    public void updatePIDControllers(){
        //rotatePIDInput.setValue(imu.getYaw());
        //SmartDashboard.putNumber("PIDInput Value: ", rotatePIDInput.pidGet());
    }


    public void relativeRotateRobot(double angle){
        SmartDashboard.putNumber("Delta Angle", angle);
        double currentAngle = imu.getYaw();
        SmartDashboard.putNumber("Current Angle:", currentAngle);
        double targetAngle = currentAngle + angle;

        if(targetAngle >= 180){
            targetAngle -= 360;
        } else if(targetAngle < -180){
            targetAngle += 360;
        }
        SmartDashboard.putNumber("Target Angle: ", targetAngle);
        updatePIDControllers();
        while(Math.abs(currentAngle - targetAngle) >= 2){ //waits until we are within range of the angle
            //rotationPID.setSetpoint(targetAngle); //tells PID loop to go to the targetAngle
            currentAngle = imu.getYaw();
            updatePIDControllers();
            //calculateSwerveControl(0,0,0.2);
            calculateSwerveControl(0, 0, 0 /*rotatePIDOutput.getPIDValue()*/); //sets the wheels to rotate based off PID loop
            try{
                Thread.sleep(1);
            } catch(Exception e){
                //Do nothing
            }
        }
        calculateSwerveControl(0,0,0); //stops robot spinning
        SmartDashboard.putNumber("Current Angle:", currentAngle);
    }

    public void relativeMoveRobot(double angle, double speed, double time){
        calculateSwerveControl(Math.sin(Math.toRadians(angle)) * speed, Math.cos(Math.toRadians(angle)) * speed, 0);
        try{
            Thread.sleep((long) (time * 1000));
        }catch(Exception e){
            //Do nothing
        }

        calculateSwerveControl(0, 0, 0);

    }

    /**
     * Used in Autonomous Mode OnRY, Rotates robot to a certain angle regardless of robots current position
     * @param targetAngle Angle(degrees) to which the robot will rotate
     */

    public void absoluteRotateRobot(double targetAngle){
        double currentAngle = imu.getYaw();
        if(targetAngle >= 180){
            targetAngle-=360;
        } else if(targetAngle < -180){
            targetAngle +=360;
        }
        updatePIDControllers();
        while(Math.abs(currentAngle - targetAngle) >= 1){//waits until we are within range of our target angle
            //rotationPID.setSetpoint(targetAngle);//tells PID loop to go to the target angle
            currentAngle = imu.getYaw();
            SmartDashboard.putNumber("Absolute Current Angle", currentAngle);
            updatePIDControllers();
            calculateSwerveControl(0, 0, 0 /*rotatePIDOutput.getPIDValue()*/);//sets the wheels to rotate based off PID loop
            try{
                Thread.sleep(10);
            } catch(Exception e){
                //Do nothing
            }
        }
        calculateSwerveControl(0,0,0); //stops robot spinning
    }

    /***********************
     * Calculation Methods *
     ***********************/

    public int angleToEncoderUnit(double angle){//OnRY pass in deltaTheta

        double deltaEncoder;
        deltaEncoder = angle*(encoderUnitsPerRotation/360.0);

        return (int)deltaEncoder;
    }

    /***********************
     * Swerve Move Methods *
     **********************/

    /**
     * Calls the correct movement method based on control mode
     * @param RY Right stick Y Axis
     * @param RX Right stick X Axis
     * @param LX Left stick X Axis
     */

    public void move(double RY, double RX, double LX){
        if(isRobotCentric){
        	if(compensateDrift == true)
        		compensateDrift(RY, RX, LX);
        	else
        		calculateSwerveControl(RY, RX, LX);
        } else if(isFieldCentric){
            getFieldCentric(RY, RX, LX);
        }else if(isGearCentric){
            calculateSwerveControl(RX, -RY, LX);
        }else {
        	calculateSwerveControl(RY, RX, LX);
        }

    }
    
    private void compensateDrift(double RY, double RX, double LY) {
//    	if(LY == 0.0) {
//    		//Drift stuff
//    	}else {
//    		headerAngle = imu.getYaw();
//    		if(headerAngle < 360)
//    	}
    		
    }

    /**
     * Called by move command, controls both field centric and robot centric modes
     * @param RY Right stick Y Axis
     * @param RX Right stick X Axis
     * @param LX Left stick X Axis
     */

    public void calculateSwerveControl(double RY, double RX, double LX){

        // correspondence to paper http://www.chiefdelphi.com/media/papers/download/3028
        //     RY  <=>   FWD
        //     RX  <=>   STR
        //     LX  <=>   RCW

        //math for rotation vector, different for every wheel so we calculate for each one seperately
        double A = RX - LX*(L/R);
        double B = RX + LX*(L/R);
        double C = RY - LX*(W/R);
        double D = RY + LX*(W/R);
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

        if( abs(RY) < 0.05 && abs(RX) < 0.05 && abs(LX) < 0.05){
            // if our inputs are nothing, don't change the angle(use currentAngle as targetAngle)
//            for( SwerveWheel wheel : wheelArray )
//                wheel.setTargetAngle(wheel.getCurrentAngle( ));
        	for(SwerveWheel wheel : wheelArray) {
                wheel.setSpeed(0);
        	}
        } else {
            //Set target angle
            for( int i=0; i < wheelArray.length; ++i ) {
                wheelArray[i].setTargetAngle(angles[i]);
                wheelArray[i].setSpeed(speeds[i]);
            }
            SmartDashboard.putNumber("Angle", angles[0]);
        }

        //Makes the wheels go to calculated target angle and speed
        for(SwerveWheel wheel : wheelArray)
            wheel.goToAngle();
        for(SwerveWheel wheel : wheelArray)
            wheel.drive();

    }

    public void getFieldCentric( double RY, double RX, double LX ) {
        // correspondence to paper http://www.chiefdelphi.com/media/papers/download/3028
        //     RY  <=>   FWD
        //     RX  <=>   STR
        //     LX  <=>   RCW

        //  imu.getYaw( ) returns angle between -180 and 180
        double theta = imu.getYaw( );
        //System.out.println("Theta: " + theta);
        while ( theta < 0 ) theta += 360;
        theta = toRadians(theta);
        double temp = RY*cos(theta) + RX*sin(theta);
        RX = -RY*sin(theta) + RX*cos(theta);
        RY = temp;

        calculateSwerveControl(RY, RX, LX);
    }



        /*
         * FOR TESTING PURPOSES
         *

        //double FRWheelTarget = FRWheel.rotateMotor.getEncPosition() + angleToEncoderUnit(FRWheel.getDeltaTheta());
        .rotateMotor.set(FRWheel.rotateMotor.getEncPosition() + angleToEncoderUnit(FRWheel.getDeltaTheta()));
        SmartDashboard.putNumber("FR Target Encoder Position", (FRWheel.getTargetAngle()));
        SmartDashboard.putNumber("FR DeltaTheta: ", angleToEncoderUnit(FRWheel.getDeltaTheta()));
        SmartDashboard.putNumber("FR Current Encoder", FRWheel.getCurrentAngle());
        try{
            //Thread.sleep(5000);
        }catch (Exception ex){

        }

        FLWheel.rotateMotor.set(FLWheel.rotateMotor.getEncPosition() + angleToEncoderUnit(FLWheel.getDeltaTheta()));
        SmartDashboard.putNumber("FL Target Encoder Position", (FLWheel.rotateMotor.getEncPosition() + angleToEncoderUnit(FLWheel.getDeltaTheta())));
        SmartDashboard.putNumber("FL DeltaTheta: ", FLWheel.getDeltaTheta());
        BRWheel.rotateMotor.set(BRWheel.rotateMotor.getEncPosition() + angleToEncoderUnit(BRWheel.getDeltaTheta()));
        SmartDashboard.putNumber("BR Target Encoder Position", (BRWheel.rotateMotor.getEncPosition() + angleToEncoderUnit(BRWheel.getDeltaTheta())));
        SmartDashboard.putNumber("BR DeltaTheta: ", BRWheel.getDeltaTheta());
        BLWheel.rotateMotor.set(BLWheel.rotateMotor.getEncPosition() + angleToEncoderUnit(BLWheel.getDeltaTheta()));
        SmartDashboard.putNumber("BL Target Encoder Position", (BLWheel.rotateMotor.getEncPosition() + angleToEncoderUnit(BLWheel.getDeltaTheta())));
        SmartDashboard.putNumber("BL DeltaTheta: ", BLWheel.getDeltaTheta());

        SmartDashboard.putNumber("Current Angle", BLWheel.getCurrentAngle());
        SmartDashboard.putNumber("Delta Theta", BLWheel.getDeltaTheta());
        SmartDashboard.putNumber("Target Angle", BLWheel.getTargetAngle());
        */

    /**
     * Called by move command, controls object centric mode
     * @param LX Right stick X Axis
     */

    public void calculateObjectControl(double LX){
        double distanceToFront = radius - L/2;
        double distanceToBack = radius + L/2;

        frontLeft.setTargetAngle(180 - Math.toDegrees(Math.atan2(W/2, distanceToFront)));
        frontRight.setTargetAngle(180 + Math.toDegrees(Math.atan2(W/2, distanceToFront)));
        backLeft.setTargetAngle(180 - Math.toDegrees(Math.atan2(W/2, distanceToBack)));
        backRight.setTargetAngle(180 + Math.toDegrees(Math.atan2(W/2, distanceToBack)));

        backLeft.setSpeed(LX);
        backRight.setSpeed(LX);

        double speedRatio = Math.sqrt(Math.pow((W/2), 2) + Math.pow(distanceToFront, 2)) / Math.sqrt(Math.pow((W/2), 2) + Math.pow(distanceToBack, 2));

        frontLeft.setSpeed(speedRatio * LX);
        frontRight.setSpeed(speedRatio * LX);

        frontRight.goToAngle();
        frontLeft.goToAngle();
        backRight.goToAngle();
        backLeft.goToAngle();

        frontRight.drive();
        frontLeft.drive();
        backRight.drive();
        backLeft.drive();

    }


    public void calculateHookControl(double LX){
        double distanceToFront = 38 - L/2;
        double distanceToBack = 38 + L/2;

        frontLeft.setTargetAngle(180 - Math.toDegrees(Math.atan2(W/2, distanceToFront)));
        frontRight.setTargetAngle(180 + Math.toDegrees(Math.atan2(W/2, distanceToFront)));
        backLeft.setTargetAngle(180 - Math.toDegrees(Math.atan2(W/2, distanceToBack)));
        backRight.setTargetAngle(180 + Math.toDegrees(Math.atan2(W/2, distanceToBack)));

        backLeft.setSpeed(LX);
        backRight.setSpeed(LX);

        double speedRatio = Math.sqrt(Math.pow((W/2), 2) + Math.pow(distanceToFront, 2)) / Math.sqrt(Math.pow((W/2), 2) + Math.pow(distanceToBack, 2));

        frontLeft.setSpeed(speedRatio * LX);
        frontRight.setSpeed(speedRatio * LX);

        frontRight.goToAngle();
        frontLeft.goToAngle();
        backRight.goToAngle();
        backLeft.goToAngle();

        frontRight.drive();
        frontLeft.drive();
        backRight.drive();
        backLeft.drive();

    }

    public void swerveControl(double RY, double RX, double LX, double radius){
        double translationaRXComponent = RX;
        double translationaRYComponent = RY;
        double rAxis = LX;

        double translationalOffset = 0.0;
        if(isFieldCentric){
            translationalOffset = imu.getYaw();
        } else {
            translationalOffset = 0;
        }

        //Same for all wheels so therefore we onRY do the transitional vector math once
        double translationalMagnitude = Math.sqrt(Math.pow(translationaRYComponent, 2) + Math.pow(translationaRXComponent, 2));
        double translationalAngle = Math.toDegrees(Math.atan2(translationaRYComponent, translationaRXComponent));

        translationalAngle += translationalOffset; //sets the robot front to be at the angle determined by orientationOffset
        if(translationalAngle >= 360){
            translationalAngle -= 360;
        } else if(translationalAngle < 0){
            translationalAngle += 360;
        }

        translationaRYComponent = Math.sin(Math.toRadians(translationalAngle)) * translationalMagnitude; //calculates y component of translation vector
        translationaRXComponent = Math.cos(Math.toRadians(translationalAngle)) * translationalMagnitude; //calculates x component of translation vector


        //Deadband
        if(Math.abs(RX) < 0.1) translationaRXComponent = 0;
        if(Math.abs(RY) < 0.1) translationaRYComponent = 0;
        if(Math.abs(LX) < 0.1) rAxis = 0;
        //End Deadband

        double distanceToFront = radius - L/2;
        double distanceToBack = radius + L/2;

        //Calculates wheel's rotational angle based on radius
        frontLeft.setRAngle(180 - Math.toDegrees(Math.atan2(W/2, distanceToFront)));
        frontRight.setRAngle(180 + Math.toDegrees(Math.atan2(W/2, distanceToFront)));
        backLeft.setRAngle(180 - Math.toDegrees(Math.atan2(W/2, distanceToBack)));
        backRight.setRAngle(180 + Math.toDegrees(Math.atan2(W/2, distanceToBack)));

        //Calculate each wheel's rotational speed based on the radius
        //THIS ONRY ALLOWS FOR A POSITVE RADIUS
        double speedRatio = Math.sqrt(Math.pow((W/2), 2) + Math.pow(distanceToFront, 2)) / Math.sqrt(Math.pow((W/2), 2) + Math.pow(distanceToBack, 2));

        backLeft.setSpeed(rAxis);
        backRight.setSpeed(rAxis);
        frontLeft.setSpeed(speedRatio * rAxis);
        frontRight.setSpeed(speedRatio * rAxis);

        /*This seems wrong, copied code from above
        BLWheel.setRSpeed(1);
        BRWheel.setRSpeed(1);
        FLWheel.setRSpeed(speedRatio);
        FRWheel.setRSpeed(speedRatio);
        */
        double fastestSpeed = 0.0;
        for (SwerveWheel wheel : wheelArray){

            double rotateXComponent = Math.cos(Math.toRadians(wheel.getRAngle())); //calculates x component of rotation vector
            double rotateYComponent = Math.sin(Math.toRadians(wheel.getRAngle())); //calculates y component of rotation vector

            if(rAxis > 0){//Why do we do this?
                rotateXComponent = -rotateXComponent;
                rotateYComponent = -rotateYComponent;
            }

            wheel.setSpeed(Math.sqrt(Math.pow(rotateXComponent + translationaRXComponent, 2) + Math.pow((rotateYComponent + translationaRYComponent), 2)));//sets the speed based off translational and rotational vectors
            wheel.setTargetAngle(Math.toDegrees(Math.atan2((rotateYComponent + translationaRYComponent), (rotateXComponent + translationaRXComponent))));//sets the target angle based off translation and rotational vectors

            if(RY == 0 && RX == 0 && LX == 0){//if our inputs are nothing, don't change the angle(use currentAngle as targetAngle)
                wheel.setTargetAngle(wheel.getCurrentAngle());
            }

            if(wheel.getSpeed() > fastestSpeed){//if speed of wheel is greater than the others store the value
                fastestSpeed = wheel.getSpeed();
            }
        }
        System.out.println("Fastest speed is "+fastestSpeed);
        if(fastestSpeed > 1){ //if the fastest speed is greater than 1(our max input) divide the target speed for each wheel by the fastest speed
            for(SwerveWheel wheel : wheelArray){
                wheel.setSpeed(wheel.getSpeed()/fastestSpeed);
            }
        }

        //Makes the wheels go to calculated target angle
        frontRight.goToAngle();
        frontLeft.goToAngle();
        backRight.goToAngle();
        backLeft.goToAngle();
        //Make the wheels drive at their calculated speed
        frontRight.drive();
        frontLeft.drive();
        backRight.drive();
        backLeft.drive();
    }

    /**
     * Change the orientation of the robot in robot centric mode(i.e. changes the left side to become the front)
     * @param north button to make robot front the original front
     * @param east button to make robot front the original right
     * @param south button to make robot front the original back
     * @param west button to make robot front the original left
     */

    public void changeOrientation(boolean north, boolean east, boolean south, boolean west){

        //switch out of field centric
        //set the robot front (N,E,S,W)
        if(north){
            isFieldCentric = false;
            isObjectCentric = false;
            orientationOffset = 0;
        } else if(east){
            isFieldCentric = false;
            isObjectCentric = false;
            orientationOffset = -90;
        } else if(south){
            isFieldCentric = false;
            isObjectCentric = false;
            orientationOffset = 180;
        } else if(west){
            isFieldCentric = false;
            isObjectCentric = false;
            orientationOffset = 90;
        }

    }

    /**
     * Called to switch to field centric mode
     */

    public void switchToFieldCentric(){
        isObjectCentric = false;
        isRobotCentric = false;
        isFieldCentric = true;
        isHookCentric = false;
        isGearCentric = false;
    }
    
    public void switchToGearCentric() {
        isObjectCentric = false;
        isRobotCentric = false;
        isFieldCentric = false;
        isHookCentric = false;
        isGearCentric = true;
    }

    /**
     * Called to switch to object centric mode
     */

    public void switchToObjectCentric(){
        isObjectCentric = true;
        isFieldCentric = false;
        isRobotCentric = false;
        isHookCentric = false;
        isGearCentric = false;
    }
   /**
    * Called to switch to HookCentric
    */
    public void switchToHookCentric(){
        isObjectCentric = false;
        isFieldCentric = false;
        isRobotCentric = false;
        isHookCentric = true;
        isGearCentric = false;
    }

    /**
     * Called to switch to robot centric mode
     */

    public void switchToRobotCentric(){
        isObjectCentric = false;
        isFieldCentric = false;
        isRobotCentric = true;
        isHookCentric = false;
        isGearCentric = false;
    }

    public boolean getRobotCentric() {
        return isRobotCentric;
    }

    public boolean getFieldCentric() {
        return isFieldCentric;
    }
    
    public boolean getGearCentric() {
    	return isGearCentric;
    }

    public float getYaw() {
        return imu.getYaw();
    }

    public void zeroIMU() {
        imu.zeroYaw();
    }

    public void autoZeroWheels() {
	    frontRight.autoZero();
	    frontLeft.autoZero();
	    backLeft.autoZero();
	    backRight.autoZero();
    }
    
    public void goToZero() {
    	frontLeft.setTargetAngle(0);
    	frontLeft.goToAngle();
    }
    
    public void zeroWheels() {
        frontRight.zero();
        frontLeft.zero();
        backLeft.zero();
        backRight.zero();
    }

    /*
    public void switchDrivingMode(boolean LStick, boolean RStick){
        if(LStick && !isFieldCentric){
            isObjectCentric = false;
            isRobotCentric = false;
            isFieldCentric = true;
        } else if(LStick && isFieldCentric){
            isObjectCentric = false;
            isFieldCentric = false;
            isRobotCentric = true;
        } else if(RStick && !isObjectCentric){
            isObjectCentric = true;
            isFieldCentric = false;
            isRobotCentric = false;
        } else if(RStick && isObjectCentric){
            isObjectCentric = false;
            isFieldCentric = false;
            isRobotCentric = true;
        } else{
            isObjectCentric = false;
            isFieldCentric = false;
            isRobotCentric = true;
        }
    }*/

    /********************
     * Calibration Code *
     ********************/

    /**
     * Moves all four swerve wheels to home position(the limit switch)
     */

    private void wheelsToHomePos(){
        /*for (SwerveWheel wheel : wheelArray){
            wheel.goToHome();
        }*/
        //frontRight.goToHome();
        //FRWheel.goToHome();
        //BRWheel.goToHome();
        //BLWheel.goToHome();

    }

    /**
     * Moves all four swerve wheels to zero position(Straight sideways)(0 degrees on the unit circle)
     */

    public void wheelsToZero(){
        // <DRS> uncomment these later...
        //frontRight.goToZero();
        //frontLeft.goToZero();
    }

    private void test(){
        //frontRight.test();
        //FLWheel.test();
        //BLWheel.test();
        //BRWheel.test();
    }
}
