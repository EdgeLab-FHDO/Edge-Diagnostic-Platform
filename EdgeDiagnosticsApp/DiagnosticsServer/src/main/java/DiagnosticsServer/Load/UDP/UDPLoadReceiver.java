package DiagnosticsServer.Load.UDP;

import DiagnosticsServer.Load.Exception.LoadReceivingException;
import DiagnosticsServer.Load.Exception.UDP.UDPConnectionException;
import DiagnosticsServer.Load.LoadReceiver;
import DiagnosticsServer.Load.ServerSocketOptions;
import LoadManagement.LoadType;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static java.net.StandardSocketOptions.*;

public class UDPLoadReceiver extends LoadReceiver {

    private UDPServerSocketOptions socketConfig;

    public UDPLoadReceiver(int port) {
        super(port);
        this.socketConfig = new UDPServerSocketOptions();
    }

    @Override
    public void receive(LoadType loadType) throws LoadReceivingException {
        switch (loadType) {
            case PING -> receivePing();
            case FILE -> receiveFile();
            case VIDEO -> receiveVideo();
        }
    }

    @Override
    public void changeSocketConfiguration(ServerSocketOptions options) {
        this.socketConfig = (UDPServerSocketOptions) options;
    }

    private void receivePing() throws LoadReceivingException {
        try (
                DatagramSocket serverSocket = new DatagramSocket(this.getPort())
        ){
            configureSocket(serverSocket);
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


    private void receiveFile() throws LoadReceivingException {

    }


    private void receiveVideo() throws LoadReceivingException {

    }

    private void configureSocket(DatagramSocket socket) throws IOException {
        socket.setReuseAddress(socketConfig.getReuseAddress());
        socket.setReceiveBufferSize(socketConfig.getReceiveBufferSize());
        socket.setSendBufferSize(socketConfig.getSendBufferSize());
        socket.setSoTimeout(socketConfig.getTimeout());
        socket.setBroadcast(socketConfig.getBroadcast());
        socket.setOption(IP_MULTICAST_LOOP, socketConfig.getMulticastLoop());
        socket.setOption(IP_MULTICAST_TTL, socketConfig.getMulticastTTL());
        socket.setOption(IP_TOS,socketConfig.getIpTOS());
    }

    private void printSocketOptions(DatagramSocket socket) throws IOException {
        System.out.println("UDP Socket properties: ");
        var options = socket.supportedOptions();
        for (var option: options) {
            System.out.print("\t" + option.name() + " : ");
            System.out.println(socket.getOption(option));
        }
        System.out.println("\t" + "SO_TIMEOUT : " + socket.getSoTimeout());
    }
}
