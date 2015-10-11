package org.usfirst.frc.team619.logic.actions;

import java.util.Vector;

import org.usfirst.frc.team619.logic.RobotThread;
import org.usfirst.frc.team619.logic.ThreadManager;

/**
 * @author thomas
 */
public abstract class Action extends RobotThread {

    private Vector dependencies = new Vector(); // List of Action s that must be complete prior to running this action
    private int waitPeriod;

    public Action(int waitForDependenciesPeriod, int runPeriod, ThreadManager threadManager, Vector dependencies) {
        super(runPeriod, threadManager);
        this.waitPeriod = waitForDependenciesPeriod;
        addDependency(dependencies);
    }

    public Action(int waitForDependenciesPeriod, int runPeriod, ThreadManager threadManager) {
        super(runPeriod, threadManager);
        this.waitPeriod = waitForDependenciesPeriod;
    }

    public Action(int waitForDependenciesPeriod, int runPeriod, ThreadManager threadManager, Action dependency) {
        super(runPeriod, threadManager);
        this.waitPeriod = waitForDependenciesPeriod;
        addDependency(dependency);
    }

    public abstract boolean isComplete();

    /**
     * Add a Action that this is dependent on
     */
    public final void addDependency(Action a) {
        if (a == null) {
            return;
        }
        dependencies.addElement(a);
    }

    /**
     * Add all valid Actions from the vector to the list of dependencies
     * @param dependencies A list of Actions on which this action is dependent
     */
    public final void addDependency(Vector dependenciesList) {
        if (dependenciesList == null) {
            return;
        }
        for (int i = 0; i < dependenciesList.size(); i++) {
            if (dependenciesList.elementAt(i) instanceof Action) {
                dependencies.addElement(dependenciesList.elementAt(i));
            }
        }
    }

    /**
     * Checks if dependencies have been satisfied
     * @return True if all Actions that this action is dependent on have completed, false otherwise 
     */
    public boolean dependenciesSatisfied() {
        System.out.println("[Action] Checking " + dependencies.size() + " dependencies.");
        for (int i = 0; i < dependencies.size(); i++) {
            if (!((Action) dependencies.elementAt(i)).isComplete()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Wait for all dependencies to be satisfied and then call super.run()
     */
    public void run() {
        isRunning = true;
        System.out.println("[Action] Run started");
        while (isRunning()) {
            System.out.println("[Action] while loop cycle");
            if (dependenciesSatisfied()) {
                break;
            }
            try {
                sleep(waitPeriod);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[Action] Dependencies Satisfied");
        super.run();
        System.out.println("[Action] Action complete");
    }
}
