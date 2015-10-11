package org.usfirst.frc.team619.logic;

/**
 * @author CaRobotics
 */
public abstract class RobotThread extends Thread{
    protected volatile boolean isRunning = false;
    private volatile boolean isSuspended = false;
    private int period = 0;
    
    public RobotThread(int period, ThreadManager threadManager){
        this.period = period;
        threadManager.addThread(this);
    }
    
    public boolean isRunning() {
        return isRunning;
    }

    public void stopRunning() {
        this.isRunning = false;
    }
    
    public void setSuspended(boolean suspended){
        this.isSuspended = suspended;
    }
    
    public void run(){
        isRunning = true;
        //System.out.println("RobotThread - run");
        begin();
        while(isRunning){
            if(isSuspended) continue;
            
            try {
                cycle();
                sleep(period);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Woah! Exception from RobotThread <---- YOU NEED TO FIX THIS!");
            }
        }
        onDestroy();
    }
    
    /**
     * Run at every cycle of the thread
     * NOTE TO IMPLEMENTORS: THIS MUST RETURN WITHIN A FEW MILLISECONDS!!!
     * YOU WILL SCREW UP OUR ENTIRE ROBOT IF YOU WRITE THIS METHOD WRONG!!!
     */
    abstract protected void cycle();
    
    /**
     * Run once when the thread begins to run
     */
    protected void begin(){
        // do nothing unless this is overridden
    }
    
    /*
     * Run once when the thread is killed
     * Release your inptus/outputs!
     */
    protected void onDestroy(){
        isRunning = false;
    }
}
