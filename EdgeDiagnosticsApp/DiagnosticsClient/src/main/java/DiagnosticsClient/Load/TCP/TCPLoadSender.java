package DiagnosticsClient.Load.TCP;

import DiagnosticsClient.Load.ClientSocketOptions;
import DiagnosticsClient.Load.LoadTypes.DiagnosticsLoad;
import DiagnosticsClient.Load.Exception.TCP.TCPConnectionException;
import DiagnosticsClient.Load.LoadSender;
import DiagnosticsClient.Load.LoadTypes.FileLoad;
import DiagnosticsClient.Load.LoadTypes.PingLoad;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import static java.net.StandardSocketOptions.IP_TOS;
import static java.net.StandardSocketOptions.SO_LINGER;

public class TCPLoadSender extends LoadSender {

    private TCPClientSocketOptions socketConfig;

    public TCPLoadSender(String address, int port) {
        super(address, port);
        socketConfig = new TCPClientSocketOptions(); //Start with default values
    }

    @Override
    public void send(DiagnosticsLoad load) throws TCPConnectionException {
        switch (load.getType()) {
            case PING -> sendPing((PingLoad)load);
            case FILE -> sendFile((FileLoad) load);
            case VIDEO -> sendVideo();
        }
    }

    @Override
    public void changeSocketConfiguration(ClientSocketOptions options) {
        this.socketConfig = (TCPClientSocketOptions) options;
    }

    private long singlePing(byte[] pingData, BufferedOutputStream out, BufferedReader in) throws IOException {
        long startTime = System.nanoTime();
        out.write(pingData.length);
        out.write(pingData);
        out.flush();
        String response = in.readLine();
        //System.out.println("Response: " + response + " Latency:" + latency + " ns");
        return System.nanoTime() - startTime;
    }

    private void sendPing(PingLoad load) throws TCPConnectionException {
        String address = this.getAddress();
        int port = this.getPort();
        byte[] pingMessage = load.getData();
        int dataLength = pingMessage.length;
        int times = load.getTimes();
        int interval = load.getPingInterval_ms();
        long[] latencies = new long[load.getTimes()];
        if (dataLength > 255 | dataLength < 1) throw new TCPConnectionException("Invalid length for ping message");
        String screenMessage = "Pinging " + address + ":" + port + " with " +
                dataLength + " bytes of data " + times + " times. Protocol TCP";
        try (
                Socket clientSocket = new Socket(address,port);
                BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            configureSocket(clientSocket);
            printSocketOptions(clientSocket);
            System.out.println(screenMessage);
            for (int i = 0; i < latencies.length; i++) {
                latencies[i] = singlePing(pingMessage,out,in);
                Thread.sleep(interval);
            }
            System.out.println("Ping load test finished");
            double avgLatency = Arrays.stream(latencies).average().orElse(0);
            System.out.println("Average latency: " + avgLatency + " ns. Data size =" + latencies.length);
        } catch (IOException | InterruptedException e) {
            throw new TCPConnectionException("TCP Connection failed: ",e);
        }
    }

    private long singleFile(File load, BufferedOutputStream out, BufferedReader in) throws TCPConnectionException {
        try (
                BufferedInputStream inFile = new BufferedInputStream(new FileInputStream(load))
        ){
            int bytesRead;
            long startTime = System.nanoTime();
            byte[] buffer = new byte[4 * 1024];
            while ((bytesRead = inFile.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            String response = in.readLine();
            //System.out.println("Response: " + response);
            return System.nanoTime() - startTime;
        } catch (IOException e) {
            throw new TCPConnectionException("Sending file failed", e);
        }
    }

    private void sendFile(FileLoad load) throws TCPConnectionException {
        String address = this.getAddress();
        int port = this.getPort();
        int times = load.getTimes();
        int interval = load.getWaitBetweenSend_ms();
        File fileToSend = load.getFileToSend();
        long fileSize = fileToSend.length();
        String fileName = fileToSend.getName();
        long[] latencies = new long[times];
        try (
                Socket clientSocket = new Socket(address, port);
                BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
                DataOutputStream out2 = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ){
            configureSocket(clientSocket);
            System.out.println("Sending file " + fileName + "(" + fileSize + " bytes) to " + address
                    + ":" + port + ". Protocol TCP");
            out2.writeInt((int) fileSize); //Send file size first as a 32bit number (Max size 2GB)
            for (int i = 0; i < times; i++) {
                latencies[i] = singleFile(fileToSend,out,in);
                Thread.sleep(interval);
            }
            System.out.println("File load test finished");
            double avgLatency = Arrays.stream(latencies).average().orElse(0);
            System.out.println("Average latency: " + avgLatency + " ns. Times that the File was sent = "
                    + latencies.length);
            fileToSend.deleteOnExit();
        } catch (IOException | InterruptedException e) {
            throw new TCPConnectionException("TCP Connection failed: ",e);
        }
    }

    private void sendVideo() {

    }

    private void configureSocket(Socket socket) throws IOException {
        socket.setKeepAlive(socketConfig.getKeepAlive());
        socket.setTcpNoDelay(!socketConfig.getNagleAlgorithm());
        socket.setReuseAddress(socketConfig.getReuseAddress());
        socket.setReceiveBufferSize(socketConfig.getReceiveBufferSize());
        socket.setSendBufferSize(socketConfig.getSendBufferSize());
        socket.setOption(SO_LINGER,socketConfig.getLinger());
        socket.setOption(IP_TOS,socketConfig.getIpTOS());
        socket.setSoTimeout(socketConfig.getTimeout());
    }

    private void printSocketOptions(Socket socket) throws IOException {
        System.out.println("TCP Socket properties: ");
        var options = socket.supportedOptions();
        for (var option: options) {
            System.out.print("\t" + option.name() + " : ");
            System.out.println(socket.getOption(option));
        }
        System.out.println("\t" + "SO_TIMEOUT : " + socket.getSoTimeout());
    }
}
