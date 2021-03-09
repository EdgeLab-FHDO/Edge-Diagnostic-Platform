package DiagnosticsClient.Load;

import DiagnosticsClient.Load.Exception.TCP.TCPConnectionException;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPLoadSender extends LoadSender {

    public TCPLoadSender(String address, int port) {
        super(address, port);
    }

    @Override
    public void send(DiagnosticsLoad load) throws TCPConnectionException {
        switch (load.getType()) {
            case PING -> sendPing((PingLoad)load);
            case FILE -> sendFile();
            case VIDEO -> sendVideo();
        }
    }

    private long singlePing(byte[] pingData, BufferedOutputStream out, BufferedReader in) throws IOException {
        long startTime = System.nanoTime();
        out.write(pingData.length);
        out.write(pingData);
        out.flush();
        String response = in.readLine();
        long latency = System.nanoTime() - startTime;
        //System.out.println("Response: " + response + " Latency:" + latency + " ns");
        return latency;
    }

    private void sendPing(PingLoad load) throws TCPConnectionException {
        String address = this.getAddress();
        int port = this.getPort();
        byte[] pingMessage = load.getData();
        int dataLength = pingMessage.length;
        int times = load.getTimes();
        int messagesSent = 0;
        int interval = load.getPingInterval_ms();
        List<Long> latencies = new ArrayList<>();
        if (dataLength > 255 | dataLength < 1) throw new TCPConnectionException("Invalid length for ping message");
        String screenMessage = "Pinging " + address + ":" + port + " with " +
                dataLength + " bytes of data " + times + " times. Protocol TCP";
        try (
                Socket clientSocket = new Socket(address,port);
                BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            System.out.println(screenMessage);
            while (messagesSent < times) {
                latencies.add(singlePing(pingMessage,out,in));
                Thread.sleep(interval);
                messagesSent++;
            }
            System.out.println("Ping load test finished");
            double avgLatency = latencies.stream().mapToLong(i -> i).average().orElse(0);
            System.out.println("Average latency: " + avgLatency + " ns. Data size =" + latencies.size());
        } catch (IOException | InterruptedException e) {
            throw new TCPConnectionException("TCP Connection failed: ",e);
        }
    }

    private void sendFile() {

    }

    private void sendVideo() {

    }
}
