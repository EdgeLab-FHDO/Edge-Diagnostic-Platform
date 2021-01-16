package InfrastructureManager.NetworkStructure;

public class NetworkRequirements {
	private int requiredLatency;
	private double requiredThroughput;
	private double requiredPacketLoss;
	
	public int getRequiredLatency() {
		return requiredLatency;
	}
	
	public double getRequiredThroughput() {
		return requiredThroughput;
	}
	
	public double getRequiredPacketLoss() {
		return requiredPacketLoss;
	}
	
	public void setRequiredLatency(int requiredLatency) {
		this.requiredLatency =  requiredLatency;
	}
	
	public void setRequiredThroughput(double requiredThroughput) {
		this.requiredThroughput = requiredThroughput;
	}
	
	public void setRequiredPacketLoss(double requiredPacketLoss) {
		this.requiredPacketLoss = requiredPacketLoss;
	}
	
	public NetworkRequirements() {
		this.requiredLatency = 0;
		this.requiredThroughput = 0.0f;
		this.requiredPacketLoss = 0.0f;
	}
	
	public NetworkRequirements(int requiredLatency,double requiredThroughput,double requiredPacketLoss) {
		this.requiredLatency = requiredLatency;
		this.requiredThroughput = requiredThroughput;
		this.requiredPacketLoss = requiredPacketLoss;
	}
	
	
}