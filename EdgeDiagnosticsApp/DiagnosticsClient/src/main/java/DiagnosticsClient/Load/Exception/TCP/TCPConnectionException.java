package DiagnosticsClient.Load.Exception.TCP;

import DiagnosticsClient.Load.Exception.LoadSendingException;

public class TCPConnectionException extends LoadSendingException {
    public TCPConnectionException(String message) {
        super(message);
    }

    public TCPConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
