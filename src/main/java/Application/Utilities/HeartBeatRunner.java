package Application.Utilities;

import java.io.IOException;
import java.util.concurrent.Semaphore;

public class HeartBeatRunner implements Runnable {
    //TODO implement and use exit and running values
    public final HeartBeatOperator beater;

    private final int interval;

    private volatile boolean exit = false;
    private volatile boolean running = false;
    private volatile boolean paused = true;
    private final Semaphore pauseBlock;

    public HeartBeatRunner(String url, String body, int interval) {
        beater = new HeartBeatOperator(url, body);
        pauseBlock = new Semaphore(1);
        this.interval = interval;
    }
    @Override
    public void run() {
        while(!exit) {
            try {
                checkPause();
                if(running) {
                    beater.beat();
                }
            } catch (IOException | InterruptedException | SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(interval);
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