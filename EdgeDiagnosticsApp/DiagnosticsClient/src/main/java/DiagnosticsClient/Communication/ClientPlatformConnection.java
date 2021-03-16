package DiagnosticsClient.Communication;


import REST.BasicPlatformConnection;
import REST.Exception.RESTClientException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ClientPlatformConnection extends BasicPlatformConnection {

    private final String assignURL;
    private final String getServerURL;

    public ClientPlatformConnection(String baseURL, String registerURL, String assignURL, String getServerURL) {
        super(baseURL,registerURL,"");
        this.assignURL = assignURL;
        this.getServerURL = getServerURL;
    }

    public ServerInformation getServer(String jsonRepresentation) throws RESTClientException, JsonProcessingException {
        this.getClient().post(assignURL,jsonRepresentation);
        String response = this.getClient().get(getServerURL);
        return new ServerInformation(response);
    }
}
