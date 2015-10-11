package org.usfirst.frc.team619.hardware;

/**
 * @author CaRobotics
 */
public class Servo {
    edu.wpi.first.wpilibj.Servo servo;
    
    public Servo(int channel/*slot in digital sidecar*/) {
        servo = new edu.wpi.first.wpilibj.Servo(channel);
//        servo.setBounds(255, 130, 127, 125, 0); // TODO: Not working yet
//        servo.setBounds(245, 0, 0, 0, 11);
    }

    /**
     * 
     * @param value Range from 0 to 1
     */
    public void set(double value) {
        servo.setRaw((int) ((value*254)+1));
    }

    public void setRaw(int rawValue) {
        servo.setRaw(rawValue);
    }

    public double getSpeed() {
        return servo.getSpeed();
    }
}
