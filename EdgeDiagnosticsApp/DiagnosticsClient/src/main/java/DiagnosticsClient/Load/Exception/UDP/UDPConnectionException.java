package DiagnosticsClient.Load.Exception.UDP;

import DiagnosticsClient.Load.Exception.LoadSendingException;

public class UDPConnectionException extends LoadSendingException {
    public UDPConnectionException(String message) {
        super(message);
    }

    public UDPConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
