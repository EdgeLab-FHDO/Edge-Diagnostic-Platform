package Application.Utilities;

import java.io.IOException;

public class HeartBeatRunner implements Runnable {
    //TODO implement and use exit and running values
    private final EdpHeartbeat beater;

    private volatile boolean exit = false;
    private volatile boolean running = false;

    public HeartBeatRunner(String url, String body) {
        beater = new EdpHeartbeat(url, body);
    }
    @Override
    public void run() {
        while(true) {
            try {
                beater.beat();
            } catch (IOException | InterruptedException | SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}