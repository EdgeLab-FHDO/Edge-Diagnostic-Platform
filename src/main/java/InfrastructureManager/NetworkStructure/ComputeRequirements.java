package InfrastructureManager.NetworkStructure;

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
	
	public void setRequiredCpu(int requiredCpu) {
		this.requiredCpu = requiredCpu;
	}
	
	public void setRequiredGpu(int requiredGpu) {
		this.requiredGpu = requiredGpu;
	}
	
	public void setRequiredMemory(int requiredMemory) {
		this.requiredMemory = requiredMemory;
	}
	
    public ComputeRequirements() {
        this.requiredCpu = 0;
        this.requiredGpu = 0;
        this.requiredMemory = 0;
    }

    public ComputeRequirements(int requiredCpu,int requiredGpu, int requiredMemory) {
        this.requiredCpu = requiredCpu;
        this.requiredGpu = requiredGpu;
        this.requiredMemory = requiredMemory;
    }
	
	
}