package DiagnosticsServer.Load;

import DiagnosticsServer.Load.Exception.LoadReceivingException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPLoadReceiver implements LoadReceiver {
    @Override
    public void receivePing(int port) throws LoadReceivingException {
        try (
                DatagramSocket serverSocket = new DatagramSocket(port);
        ){
            byte[] receivingDataBuffer = new byte[1024];
            byte[] sendingDataBuffer;
            // receive request
            DatagramPacket inputPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
            System.out.println("Waiting for a client to connect...");
            serverSocket.receive(inputPacket);

            String receivedData = new String(inputPacket.getData());
            System.out.println("Sent from the client: "+receivedData);

            sendingDataBuffer = inputPacket.getData();

            // send the response to the client at "address" and "port"
            InetAddress address = inputPacket.getAddress();
            int sourcePort = inputPacket.getPort();
            DatagramPacket outputPacket = new DatagramPacket(sendingDataBuffer, sendingDataBuffer.length, address, sourcePort);
            serverSocket.send(outputPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveFile(int port) throws LoadReceivingException {

    }

    @Override
    public void receiveVideo(int port) throws LoadReceivingException {

    }
}
