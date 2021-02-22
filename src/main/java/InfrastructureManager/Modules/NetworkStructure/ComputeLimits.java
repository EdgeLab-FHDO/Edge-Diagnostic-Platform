package InfrastructureManager.Modules.NetworkStructure;

public class ComputeLimits {
	private float cpuCount; // Number of CPU's available
	private float gpuCount; // Number of GPU's available
	private float memoryLimit; // Mega Bytes of memory available
	
	
	public ComputeLimits(float cpuCount,float gpuCount, float memoryLimit) {
        this.cpuCount = cpuCount;
        this.gpuCount = gpuCount;
        this.memoryLimit = memoryLimit;
    }
	
	public float getCpuCount() {
		return cpuCount;
	}
	
	public float getGpuCount() {
		return gpuCount;
	}
	
	public float getMemoryLimit() {
		return memoryLimit;
	}
	
}