package DiagnosticsServer.Load.Exception.TCP;

import DiagnosticsServer.Load.Exception.LoadReceivingException;

public class TCPConnectionException extends LoadReceivingException {
    public TCPConnectionException(String message) {
        super(message);
    }

    public TCPConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
