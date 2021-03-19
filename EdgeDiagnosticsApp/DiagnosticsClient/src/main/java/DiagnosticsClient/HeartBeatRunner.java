package DiagnosticsClient;

import DiagnosticsClient.Communication.ClientPlatformConnection;
import REST.Exception.RESTClientException;

public class HeartBeatRunner extends AbstractRunner {

    private final ClientPlatformConnection connection;
    private final String body;

    public HeartBeatRunner(ClientPlatformConnection connection, String body) {
        this.connection = connection;
        this.body = body;
    }

    @Override
    public void runnerOperation() throws InterruptedException {
        //connection.register(body);
        Thread.sleep(1000);
    }
}
