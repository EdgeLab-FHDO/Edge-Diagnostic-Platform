package InfrastructureManager.NetworkStructure;

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
	
	public void setLatency(int latency) {
		this.latency =  latency;
	}
	
	public void setThroughput(double throughput) {
		this.throughput = throughput;
	}
	
	public void setPacketLoss(double packetLoss) {
		this.packetLoss = packetLoss;
	}
	
	public NetworkLimits() {
		this.latency = 0;
		this.throughput = 0.0f;
		this.packetLoss = 0.0f;
	}
	
	public NetworkLimits(int latency,double throughput,double packetLoss) {
		this.latency = latency;
		this.throughput = throughput;
		this.packetLoss = packetLoss;
	}
	
}