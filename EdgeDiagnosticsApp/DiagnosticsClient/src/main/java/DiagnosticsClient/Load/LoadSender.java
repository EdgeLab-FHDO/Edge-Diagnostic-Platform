package DiagnosticsClient.Load;

import DiagnosticsClient.Load.Exception.LoadSendingException;

public interface LoadSender {
    void sendPing(String address, int port) throws LoadSendingException;
    void sendFile(String address, int port) throws LoadSendingException;
    void sendVideo(String address, int port) throws LoadSendingException;
}
