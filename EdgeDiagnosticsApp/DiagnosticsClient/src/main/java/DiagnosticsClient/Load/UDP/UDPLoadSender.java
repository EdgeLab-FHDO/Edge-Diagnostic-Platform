package DiagnosticsClient.Load.UDP;

import DiagnosticsClient.Load.ClientSocketOptions;
import DiagnosticsClient.Load.LoadTypes.DiagnosticsLoad;
import DiagnosticsClient.Load.Exception.LoadSendingException;
import DiagnosticsClient.Load.Exception.UDP.UDPConnectionException;
import DiagnosticsClient.Load.LoadSender;
import DiagnosticsClient.Load.LoadTypes.FileLoad;
import DiagnosticsClient.Load.LoadTypes.PingLoad;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

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
            case FILE -> sendFile((FileLoad) load);
        }
    }

    private void sendFile(FileLoad load) {
        System.err.println("File load not supported in UDP");
    }

    private long singlePing(byte[] data, InetAddress address, int port ,DatagramSocket socket) throws IOException {
        byte[] receivingDataBuffer = new byte[data.length];
        DatagramPacket outPackage = new DatagramPacket(data, data.length,address,port);
        DatagramPacket inPackage = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
        long startTime = System.nanoTime();
        socket.send(outPackage);
        socket.receive(inPackage);
        long latency = System.nanoTime() - startTime;
        //String response = new String(inPackage.getData());
        //System.out.println("Response: " + response + " Latency:" + latency + " ns");
        return latency;
    }

    private void sendPing(PingLoad load) throws UDPConnectionException {
        try (
                DatagramSocket clientSocket = new DatagramSocket();
        ){
            configureSocket(clientSocket);
            printSocketOptions(clientSocket);
            long[] latencies = new long[load.getTimes()];
            byte[] message = load.getData();
            int interval = load.getPingInterval_ms();
            InetAddress address = InetAddress.getByName(this.getAddress());
            int port = this.getPort();
            System.out.println("Pinging " + this.getAddress() + ":" + port + " with " +
                    message.length + " bytes of data " + load.getTimes() + " times. Protocol UDP");
            for (int i = 0; i < latencies.length; i++) {
                latencies[i] = singlePing(message,address,port,clientSocket);
                Thread.sleep(interval);
            }
            singlePing("exit".getBytes(),address,port,clientSocket); //Exit message for the server
            System.out.println("Ping load test finished");
            double avgLatency = Arrays.stream(latencies).average().orElse(0);
            System.out.println("Average latency: " + avgLatency + " ns. Data size =" + latencies.length);
        } catch (IOException | InterruptedException e) {
            throw new UDPConnectionException("UDP Connection failed: ", e);
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
