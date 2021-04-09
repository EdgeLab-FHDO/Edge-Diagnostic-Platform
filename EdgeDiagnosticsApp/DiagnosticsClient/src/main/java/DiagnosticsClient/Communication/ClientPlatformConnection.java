package DiagnosticsClient.Communication;


import Communication.BasicPlatformConnection;
import Communication.Exception.RESTClientException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ClientPlatformConnection extends BasicPlatformConnection {

    private final String assignURL;
    private final String getServerURL;
    private final String measurementsURL;

    public ClientPlatformConnection(String baseURL, String registerURL,
                                    String assignURL, String getServerURL, String heartbeatURL,
                                    String instructionsURL, String measurementsURL, String nextURL) {
        super(baseURL,registerURL,heartbeatURL,instructionsURL,nextURL);
        this.assignURL = assignURL;
        this.getServerURL = getServerURL;
        this.measurementsURL = measurementsURL;
    }

    public ServerInformation getServer(String jsonRepresentation) throws RESTClientException, JsonProcessingException {
        this.getClient().post(assignURL,jsonRepresentation);
        String response = this.getClient().get(getServerURL);
        System.out.println(response);
        return new ServerInformation(response);
    }

    public void sendMeasurements(String latencyString) throws RESTClientException {
        this.getClient().post(measurementsURL,latencyString);
    }
}
