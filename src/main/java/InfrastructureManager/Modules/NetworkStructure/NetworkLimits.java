package InfrastructureManager.Modules.NetworkStructure;

public class NetworkLimits {
	private float latency;
	private float jitter;
	private double throughput;
	private double packetLoss;
	
	public NetworkLimits(float latency,float jitter,double throughput,double packetLoss) {
		this.latency = latency;
		this.jitter = jitter;
		this.throughput = throughput;
		this.packetLoss = packetLoss;
	}
	
	public float getLatency() {
		return latency;
	}
	
	public float getJitter() {
		return jitter;
	}
	
	public double getThroughput() {
		return throughput;
	}
	
	public double getPacketLoss() {
		return packetLoss;
	}
	
}