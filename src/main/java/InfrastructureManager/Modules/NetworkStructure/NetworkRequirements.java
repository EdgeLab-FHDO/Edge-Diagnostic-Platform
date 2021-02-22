package InfrastructureManager.Modules.NetworkStructure;

public class NetworkRequirements {
	private float requiredLatency;
	private float requiredJitter;
	private double requiredThroughput;
	private double requiredPacketLoss;
	
	public NetworkRequirements(float requiredLatency,float requiredJitter,double requiredThroughput,double requiredPacketLoss) {
		this.requiredLatency = requiredLatency;
		this.requiredJitter = requiredJitter;
		this.requiredThroughput = requiredThroughput;
		this.requiredPacketLoss = requiredPacketLoss;
	}
	
	public float getRequiredLatency() {
		return requiredLatency;
	}
	
	public float getRequiredJitter() {
		return requiredJitter;
	}
	
	public double getRequiredThroughput() {
		return requiredThroughput;
	}
	
	public double getRequiredPacketLoss() {
		return requiredPacketLoss;
	}
	
}