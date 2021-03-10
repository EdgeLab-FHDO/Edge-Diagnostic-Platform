package DiagnosticsServer.Load;

import DiagnosticsServer.Load.Exception.LoadReceivingException;
import DiagnosticsServer.Load.Exception.UDP.UDPConnectionException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPLoadReceiver implements LoadReceiver {
    @Override
    public void receivePing(int port) throws LoadReceivingException {
        try (
                DatagramSocket serverSocket = new DatagramSocket(port)
        ){
            byte[] receivingDataBuffer = new byte[1024];
            byte[] sendingDataBuffer;
            DatagramPacket inPacket;
            DatagramPacket outPacket;
            InetAddress replyAddress;
            int replyPort;
            String receivedData = "";
            while (!receivedData.trim().equals("exit")) {
                inPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
                serverSocket.receive(inPacket);
                receivedData = new String(inPacket.getData());
                sendingDataBuffer = receivedData.getBytes();
                replyAddress = inPacket.getAddress();
                replyPort = inPacket.getPort();
                outPacket = new DatagramPacket(sendingDataBuffer, sendingDataBuffer.length, replyAddress, replyPort);
                serverSocket.send(outPacket);

            }
            System.out.println("Bye");
        } catch (IOException e) {
            throw new UDPConnectionException("UDP Connection failed: ", e);
        }
    }

    @Override
    public void receiveFile(int port) throws LoadReceivingException {

    }

    @Override
    public void receiveVideo(int port) throws LoadReceivingException {

    }
}
