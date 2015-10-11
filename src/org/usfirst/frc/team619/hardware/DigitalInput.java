package org.usfirst.frc.team619.hardware;

/**
 * @author CaRobotics
 */
public class DigitalInput {
    private edu.wpi.first.wpilibj.DigitalInput input;
    
    public DigitalInput(int channel/*spot on the DIO section of Athena*/) {
        input = new edu.wpi.first.wpilibj.DigitalInput(channel);
    }
    
    public boolean get() {
        return input.get();
    }
}
