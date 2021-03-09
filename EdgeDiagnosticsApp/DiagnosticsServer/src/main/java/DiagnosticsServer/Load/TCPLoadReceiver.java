package DiagnosticsServer.Load;

import DiagnosticsServer.Load.Exception.LoadReceivingException;
import DiagnosticsServer.Load.Exception.TCP.TCPConnectionException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPLoadReceiver implements LoadReceiver {
    @Override
    public void receivePing(int port) throws LoadReceivingException {
        try (
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
                BufferedInputStream in = new BufferedInputStream(clientSocket.getInputStream())
        ){
            while (true) {
                int bufferSize = in.read(); //First byte to indicate message size
                if (bufferSize < 0) break;
                byte[] data = new byte[bufferSize];
                int bytesReceived = in.read(data);
                //String readingString = new String(data);
                out.println("Data Read: " + bytesReceived + " bytes");
            }

        } catch (IOException e) {
            throw new TCPConnectionException("TCP connection failed", e);
        }
    }

    @Override
    public void receiveFile(int port) throws LoadReceivingException {

    }

    @Override
    public void receiveVideo(int port) throws LoadReceivingException {

    }
}
