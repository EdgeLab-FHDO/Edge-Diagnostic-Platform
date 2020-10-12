package InfrastructureManager.AdvantEdge.NetworkCharacteristic;

public class NetworkParameters {
    private int throughputDl;
    private int throughputUl;
    private int latency;
    private int latencyVariation;
    private int packetLoss;

    public NetworkParameters(int throughputDl, int throughputUl, int latency, int latencyVariation, int packetLoss)
    {
        this.setThroughputDl(throughputDl);
        this.setLatency(latency);
        this.setThroughputUl(throughputUl);
        this.setLatencyVariation(latencyVariation);
        this.setPacketLoss(packetLoss);
    }

    public int getThroughputDl() {
        return throughputDl;
    }

    public void setThroughputDl(int throughputDl) {
        this.throughputDl = throughputDl;
    }

    public int getThroughputUl() {
        return throughputUl;
    }

    public void setThroughputUl(int throughputUl) {
        this.throughputUl = throughputUl;
    }

    public int getLatency() {
        return latency;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }

    public int getLatencyVariation() {
        return latencyVariation;
    }

    public void setLatencyVariation(int latencyVariation) {
        this.latencyVariation = latencyVariation;
    }

    public int getPacketLoss() {
        return packetLoss;
    }

    public void setPacketLoss(int packetLoss) {
        this.packetLoss = packetLoss;
    }
}
