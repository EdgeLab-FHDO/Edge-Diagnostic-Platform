package Application.MarkerDetection.OpenCVClient;

import Application.Utilities.RemoteExecutionException;
import java.util.concurrent.Semaphore;

public class ServerEvaluationRunner implements Runnable {
    //TODO implement and use exit and running values
    private final OpenCVClientOperator activeOperator;
    private final ServerEvaluationOperator operator;

    private volatile boolean exit = false;
    private volatile boolean running = false;
    private volatile boolean paused = true;
    private final Semaphore pauseBlock;

    public ServerEvaluationRunner() {
        activeOperator = OpenCVClientOperator.getInstance();
        operator = new ServerEvaluationOperator();
        pauseBlock = new Semaphore(1);
    }

    @Override
    public void run() {
        while(!exit) {
            try {
                checkPause();
                if(running) {
                    operator.evaluationOperation();
                }
            } catch(RemoteExecutionException e) {
                activeOperator.evaluator.handleException(e);
                System.out.println("Cannot communicate with server, current EQR:" + activeOperator.evaluator.getEvaluationParameter());
            } catch (InterruptedException | SecurityException | IllegalArgumentException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * When called, the runner terminates
     */
    public void exit(){
        exit = true;
        running = false;
    }

    /**
     * See if the current runner is running
     * @return True if the runner is running, otherwise false.
     */
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
