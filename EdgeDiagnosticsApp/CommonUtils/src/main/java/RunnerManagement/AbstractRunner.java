package RunnerManagement;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractRunner implements Runnable {

    private final AtomicBoolean exit = new AtomicBoolean(false);

    @Override
    public void run() {
        exit.set(false);
        while (!exit.get()) {
            try {
                runnerOperation();
            } catch (InterruptedException e) {
                this.stop();
            }
        }
    }

    public void stop() {
        exit.set(true);
    }

    public abstract void runnerOperation() throws InterruptedException;
}
