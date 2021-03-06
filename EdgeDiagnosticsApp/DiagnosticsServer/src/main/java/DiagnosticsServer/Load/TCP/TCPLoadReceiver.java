package DiagnosticsServer.Load.TCP;

import DiagnosticsServer.Control.BufferControl.BufferInformation;
import DiagnosticsServer.Load.Exception.LoadReceivingException;
import DiagnosticsServer.Load.Exception.TCP.TCPConnectionException;
import DiagnosticsServer.Load.LoadReceiver;
import DiagnosticsServer.Load.ServerSocketOptions;
import LoadManagement.LoadType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPLoadReceiver extends LoadReceiver {

    private ServerSocketOptions socketConfig;
    private BufferedInputStream in;

    public TCPLoadReceiver(int port) {
        super(port);
        this.socketConfig = new ServerSocketOptions();
    }

    @Override
    public void receive(LoadType loadType, BufferInformation bufferInformation) throws LoadReceivingException {
        switch (loadType) {
            case PING -> receivePing(bufferInformation);
            case FILE -> receiveFile(bufferInformation);
            case VIDEO -> receiveVideo();
        }
    }

    @Override
    public void changeSocketConfiguration(ServerSocketOptions options) {
        this.socketConfig = options;
    }

    private void receivePing(BufferInformation bufferInformation) throws LoadReceivingException {
        System.out.println("Configured for PING TCP load");
        try (ServerSocket serverSocket = new ServerSocket(this.getPort())) {
            configureSocket(serverSocket);
            try (
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
                    BufferedInputStream in = new BufferedInputStream(clientSocket.getInputStream(), bufferInformation.getInputStreamBuffer())
            ){
                while (true) {
                    int bufferSize = in.read(); //First byte to indicate message size
                    if (bufferSize < 0) break;
                    byte[] data = new byte[bufferSize];
                    int bytesReceived = in.read(data);
                    out.println("Data Read: " + bytesReceived + " bytes");
                }
                System.out.println("Finished PING TCP");
            }
        } catch (IOException e) {
            throw new TCPConnectionException("TCP connection failed", e);
        }
    }


    private void receiveFile(BufferInformation bufferInformation) throws LoadReceivingException {
        System.out.println("Configured for FILE TCP load");
        try (ServerSocket serverSocket = new ServerSocket(this.getPort())) {
            configureSocket(serverSocket);
            File tempFile = File.createTempFile("file_load_test","tmp");
            FileOutputStream outFile = null;
            try (
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
                    BufferedInputStream in = new BufferedInputStream(clientSocket.getInputStream(),
                            bufferInformation.getInputStreamBuffer());
                    DataInputStream in2 = new DataInputStream(clientSocket.getInputStream())
            ){
                final int fileSize = in2.readInt();
                int sizeCounter;
                int count = 0;
                while (count >= 0) {
                    sizeCounter = fileSize;
                    outFile = new FileOutputStream(tempFile);
                    byte[] buffer = new byte[bufferInformation.getReceiveBuffer()];
                    while (sizeCounter > 0 && (count = in.read(buffer)) > 0) {
                        outFile.write(buffer, 0, count);
                        sizeCounter -= count;
                    }
                    outFile.close();
                    long receivedData = tempFile.length();
                    String response = "Received " + receivedData + " bytes";
                    out.println(response);
                }
                System.out.println("Finished FILE TCP");
            }
            finally {
                if (outFile != null) outFile.close();
            }
        } catch (IOException e) {
            throw new TCPConnectionException("TCP connection failed", e);
        }
    }


    private void receiveVideo() {

    }

    private void configureSocket(ServerSocket socket) throws SocketException {
        socket.setReceiveBufferSize(socketConfig.getReceiveBufferSize());
        socket.setReuseAddress(socketConfig.getReuseAddress());
        socket.setSoTimeout(socketConfig.getTimeout());
    }

    private void printSocketOptions(ServerSocket socket) throws IOException {
        System.out.println("TCP Socket properties: ");
        var options = socket.supportedOptions();
        for (var option: options) {
            System.out.print("\t" + option.name() + " : ");
            System.out.println(socket.getOption(option));
        }
        System.out.println("\t" + "SO_TIMEOUT : " + socket.getSoTimeout());
    }


}
