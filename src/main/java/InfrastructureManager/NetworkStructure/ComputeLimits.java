package InfrastructureManager.NetworkStructure;

public class ComputeLimits {
	private int cpuCount;
	private int gpuCount;
	private int memoryLimit;
	
	public int getCpuCount() {
		return cpuCount;
	}
	
	public int getGpuCount() {
		return gpuCount;
	}
	
	public int getMemoryLimit() {
		return memoryLimit;
	}
	
	public void setCpuCount(int cpuCount) {
		this.cpuCount = cpuCount;
	}
	
	public void setGpuCount(int gpuCount) {
		this.gpuCount = gpuCount;
	}
	
	public void setMemoryLimit(int memoryLimit) {
		this.memoryLimit = memoryLimit;
	}
	
    public ComputeLimits() {
        this.cpuCount = 0;
        this.gpuCount = 0;
        this.memoryLimit = 0;
    }

    public ComputeLimits(int cpuCount,int gpuCount, int memoryLimit) {
        this.cpuCount = cpuCount;
        this.gpuCount = gpuCount;
        this.memoryLimit = memoryLimit;
    }
	
}