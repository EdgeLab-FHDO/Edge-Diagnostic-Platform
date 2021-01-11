package InfrastructureManager.AdvantEdge.NetworkStructure;

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
	
	public setCpuCount(int cpuCount) {
		this.cpuCount = cpuCount;
	}
	
	public setGpuCount(int gpuCount) {
		this.gpuCount = gpuCount;
	}
	
	public setMemoryLimit(int memoryLimit) {
		this.memoryLimit = memoryLimit;
	}
	
}