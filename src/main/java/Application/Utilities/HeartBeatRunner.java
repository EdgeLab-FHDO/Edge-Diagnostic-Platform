package Application.Utilities;

import java.io.IOException;
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
            try {
                beater.beat();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                System.out.println("Heartbeat target URL is faulty");
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}