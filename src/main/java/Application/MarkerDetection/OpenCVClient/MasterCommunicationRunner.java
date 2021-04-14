package Application.MarkerDetection.OpenCVClient;

import java.io.IOException;
import java.util.concurrent.Semaphore;

public class MasterCommunicationRunner implements Runnable {
    //TODO today, request assign client during the first time
    private final OpenCVClientOperator activeOperator;
    public ConnectionEvaluator evaluator;
    public MasterCommunicator communicator;

    private volatile boolean exit = false;
    private volatile boolean running = false;
    private volatile boolean paused = true;
    private final Semaphore pauseBlock;

    public MasterCommunicationRunner(String getServerUrl, String disconnectUrl, String disconnectBody) {
        activeOperator = OpenCVClientOperator.getInstance();
        evaluator = activeOperator.evaluator;
        communicator = new MasterCommunicator(activeOperator, getServerUrl, disconnectUrl, disconnectBody);
        pauseBlock = new Semaphore(1);
    }

    @Override
    public void run() {
        while(!exit) {
            try {
                checkPause();
                activeOperator.setupTcpConnection(communicator.getServer());
                evaluator.initialize();
            } catch (InterruptedException | IllegalArgumentException | IOException e) {
                e.printStackTrace();
            }
            System.out.println(evaluator.isGood());
            while (evaluator.isGood()) {
                //TODO consider moving the new server utilization value into a parameter or take it from the evaluation result
                evaluator.evaluate();
                if(evaluator.isEvaluating()) {
                    activeOperator.setServerUtilization(false);
                    activeOperator.serverEvaluationRunner.resume();
                } else {
                    activeOperator.setServerUtilization(true);
                    activeOperator.serverEvaluationRunner.pause();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }

            try {
                activeOperator.setServerUtilization(false);
                communicator.disconnectServer();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

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