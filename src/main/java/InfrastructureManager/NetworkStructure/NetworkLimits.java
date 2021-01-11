package InfrastructureManager.AdvantEdge.NetworkStructure;

public class NetworkLimits {
	private int latency;
	private double throughput;
	private double packetLoss;
	
	public int getLatency() {
		return latency;
	}
	
	public double getThroughput() {
		return throughput;
	}
	
	public double getPacketLoss() {
		return packetLoss;
	}
	
	public setLatency(int latency) {
		this.latency =  latency;
	}
	
	public setThroughput(double throughput) {
		this.throughput = throughput;
	}
	
	public setPacketLoss(double packetLoss) {
		this.packetLoss = packetLoss;
	}
	
}