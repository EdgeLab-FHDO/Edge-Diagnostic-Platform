package DiagnosticsServer.Communication;

import Communication.BasicPlatformConnection;

public class ServerPlatformConnection extends BasicPlatformConnection {
    public ServerPlatformConnection(String baseURL, String registerURL, String instructionsURL) {
        super(baseURL, registerURL,instructionsURL);
    }
}
