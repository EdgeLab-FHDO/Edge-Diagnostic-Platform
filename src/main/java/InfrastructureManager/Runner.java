package InfrastructureManager;

import InfrastructureManager.Configuration.CommandSet;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Class implementing the Runnable interface to be run in different threads by the master.
 * Implements the basic master functionality of reading from input, mapping and sending to output.
 * Each runner, has input and outputs which allow for static configuration of the master
 */
public class Runner implements Runnable{

    private BiConsumer<Runner,String> runOperation;

    protected String name;
    protected MasterInput input;
    protected List<Connection> connections;


    protected final Semaphore pauseBlock;
    protected volatile boolean paused = false; //Flag to check status and be able to pause
    protected volatile boolean exit = false; //Flag to check status and be able to exit
    protected volatile boolean running = false; //Flag to see runner status

    public Runner(String name, MasterInput input) {
        this.name = name;
        this.input = input;
        this.pauseBlock = new Semaphore(0);
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    /**
     * Overriding of the run method in the Runnable interface
     * Implement controls the exit, pause and execution of the runner
     */
    @Override
    public void run() {
        running = true;
        exit = false;
        while (!exit) {
            try {
                checkPause();
                runOperation.accept(this,input.read());
            } catch (InterruptedException e) {
                exit = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        running = false;
    }

    /**
     * Method to poll for the status of the pause variables, and pause the runner if needed
     */
    private void checkPause() throws InterruptedException {
        if (paused) {
            running = false;
            pauseBlock.acquire();
        }
    }

    public void setRunOperation(BiConsumer<Runner,String> runOperation) {
        this.runOperation = runOperation;
    }

    /**
     * When called, the runner terminates
     */
    public void exit(){
        exit = true;
    }

    /**
     * When called, the runner is paused
     */
    public void pause() {
        paused = true;
    }

    /**
     * When called, if the runner is paused then is resumed
     */
    public void resume() {
        paused = false;
        running = true;
        pauseBlock.release();
    }

    /**
     * Method to get the name of the current runner
     * @return Name of the Runner
     */
    public String getName() {
        return name;
    }

    /**
     * See if the current runner is running
     * @return True if the runner is running, otherwise false.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * See if the current runner is paused
     * @return True if the runner was started but now its paused, false otherwise
     */
    public boolean isPaused() {
        return paused;
    }


    /**
     * Determines if an object is equal to a runner. Bases the comparison on runner names
     * @param o Object to be compared to
     * @return True if the objects are equal, otherwise False
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Runner runner = (Runner) o;
        return name.equals(runner.name);
    }
}
