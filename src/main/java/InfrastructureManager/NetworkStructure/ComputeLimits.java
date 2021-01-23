package InfrastructureManager.NetworkStructure;

/**
 * This class contains Computation limitation of the device.
 *
 * @author Shankar Lokeshwara
 */
public class ComputeLimits {
	private int cpuCount; // Number of CPU's available
	private int gpuCount; // Number of GPU's available
	private int memoryLimit; // Mega Bytes of memory available
	
	
	/**
	* Default constructor for class ComputeLimits
	*/
    public ComputeLimits() {
        this.cpuCount = 0;
        this.gpuCount = 0;
        this.memoryLimit = 0;
    }
	/**
	* Parameterized constructor for class ComputeLimits
	* @param cpuCount 		integer
	* @param gpuCount 		integer
	* @param memoryLimit 	integer
	*/
    public ComputeLimits(int cpuCount,int gpuCount, int memoryLimit) {
        this.cpuCount = cpuCount;
        this.gpuCount = gpuCount;
        this.memoryLimit = memoryLimit;
    }
	
	/**
	 * Getter function
	 * @return cpuCount
	 */
	public int getCpuCount() {
		return cpuCount;
	}
	
	/**
	 * Getter function
	 * @return gpuCount
	 */
	
	public int getGpuCount() {
		return gpuCount;
	}
	
	/**
	 * Getter function
	 * @return memoryLimit
	 */
	public int getMemoryLimit() {
		return memoryLimit;
	}
	
	/**
	 * Setter function
	 * @param cpuCount
	 */
	
	public void setCpuCount(int cpuCount) {
		this.cpuCount = cpuCount;
	}
	
	/**
	 * Setter function
	 * @param gpuCount
	 */
	
	public void setGpuCount(int gpuCount) {
		this.gpuCount = gpuCount;
	}
	
	/**
	 * Setter function
	 * @param memoryLimit
	 */
	
	public void setMemoryLimit(int memoryLimit) {
		this.memoryLimit = memoryLimit;
	}
	
}