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
import java.util.Map;

import static java.net.StandardSocketOptions.*;

public class UDPLoadSender extends LoadSender {

    private UDPClientSocketOptions socketConfig;

    public UDPLoadSender(String address, int port, Map<Integer,Long> latencyMeasurements) {
        super(address, port, latencyMeasurements);
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
        return System.nanoTime() - startTime;
    }

    private void sendPing(PingLoad load) throws UDPConnectionException {
        try (
                DatagramSocket clientSocket = new DatagramSocket();
        ){
            configureSocket(clientSocket);
            printSocketOptions(clientSocket);
            int times = load.getTimes();
            byte[] message = load.getData();
            int interval = load.getInterval_ms();
            InetAddress address = InetAddress.getByName(this.getAddress());
            int port = this.getPort();
            System.out.println("Pinging " + this.getAddress() + ":" + port + " with " +
                    message.length + " bytes of data " + load.getTimes() + " times. Protocol UDP");
            for (int i = 0; i < times; i++) {
                try {
                    this.addLatency(i, singlePing(message,address,port,clientSocket));
                    Thread.sleep(interval);
                } catch (SocketTimeoutException ignored) {}
            }
            System.out.println("Ping load test finished");
            System.out.println("Average latency: " + this.getAverageLatency() + " ns. Data size =" + this.getMeasurementNumber());
            System.out.println("Not responded packets : " + (times - this.getMeasurementNumber()));
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
