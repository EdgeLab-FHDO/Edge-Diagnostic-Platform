package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleStoppedException;
import InfrastructureManager.PlatformObject;

import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Class implementing the Runnable interface to be run in different threads.
 *
 * The operation it performs when running is configurable and normally done by a {@link PlatformModule}
 *
 * Each runner is related to an input and in most cases, performs the operation of reading from input, mapping
 * and sending to output.
 */
public class Runner extends PlatformObject implements Runnable{

    /**
     * Function as an object representing the operation this runner will do when a thread is created for it and it runs
     */
    private RunnerOperation runOperation;

    protected String name;
    protected PlatformInput input;
    protected List<Connection> connections;

    protected final Semaphore pauseBlock;
    protected volatile boolean paused = false; //Flag to check status and be able to pause
    protected volatile boolean exit = false; //Flag to check status and be able to exit
    protected volatile boolean running = false; //Flag to see runner status

    /**
     * Creates a new Runner based on its ownerModule, a name and the input it is bounded to.
     * @param ownerModule Owner module of this runner. This will normally trigger its execution
     * @param name Name for this runner
     * @param input Input to bound to this runner. One runner can only be related to one input.
     */
    public Runner(ImmutablePlatformModule ownerModule,String name, PlatformInput input) {
        super(ownerModule);
        this.name = name;
        this.input = input;
        this.pauseBlock = new Semaphore(0);
    }

    /**
     * For the input related to this runner, receives the connections that input has to different outputs
     * @param connections List of connections between the input and different outputs
     */
    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

    /**
     * Returns the connections defined for the input related to this runner
     * @return List of connections between the input and different outputs
     */
    public List<Connection> getConnections() {
        return connections;
    }

    /**
     * Overriding of the run method in the Runnable interface
     * Checks for exit and pause flags and runs the operation defined in {@link #setRunOperation(RunnerOperation)} in a loop.
     * The loop only finished when {@link #exit()} is called on the runner
     */
    @Override
    public void run() {
        running = true;
        exit = false;
        while (!exit) {
            try {
                checkPause();
                runOperation.process(this,input);
            } catch (InterruptedException | ModuleStoppedException e) {
                exit = true;
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

    public void setRunOperation(RunnerOperation runOperation) {
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
