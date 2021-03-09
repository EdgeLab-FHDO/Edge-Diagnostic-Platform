package DiagnosticsClient.Load;

import DiagnosticsClient.Load.Exception.LoadSendingException;

public class UDPLoadSender extends LoadSender {
    public UDPLoadSender(String address, int port) {
        super(address, port);
    }

    @Override
    public void send(DiagnosticsLoad load) throws LoadSendingException {

    }
}
