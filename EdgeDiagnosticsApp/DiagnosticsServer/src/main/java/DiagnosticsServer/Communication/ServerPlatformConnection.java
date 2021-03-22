package DiagnosticsServer.Communication;

import REST.BasicPlatformConnection;

public class ServerPlatformConnection extends BasicPlatformConnection {
    public ServerPlatformConnection(String baseURL, String registerURL, String instructionsURL) {
        super(baseURL, registerURL,instructionsURL);
    }
}
