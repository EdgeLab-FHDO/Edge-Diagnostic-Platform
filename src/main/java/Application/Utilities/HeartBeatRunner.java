package Application.Utilities;

import java.io.IOException;

public class HeartBeatRunner implements Runnable {
    private final EdpHeartbeat beater;

    //stub for future classes
    private volatile boolean exit = false; //Flag to check status and be able to exit
    private volatile boolean running = false; //Flag to see runner status

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
                System.out.println("Heartbeat target URL is faulty");
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