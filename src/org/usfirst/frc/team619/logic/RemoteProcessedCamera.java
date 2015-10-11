package org.usfirst.frc.team619.logic;

/**
 *
 * @author Student
 */
public class RemoteProcessedCamera {
    
    private int hotGoal = 0; //info for horizontal bar location
    
    public static final int GOAL_UNKNOWN = 0;
    public static final int GOAL_LEFT = 1;
    public static final int GOAL_RIGHT = 2;
    public static final int GOAL_TWO = 3;
    public static final int GOAL_MORE = 4;
    
    public void setHotGoal(int hotGoal) {
        this.hotGoal = hotGoal;
    }
    
    public int getHotGoal() {
        return hotGoal;
    }
    
}
