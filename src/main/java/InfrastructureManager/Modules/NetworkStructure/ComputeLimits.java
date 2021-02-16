package InfrastructureManager.Modules.NetworkStructure;

/**
 * This class contains Computation limitation of the device.
 *
 * @author Shankar Lokeshwara
 */
public class ComputeLimits {
	private float cpuCount; // Number of CPU's available
	private float gpuCount; // Number of GPU's available
	private float memoryLimit; // Mega Bytes of memory available


	/**
	 * Parameterized constructor for class ComputeLimits
	 * @param cpuCount 		float
	 * @param gpuCount 		float
	 * @param memoryLimit 	float
	 */
	public ComputeLimits(float cpuCount,float gpuCount, float memoryLimit) {
		this.cpuCount = cpuCount;
		this.gpuCount = gpuCount;
		this.memoryLimit = memoryLimit;
	}

	/**
	 * Getter function
	 * @return cpuCount
	 */
	public float getCpuCount() {
		return cpuCount;
	}

	/**
	 * Getter function
	 * @return gpuCount
	 */

	public float getGpuCount() {
		return gpuCount;
	}

	/**
	 * Getter function
	 * @return memoryLimit
	 */
	public float getMemoryLimit() {
		return memoryLimit;
	}

}