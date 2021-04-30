package DiagnosticsServer.Load.Exception.UDP;

import DiagnosticsServer.Load.Exception.LoadReceivingException;

public class UDPConnectionException extends LoadReceivingException {
    public UDPConnectionException(String message) {
        super(message);
    }

    public UDPConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
