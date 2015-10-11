package org.usfirst.frc.team619.hardware;

import edu.wpi.first.wpilibj.AnalogInput;

/**
 * @author CaRobotics
 */
public class AnalogIn {
    
    private AnalogInput analog;
    
    public AnalogIn(int channel) {
        analog = new AnalogInput(channel);
    }
    
    
//        final int value = getValue(channel);
//        final long LSBWeight = getLSBWeight(channel);
//        final int offset = getOffset(channel);
//        final double voltage = (LSBWeight * 1.0e-9 * value) - (offset * 1.0e-9);
    
    public double get() {
        return analog.getVoltage();
    }
    
    public double getVoltage() {
        return analog.getVoltage();
    }
    
    public int getChannel() {
        return analog.getChannel();
    }
    
//        List<GameUnit>[] map = new ArrayList<GameUnit>[100 * 100];
//        for(List<GameUnit> list : map) {
//            list = new List<GameUnit>();
//        }
//        
//        List<GameUnit> list = map[100 + 63];
}
