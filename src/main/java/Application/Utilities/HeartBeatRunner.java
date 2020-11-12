package Application.Utilities;

import java.net.StandardSocketOptions;

public class HeartBeatRunner implements Runnable {
    //TO DO: change extends Runner and instead implement Runnable
    //TO DO: reuse instances
    private EdpHeartbeat beater;

    public HeartBeatRunner(String url, String body) {
        beater = new EdpHeartbeat(url, body);
    }
    @Override
    public void run() {
        while(true) {
            beater.beat();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}