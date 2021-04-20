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
    private String ipAddress;

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
                ipAddress=communicator.getServer();
                if(ipAddress != "") {
                    System.out.println("Initializing with the IP (" + ipAddress +")");
                    activeOperator.setupTcpConnection(ipAddress);
                    evaluator.initialize();
                }
            } catch (InterruptedException | IllegalArgumentException | IOException e) {
                e.printStackTrace();
            }

            while (evaluator.isGood()) {
                //TODO consider moving the new server utilization value into a parameter or take it from the evaluation result
                evaluator.evaluate();
                if(evaluator.isEvaluating()) {
                    activeOperator.setServerUtilization(false);
                    activeOperator.serverEvaluationRunner.resume();
                } else if(evaluator.isGood()) {
                    activeOperator.setServerUtilization(true);
                    activeOperator.serverEvaluationRunner.pause();
                } else {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
            if(evaluator.needToDisconnect()) {
                try {
                    communicator.disconnectServer();
                    activeOperator.setServerUtilization(false);
                checkPause();
                    evaluator.setDisconnect(false);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
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