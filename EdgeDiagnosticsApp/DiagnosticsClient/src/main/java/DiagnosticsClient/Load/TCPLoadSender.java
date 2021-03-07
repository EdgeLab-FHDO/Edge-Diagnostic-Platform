package DiagnosticsClient.Load;

import DiagnosticsClient.Load.Exception.TCP.TCPConnectionException;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPLoadSender implements LoadSender{

    @Override
    public void sendPing(String address, int port) throws TCPConnectionException {
        byte[] pingMessage = "ping".getBytes();
        int dataLength = pingMessage.length;
        if (dataLength > 255 | dataLength < 1) throw new TCPConnectionException("Invalid length for ping message");
        String screenMessage = "Pinging " + address + ":" + port + " with " +
                dataLength + " bytes of data. Protocol TCP";
        try (
                Socket clientSocket = new Socket(address,port);
                BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            System.out.println(screenMessage);
            out.write(dataLength); //First byte to indicate message size
            out.write(pingMessage);
            out.flush();
            System.out.println("Response: " + in.readLine());
        } catch (IOException e) {
            throw new TCPConnectionException("TCP Connection failed: ",e);
        }
    }

    @Override
    public void sendFile(String address, int port) {

    }

    @Override
    public void sendVideo(String address, int port) {

    }
}
