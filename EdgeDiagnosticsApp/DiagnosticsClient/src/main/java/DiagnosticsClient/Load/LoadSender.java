package DiagnosticsClient.Load;

import DiagnosticsClient.Load.Exception.LoadSendingException;
import DiagnosticsClient.Load.LoadTypes.DiagnosticsLoad;

import java.util.Map;

public abstract class LoadSender {

    private final String address;
    private final int port;
    private final Map<Integer,Long> latencyMeasurements;

    public LoadSender(String address, int port, Map<Integer,Long> latencyMeasurements) {
        this.address = address;
        this.port = port;
        this.latencyMeasurements = latencyMeasurements;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    protected void addLatency(int iteration, long latency) {
        this.latencyMeasurements.put(iteration,latency);
    }

    protected double getAverageLatency() {
        return latencyMeasurements.values().stream().mapToLong(i -> i).average().orElse(0);
    }

    protected int getMeasurementNumber() {
        return latencyMeasurements.size();
    }

    public abstract void send(DiagnosticsLoad load, BufferInformation bufferInformation) throws LoadSendingException;
    public abstract void changeSocketConfiguration(ClientSocketOptions options);
}
