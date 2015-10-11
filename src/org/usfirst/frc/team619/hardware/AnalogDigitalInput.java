package org.usfirst.frc.team619.hardware;

/**
 * @author CaRobotics
 */
public class AnalogDigitalInput {
    
    private static final double maxVoltageHalf = 2.5;
    
    private AnalogIn analog;
    
    public AnalogDigitalInput(int channel) {
        analog = new AnalogIn(channel);
    }
    
    
//        final int value = getValue(channel);
//        final long LSBWeight = getLSBWeight(channel);
//        final int offset = getOffset(channel);
//        final double voltage = (LSBWeight * 1.0e-9 * value) - (offset * 1.0e-9);
    
    public boolean get() {
        return Math.abs(analog.getVoltage()) > maxVoltageHalf;
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
