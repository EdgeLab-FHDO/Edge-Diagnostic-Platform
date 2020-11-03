import java.net.StandardSocketOptions;

public class HeartBeatRunner implements Runnable {
    //TO DO: change extends Runner and instead implement Runnable
    //TO DO: reuse instances
    public String url;
    public String body;
    @Override
    public void run() {
        EdpHeartbeat beater = new EdpHeartbeat();
        beater.masterUrl = url;
        while(true) {
            beater.beat(body);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}