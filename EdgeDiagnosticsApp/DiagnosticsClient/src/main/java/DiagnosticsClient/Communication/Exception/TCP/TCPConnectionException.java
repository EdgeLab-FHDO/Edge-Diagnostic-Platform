package DiagnosticsClient.Communication.Exception.TCP;

import DiagnosticsClient.Communication.Exception.ClientCommunicationException;

public class TCPConnectionException extends ClientCommunicationException {
    public TCPConnectionException(String message) {
        super(message);
    }

    public TCPConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
