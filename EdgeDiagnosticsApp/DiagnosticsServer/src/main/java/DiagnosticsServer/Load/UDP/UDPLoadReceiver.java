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
import java.net.SocketTimeoutException;

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
        System.out.println("Configured for PING UDP load");
        try (
                DatagramSocket serverSocket = new DatagramSocket(this.getPort())
        ){
            configureSocket(serverSocket);
            printSocketOptions(serverSocket);
            byte[] receivingDataBuffer = new byte[1024];
            byte[] sendingDataBuffer;
            DatagramPacket inPacket;
            DatagramPacket outPacket;
            InetAddress replyAddress;
            int replyPort;
            int timeoutCounter = 0;
            final int TIMEOUT_COUNTER_MAX = 5;
            while (timeoutCounter < TIMEOUT_COUNTER_MAX) {
                try {
                    inPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
                    serverSocket.receive(inPacket);
                } catch (SocketTimeoutException e) {
                    timeoutCounter++;
                    continue;
                }
                timeoutCounter = 0;
                sendingDataBuffer = inPacket.getData();
                replyAddress = inPacket.getAddress();
                replyPort = inPacket.getPort();
                outPacket = new DatagramPacket(sendingDataBuffer, sendingDataBuffer.length, replyAddress, replyPort);
                serverSocket.send(outPacket);
            }
            System.out.println("Finished PING UDP");
        } catch (IOException e) {
            throw new UDPConnectionException("UDP Connection failed: ", e);
        }
    }


    private void receiveFile() {

    }


    private void receiveVideo() {

    }

    private void configureSocket(DatagramSocket socket) throws IOException {
        socket.setReuseAddress(socketConfig.getReuseAddress());
        socket.setReceiveBufferSize(socketConfig.getReceiveBufferSize());
        socket.setSendBufferSize(socketConfig.getSendBufferSize());
        socket.setSoTimeout(socketConfig.getTimeout());
        socket.setBroadcast(socketConfig.getBroadcast());
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
