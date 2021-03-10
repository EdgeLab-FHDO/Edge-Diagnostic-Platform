package DiagnosticsClient.Load;

import DiagnosticsClient.Load.Exception.LoadSendingException;

import java.io.IOException;
import java.net.*;

import static java.net.StandardSocketOptions.*;

public class UDPLoadSender extends LoadSender {

    private UDPClientSocketOptions socketConfig;

    public UDPLoadSender(String address, int port) {
        super(address, port);
        this.socketConfig = new UDPClientSocketOptions();
    }

    @Override
    public void send(DiagnosticsLoad load) throws LoadSendingException {
        switch (load.getType()) {
            case PING -> sendPing((PingLoad) load);
        }
    }

    private void sendPing(PingLoad load) {
        try (
                DatagramSocket clientSocket = new DatagramSocket();
        ){
            configureSocket(clientSocket);
            printSocketOptions(clientSocket);
            byte[] message = load.getData();
            InetAddress address = InetAddress.getByName(this.getAddress());
            int port = this.getPort();
            DatagramPacket packet = new DatagramPacket(message, message.length,address,port);
            clientSocket.send(packet);
            packet = new DatagramPacket(message, message.length);
            clientSocket.receive(packet);
            String received = new String(packet.getData());
            System.out.println("Received: " + received);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void changeSocketConfiguration(ClientSocketOptions options) {
        this.socketConfig = (UDPClientSocketOptions) options;
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
