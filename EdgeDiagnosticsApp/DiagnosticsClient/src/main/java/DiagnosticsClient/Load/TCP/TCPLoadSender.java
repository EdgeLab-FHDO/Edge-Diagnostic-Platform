package DiagnosticsClient.Load.TCP;

import DiagnosticsClient.Control.BufferInformation;
import DiagnosticsClient.Load.ClientSocketOptions;
import DiagnosticsClient.Load.Exception.TCP.TCPConnectionException;
import DiagnosticsClient.Control.FileBufferInformation;
import DiagnosticsClient.Load.LoadSender;
import DiagnosticsClient.Load.LoadTypes.DiagnosticsLoad;
import DiagnosticsClient.Load.LoadTypes.FileLoad;
import DiagnosticsClient.Load.LoadTypes.PingLoad;

import java.io.*;
import java.net.Socket;
import java.util.List;

import static java.net.StandardSocketOptions.IP_TOS;
import static java.net.StandardSocketOptions.SO_LINGER;

public class TCPLoadSender extends LoadSender {

    private TCPClientSocketOptions socketConfig;

    public TCPLoadSender(String address, int port, List<Double> latencyMeasurements) {
        super(address, port,latencyMeasurements);
        socketConfig = new TCPClientSocketOptions(); //Start with default values
    }

    @Override
    public void send(DiagnosticsLoad load, BufferInformation bufferInformation) throws TCPConnectionException {
        switch (load.getType()) {
            case PING -> sendPing((PingLoad)load, bufferInformation);
            case FILE -> sendFile((FileLoad) load, (FileBufferInformation) bufferInformation);
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
        in.readLine();
        return System.nanoTime() - startTime;
    }

    private void sendPing(PingLoad load, BufferInformation bufferInformation) throws TCPConnectionException {
        String address = this.getAddress();
        int port = this.getPort();
        byte[] pingMessage = load.getData();
        int dataLength = pingMessage.length;
        int times = load.getTimes();
        int interval = load.getInterval_ms();
        if (dataLength > 255 | dataLength < 1) throw new TCPConnectionException("Invalid length for ping message");
        String screenMessage = "Pinging " + address + ":" + port + " with " +
                dataLength + " bytes of data " + times + " times. Protocol TCP";
        try (
                Socket clientSocket = new Socket(address,port);
                BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream(),
                        bufferInformation.getOutputStreamBuffer());
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()),
                        bufferInformation.getInputReaderBuffer())
        ) {
            configureSocket(clientSocket);
            //printSocketOptions(clientSocket);
            System.out.println(screenMessage);
            long[] latencies = new long[times];
            for (int i = 0; i < times; i++) {
                latencies[i] = singlePing(pingMessage,out,in);
                Thread.sleep(interval);
            }
            System.out.println("Ping load test finished");
            double avgLatency = this.addAverageLatency(latencies);
            System.out.println("Average latency: " + avgLatency + " ns. Data size =" + times);
        } catch (IOException | InterruptedException e) {
            throw new TCPConnectionException("TCP Connection failed: ",e);
        }
    }

    private long singleFile(File load, BufferedOutputStream out, BufferedReader in,
                            int fileReadBufferSize, int fileInputBufferSize) throws TCPConnectionException {
        try (
                BufferedInputStream inFile = new BufferedInputStream(new FileInputStream(load), fileInputBufferSize)
        ){
            int bytesRead;
            long startTime = System.nanoTime();
            byte[] buffer = new byte[fileReadBufferSize];
            while ((bytesRead = inFile.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            in.readLine();
            return System.nanoTime() - startTime;
        } catch (IOException e) {
            throw new TCPConnectionException("Sending file failed", e);
        }
    }

    private void sendFile(FileLoad load, FileBufferInformation bufferInformation) throws TCPConnectionException {
        String address = this.getAddress();
        int port = this.getPort();
        int times = load.getTimes();
        int interval = load.getInterval_ms();
        File fileToSend = load.getFileToSend();
        long fileSize = load.getDataLength();
        String fileName = fileToSend.getName();
        try (
                Socket clientSocket = new Socket(address, port);
                BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream(),
                        bufferInformation.getOutputStreamBuffer());
                DataOutputStream out2 = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()),
                        bufferInformation.getFileInputBuffer())
        ){
            configureSocket(clientSocket);
            System.out.println("Sending file " + fileName + "(" + fileSize + " bytes) to " + address
                    + ":" + port + ". Protocol TCP");
            out2.writeInt((int) fileSize); //Send file size first as a 32bit number (Max size 2GB)
            int fileBuffer = bufferInformation.getFileReadingBuffer();
            int fileInputBuffer = bufferInformation.getFileInputBuffer();
            long[] latencies = new long[times];
            for (int i = 0; i < times; i++) {
                latencies[i] = singleFile(fileToSend,out,in, fileBuffer, fileInputBuffer);
                Thread.sleep(interval);
            }
            System.out.println("File load test finished");
            double avgLatency = this.addAverageLatency(latencies);
            System.out.println("Average latency: " + avgLatency + " ns. Times that the File was sent = "
                    + times);
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
