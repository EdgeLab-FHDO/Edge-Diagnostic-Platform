package InfrastructureManager;

import java.util.Objects;

public class Runner implements Runnable{
    protected String name;
    protected MasterInput input;
    protected MasterOutput[] outputs;

    protected final Object pauseLock = new Object();
    protected volatile boolean paused = false;
    protected volatile boolean exit = false;

    public Runner(String name,MasterInput input, MasterOutput...outputs) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Runner runner = (Runner) o;
        return name.equals(runner.name);
    }
}
