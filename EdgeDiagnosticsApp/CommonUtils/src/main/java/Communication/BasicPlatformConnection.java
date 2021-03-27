package Communication;

import Communication.Exception.RESTClientException;

public abstract class BasicPlatformConnection {

    private final RequestMaker client;
    private final String registerURL;
    private final String heartbeatURL;
    private final String instructionsURL;

    public BasicPlatformConnection(String baseURL, String registerURL,String heartbeatURL,String instructionsURL) {
        this.registerURL = registerURL;
        this.instructionsURL = instructionsURL;
        this.heartbeatURL = heartbeatURL;
        this.client = new RequestMaker(baseURL);
    }

    public void register(String jsonRepresentation) throws RESTClientException {
        this.client.post(registerURL,jsonRepresentation);
    }

    public String getInstructions() throws RESTClientException {
        return this.client.get(instructionsURL);
    }

    public void beat(String heartbeatBody) throws RESTClientException {
        this.client.post(heartbeatURL, heartbeatBody);
    }

    protected RequestMaker getClient() {
        return client;
    }

}
