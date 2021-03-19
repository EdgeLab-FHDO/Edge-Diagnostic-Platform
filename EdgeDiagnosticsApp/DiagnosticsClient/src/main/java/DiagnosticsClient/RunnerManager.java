package DiagnosticsClient;

import DiagnosticsClient.Communication.ClientPlatformConnection;
import DiagnosticsClient.Communication.ServerInformation;
import DiagnosticsClient.Load.Exception.TCP.ServerNotSetUpException;

import java.util.Arrays;

public class RunnerManager {

    private boolean configured;
    private final AbstractRunner[] runners;
    private final Thread[] threads;
    private final int runnerNumber;

    public RunnerManager() {
        this.configured = false;
        this.runnerNumber = 3;
        this.runners = new AbstractRunner[runnerNumber];
        this.threads = new Thread[runnerNumber];
    }

    public void configure(ClientPlatformConnection connection,
                          String heartBeatBody,
                          ServerInformation serverInformation) throws ServerNotSetUpException {
        runners[0] = new HeartBeatRunner(connection, heartBeatBody);
        InstructionQueue instructionQueue = new InstructionQueue();
        runners[1] = new ControlRunner(connection,instructionQueue);
        runners[2] = new LoadRunner(serverInformation, instructionQueue);
        configured = true;
    }

    public void startRunners() {
        if (!configured) {
            //TODO: Throw custom exception
        }
        for (int i = 0; i < runnerNumber; i++) {
            Thread thread = new Thread(runners[i]);
            threads[i] = thread;
            thread.start();
        }
    }

    public void stopRunners() {
        for (Thread t : threads) t.interrupt();
        for (AbstractRunner r : runners) r.stop();
    }

}
