package InfrastructureManager.ModuleManagement;

import java.util.concurrent.Semaphore;

public abstract class ModuleInput {

    private final String name;
    private final Semaphore readingLock; //Binary semaphore to implement blocking on inputs
    private Runner runner;

    public ModuleInput(String name) {
        this.name = name;
        this.readingLock = new Semaphore(0); // starts without permits so it will block until input is available
    }

    protected void block() throws InterruptedException {
        this.readingLock.acquire();
    }

    protected void unblock() {
        this.readingLock.release();
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

    public abstract String read() throws InterruptedException;
}
