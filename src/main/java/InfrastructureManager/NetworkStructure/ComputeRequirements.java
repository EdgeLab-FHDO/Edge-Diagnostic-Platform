package InfrastructureManager.AdvantEdge.NetworkStructure;

public class ComputeRequirements {
	private int requiredCpu;
	private int requiredGpu;
	private int requiredMemory;
	
	public int getRequiredCpu() {
		return requiredCpu;
	}
	
	public int getRequiredGpu() {
		return requiredGpu;
	}
	
	public int getRequiredMemory() {
		return requiredMemory;
	}
	
	public setRequiredCpu(int requiredCpu) {
		this.requiredCpu = requiredCpu;
	}
	
	public setRequiredGpu(int requiredGpu) {
		this.requiredGpu = requiredGpu;
	}
	
	public setRequiredMemory(int requiredMemory) {
		this.requiredMemory = requiredMemory;
	}
	
}