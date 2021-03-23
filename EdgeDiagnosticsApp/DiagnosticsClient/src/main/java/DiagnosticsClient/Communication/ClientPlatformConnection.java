package DiagnosticsClient.Communication;


import REST.BasicPlatformConnection;
import REST.Exception.RESTClientException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ClientPlatformConnection extends BasicPlatformConnection {

    private final String assignURL;
    private final String getServerURL;

    public ClientPlatformConnection(String baseURL, String registerURL,
                                    String assignURL, String getServerURL,
                                    String instructionsURL) {
        super(baseURL,registerURL,instructionsURL);
        this.assignURL = assignURL;
        this.getServerURL = getServerURL;
    }

    public ServerInformation getServer(String jsonRepresentation) throws RESTClientException, JsonProcessingException {
        this.getClient().post(assignURL,jsonRepresentation);
        String response = this.getClient().get(getServerURL);
        return new ServerInformation(response);
    }

    public void sendMeasurements(String latencyString) throws RESTClientException {
        String measurementsURL = "/diagnostics/report";
        this.getClient().post(measurementsURL,latencyString);
    }
}
