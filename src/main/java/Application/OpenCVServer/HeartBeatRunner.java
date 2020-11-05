import java.net.StandardSocketOptions;

public class HeartBeatRunner implements Runnable {
    //TO DO: change extends Runner and instead implement Runnable
    //TO DO: reuse instances
    @Override
    public void run() {
        EdpHeartbeat beater = new EdpHeartbeat();
        while(true) {
            beater.beat("client");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}