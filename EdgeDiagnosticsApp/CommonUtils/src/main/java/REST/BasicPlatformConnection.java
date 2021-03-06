package REST;

import REST.Exception.RESTClientException;

public abstract class BasicPlatformConnection {

    private final RequestMaker client;
    private final String registerURL;

    public BasicPlatformConnection(String baseURL, String registerURL) {
        this.registerURL = registerURL;
        this.client = new RequestMaker(baseURL);
    }

    public void register(String jsonRepresentation) throws RESTClientException {
        this.client.post(registerURL,jsonRepresentation);
    }

    protected RequestMaker getClient() {
        return client;
    }

}
