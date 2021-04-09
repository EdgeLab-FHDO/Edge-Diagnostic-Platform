package DiagnosticsServer.Communication;

import Communication.BasicPlatformConnection;

public class ServerPlatformConnection extends BasicPlatformConnection {
    public ServerPlatformConnection(String baseURL, String registerURL,
                                    String heartbeatURL, String instructionsURL,
                                    String nextURL) {
        super(baseURL, registerURL,heartbeatURL,instructionsURL, nextURL);
    }
}
