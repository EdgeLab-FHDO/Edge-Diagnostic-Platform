package InfrastructureManager;

import InfrastructureManager.ModuleManagement.Runner;

import java.util.concurrent.Semaphore;

public abstract class MasterInput {

    private final String name;
    private final Semaphore readingLock; //Binary semaphore to implement blocking on inputs
    private Runner runner;

    public MasterInput(String name) {
        this.name = name;
        this.readingLock = new Semaphore(0); // starts without permits so it will block until input is available
    }

    /**
     * Stores the command in the corresponding storing element for each implementation and unblocks the semaphore
     * @param reading Command to be sent.
     * @author juan-castrillon
     */
    protected void storeReadingAndUnblock(String reading) {
        storeSingleReading(reading);
        this.readingLock.release();
    }

    /**
     * Implements blocking capabilities until a value is available to be sent. Then returns the value fetching it from
     * each implementation's storage element
     * @return Value to be returned when read() is called
     * @throws InterruptedException If interrupted while blocked
     * @author juan-castrillon
     */
    protected String getReading() throws InterruptedException {
        this.readingLock.acquire();
        return getSingleReading();
    }

    public void setRunner(Runner runner) {
        this.runner = runner;
    }

    public Runner getRunner() {
        return runner;
    }

    public String getName() {
        return name;
    }

    protected abstract String getSingleReading();
    protected abstract void storeSingleReading(String reading);
    public abstract String read() throws InterruptedException;
}
