package Multithreading;

import REST.BasicPlatformConnection;

import java.util.ArrayList;
import java.util.List;

public class BasicRunnerManager {
    private boolean configured;
    private final List<AbstractRunner> runners;
    private final List<Thread> threads;

    protected BasicRunnerManager() {
        this.configured = false;
        this.runners = new ArrayList<>();
        this.threads = new ArrayList<>();
    }

    protected void configure(BasicPlatformConnection connection, String heartbeatBody) {
        runners.add(new HeartBeatRunner(connection, heartbeatBody));
        this.configured = true;
    }

    protected List<AbstractRunner> getRunners() {
        return runners;
    }

    public void startRunners() {
        if (!configured) {
            //TODO: Throw custom exception
        }
        for (int i = 0; i < runners.size(); i++) {
            Thread thread = new Thread(runners.get(i));
            threads.add(thread);
            thread.start();
        }
    }

    public void stopRunners() {
        for (Thread t : threads) t.interrupt();
        for (AbstractRunner r : runners) r.stop();
    }
}
