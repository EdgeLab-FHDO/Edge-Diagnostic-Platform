package DiagnosticsClient.Load;

import DiagnosticsClient.Control.BufferInformation;
import DiagnosticsClient.Load.Exception.LoadSendingException;
import DiagnosticsClient.Load.LoadTypes.DiagnosticsLoad;

import java.util.*;

public abstract class LoadSender {

    private final String address;
    private final int port;
    private List<Double> latencyMeasurements;

    public LoadSender(String address, int port, List<Double> latencyMeasurements) {
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

    protected double addAverageLatency(long[] latencies) {
        double average = Arrays.stream(latencies).average().orElse(-1);
        this.latencyMeasurements.add(average);
        return average;
    }

    public void setLatencyMeasurements(List<Double> latencyMeasurements) {
        this.latencyMeasurements = latencyMeasurements;
    }

    public abstract void send(DiagnosticsLoad load, BufferInformation bufferInformation) throws LoadSendingException;
    public abstract void changeSocketConfiguration(ClientSocketOptions options);
}
