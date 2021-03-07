package DiagnosticsServer.Load;

import DiagnosticsServer.Load.Exception.LoadReceivingException;

public interface LoadReceiver {
    void receivePing(int port) throws LoadReceivingException;
    void receiveFile(int port) throws LoadReceivingException;
    void receiveVideo(int port) throws LoadReceivingException;
}
