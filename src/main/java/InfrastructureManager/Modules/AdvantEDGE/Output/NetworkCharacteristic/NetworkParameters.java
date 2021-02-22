package InfrastructureManager.Modules.AdvantEDGE.Output.NetworkCharacteristic;

/**
 * Class to model the different network characteristics that can be applied in AdvantEdge to Scenarios,
 * Domains, Zones, among other elements.
 * <p>
 * These effectively allow to simulate changes in the network in an edge deployment.
 * <p>
 * The class wraps all possible network characteristics and provides setters and getters for each of them.
 *
 * @see <a href="https://github.com/InterDigitalInc/AdvantEDGE/wiki/platform-concepts#network-characteristics">AdvantEdge Wiki</a>
 */
public class NetworkParameters {
    private int throughputDl;
    private int throughputUl;
    private int latency;
    private int latencyVariation;
    private int packetLoss;

    /**
     * Creates a new NetworkParameters object based on the different network characteristics
     *
     * @param throughputDl     Maximum amount of data moved between two points in the network (DownLink direction) (Mbps)
     * @param throughputUl     Maximum amount of data moved between two points in the network (UpLink direction) (Mbps)
     * @param latency          Amount of time that a packet takes to traverse a network to its final destination (ms)
     * @param latencyVariation Variation of the latency parameter (ms)
     * @param packetLoss       Packet loss occurs when data packets travelling across the network fail to reach their destination (%)
     */
    public NetworkParameters(int throughputDl, int throughputUl, int latency, int latencyVariation, int packetLoss)
    {
        this.setThroughputDl(throughputDl);
        this.setLatency(latency);
        this.setThroughputUl(throughputUl);
        this.setLatencyVariation(latencyVariation);
        this.setPacketLoss(packetLoss);
    }

    /**
     * Gets throughput dl.
     *
     * @return the throughput dl
     */
    public int getThroughputDl() {
        return throughputDl;
    }

    /**
     * Sets throughput dl.
     *
     * @param throughputDl the throughput dl
     */
    public void setThroughputDl(int throughputDl) {
        this.throughputDl = throughputDl;
    }

    /**
     * Gets throughput ul.
     *
     * @return the throughput ul
     */
    public int getThroughputUl() {
        return throughputUl;
    }

    /**
     * Sets throughput ul.
     *
     * @param throughputUl the throughput ul
     */
    public void setThroughputUl(int throughputUl) {
        this.throughputUl = throughputUl;
    }

    /**
     * Gets latency.
     *
     * @return the latency
     */
    public int getLatency() {
        return latency;
    }

    /**
     * Sets latency.
     *
     * @param latency the latency
     */
    public void setLatency(int latency) {
        this.latency = latency;
    }

    /**
     * Gets latency variation.
     *
     * @return the latency variation
     */
    public int getLatencyVariation() {
        return latencyVariation;
    }

    /**
     * Sets latency variation.
     *
     * @param latencyVariation the latency variation
     */
    public void setLatencyVariation(int latencyVariation) {
        this.latencyVariation = latencyVariation;
    }

    /**
     * Gets packet loss.
     *
     * @return the packet loss
     */
    public int getPacketLoss() {
        return packetLoss;
    }

    /**
     * Sets packet loss.
     *
     * @param packetLoss the packet loss
     */
    public void setPacketLoss(int packetLoss) {
        this.packetLoss = packetLoss;
    }
}
