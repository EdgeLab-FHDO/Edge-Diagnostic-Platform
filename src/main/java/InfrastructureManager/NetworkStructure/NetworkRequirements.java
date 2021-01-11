package InfrastructureManager.AdvantEdge.NetworkStructure;

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
	
	public setRequiredLatency(int requiredLatency) {
		this.requiredLatency =  requiredLatency;
	}
	
	public setRequiredThroughput(double requiredThroughput) {
		this.requiredThroughput = requiredThroughput;
	}
	
	public setRequiredPacketLoss(double requiredPacketLoss) {
		this.requiredPacketLoss = requiredPacketLoss;
	}
	
}