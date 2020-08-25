package InfrastructureManager;

/**
 * Class implementing the Runnable interface to be run in different threads by the master.
 * Implements the basic master functionality of reading from input, mapping and sending to output.
 * Each runner, has input and outputs which allow for static configuration of the master
 */
public class Runner implements Runnable{
    protected String name;
    protected MasterInput input;
    protected MasterOutput[] outputs;

    protected final Object pauseLock = new Object(); //In order to pause the runnable
    protected volatile boolean paused = false; //Flag to check status and be able to pause
    protected volatile boolean exit = false; //Flag to check status and be able to exit
    protected volatile boolean running = false; //Flag to see runner status

    /**
     * Constructor of the class, normally configured based on raw data (RunnerConfigData) by the
     * MasterConfigurator
     * @param name Name of the runner
     * @param input Input of the runner (MasterInput object)
     * @param outputs 1 or more MasterOutput objects to be defined as outputs of the runner
     */
    public Runner(String name,MasterInput input, MasterOutput...outputs) {
        this.name = name;
        this.input = input;
        this.outputs = outputs;
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
            checkPause();
            runOperation();
        }
        running = false;
    }

    /**
     * Method to poll for the status of the pause variables, and pause the runner if needed
     */
    private void checkPause() {
        if (paused) {
            running = false;
            synchronized (pauseLock) { //Access the shared flag resource
                try {
                    pauseLock.wait(); //Runner Pauses until is notified by other thread
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }

    /**
     * Method to run the basic master operation consisting of
     * 1. Reading from Input
     * 2. Mapping the command
     * 3. Sending to output(s)
     */
    protected void runOperation() {
        Master master = Master.getInstance();
        try {
            String mapping = master.execute(input.read());
            for (MasterOutput output : outputs) {
                output.out(mapping);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        synchronized (pauseLock) {
            paused = false;
            running = true;
            pauseLock.notifyAll(); //Notifies using the shared resource
        }
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
