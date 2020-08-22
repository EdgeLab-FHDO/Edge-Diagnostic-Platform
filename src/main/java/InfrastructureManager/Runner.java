package InfrastructureManager;

import java.util.Arrays;

public class Runner implements Runnable{
    protected MasterInput input;
    protected MasterOutput[] outputs;

    protected final Object pauseLock = new Object();
    protected volatile boolean paused = false;
    protected volatile boolean exit = false;

    public Runner(MasterInput input, MasterOutput...outputs) {
        this.input = input;
        this.outputs = outputs;
    }

    @Override
    public void run() {
        exit = false;
        while (!exit) {
            checkPause();
            runOperation();
        }
    }
    public void checkPause() {
        if (paused) {
            synchronized (pauseLock) {
                try {
                    pauseLock.wait();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }

    public void runOperation() {
        Master master = Master.getInstance();
        String mapping = master.execute(input.read());
        for (MasterOutput output : outputs) {
            output.out(mapping);
        }
    }

    public void exit(){
        exit = true;
    }

    public void pause() {
        paused = true;
    }
    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }
}
