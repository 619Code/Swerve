package org.usfirst.frc.team619.hardware;

/**
 *
 *
 * @author CaRobotics
 */


public class Talon extends edu.wpi.first.wpilibj.Talon {

    protected double factor = 1.0;

    public Talon(int channel) {
        super(channel);
    }

    /**
     * @param value Range from -1 to 1
     */
    public void set(double value) {
        super.set(value * factor);
    }

    public void reverseOutput(boolean rev){
        if(rev){
            factor = -1.0;
        }else{
            factor = 1.0;
        }
    }

}
