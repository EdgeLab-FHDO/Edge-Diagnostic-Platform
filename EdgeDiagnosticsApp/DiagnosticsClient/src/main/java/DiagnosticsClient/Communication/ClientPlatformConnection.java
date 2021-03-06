package DiagnosticsClient.Communication;


import DiagnosticsClient.Communication.Exception.JSONException;
import REST.BasicPlatformConnection;
import REST.Exception.RESTClientException;

public class ClientPlatformConnection extends BasicPlatformConnection {

    private final String assignURL;
    private final String getServerURL;

    public ClientPlatformConnection(String baseURL, String registerURL, String assignURL, String getServerURL) {
        super(baseURL,registerURL);
        this.assignURL = assignURL;
        this.getServerURL = getServerURL;
    }

    public ServerInformation getServer(String jsonRepresentation) throws RESTClientException, JSONException {
        this.getClient().post(assignURL,jsonRepresentation);
        String response = this.getClient().get(getServerURL);
        return new ServerInformation(response);
    }
}
