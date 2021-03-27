package RunnerManagement;

import Communication.BasicPlatformConnection;
import Communication.HeartBeatRunner;
import RunnerManagement.Exception.RunnersNotConfiguredException;

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

    public void startRunners() throws RunnersNotConfiguredException {
        if (!configured) {
            throw new RunnersNotConfiguredException("Runners were not configured");
        }
        for (AbstractRunner runner : runners) {
            Thread thread = new Thread(runner);
            threads.add(thread);
            thread.start();
        }
    }

    public void stopRunners() {
        threads.forEach(Thread::interrupt);
        runners.forEach(AbstractRunner::stop);
    }
}
