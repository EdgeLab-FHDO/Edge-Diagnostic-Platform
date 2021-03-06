package DiagnosticsClient.Communication.Exception.TCP;

import DiagnosticsClient.Communication.Exception.TCP.TCPConnectionException;

public class ServerNotSetUpException extends TCPConnectionException {
    public ServerNotSetUpException(String message) {
        super(message);
    }
}
