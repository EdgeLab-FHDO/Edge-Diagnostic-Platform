package Communication;

import Communication.Exception.RESTClientException;
import RunnerManagement.AbstractRunner;

public class HeartBeatRunner extends AbstractRunner {

    private final BasicPlatformConnection connection;
    private final String body;

    public HeartBeatRunner(BasicPlatformConnection connection, String body) {
        this.connection = connection;
        this.body = body;
    }

    @Override
    public void runnerOperation() throws InterruptedException {
        try {
            connection.beat(body);
        } catch (RESTClientException e) {
            e.printStackTrace();
            this.stop();
        }
        Thread.sleep(1000);
    }
}
