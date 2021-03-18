package Application.MarkerDetection.OpenCVClient;

import java.io.IOException;
import java.util.concurrent.Semaphore;

public class MasterCommunicationRunner implements Runnable {
    //TODO today, request assign client during the first time
    private final OpenCVClientOperator activeOperator;
    private ConnectionEvaluator evaluation;
    public MasterCommunicator communicator;

    private volatile boolean exit = false;
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private final Semaphore pauseBlock;

    public MasterCommunicationRunner(String masterUrl) {
        activeOperator = OpenCVClientOperator.getInstance();
        evaluation = new ConnectionEvaluator();
        communicator = new MasterCommunicator(masterUrl);
        pauseBlock = new Semaphore(1);
    }

    @Override
    public void run() {
        while(!exit) {
            try {
                checkPause();
                activeOperator.setupTcpConnection(communicator.getServer());
                activeOperator.setServerUtilization(true);
            } catch (InterruptedException | IllegalArgumentException | IOException e) {
                e.printStackTrace();
            }

            while (OpenCVClientOperator.getInstance().utilizeServer && evaluation.isGood()) {
                //TODO consider moving the new server utilization value into a parameter or take it from the evaluation result
                //TODO add condition if processing is local, break from this loop and skip evaluation (make a custom Exception and throw it)
                evaluation.evaluate();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
            activeOperator.setServerUtilization(false);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    private void checkPause() throws InterruptedException {
        if (paused) {
            running = false;
            pauseBlock.acquire();
        }
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
        running = true;
        pauseBlock.release();
    }
}