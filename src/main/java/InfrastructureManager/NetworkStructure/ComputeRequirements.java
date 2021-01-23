package InfrastructureManager.NetworkStructure;


/**
 * This class contains Computation requirements of the application.
 *
 * @author Shankar Lokeshwara
 */

public class ComputeRequirements {
	private int requiredCpu;
	private int requiredGpu;
	private int requiredMemory;
	
	/**
	* Default constructor for class ComputeRequirements
	*/
    public ComputeRequirements() {
        this.requiredCpu = 0;
        this.requiredGpu = 0;
        this.requiredMemory = 0;
    }

	/**
	* Parameterized constructor for class ComputeRequirements
	* @param requiredCpu 		integer
	* @param requiredGpu 		integer
	* @param requiredMemory 	integer
	*/
    public ComputeRequirements(int requiredCpu,int requiredGpu, int requiredMemory) {
        this.requiredCpu = requiredCpu;
        this.requiredGpu = requiredGpu;
        this.requiredMemory = requiredMemory;
    }
	
	/**
	 * Getter function
	 * @return requiredCpu
	 */
	public int getRequiredCpu() {
		return requiredCpu;
	}
	
	/**
	 * Getter function
	 * @return requiredGpu
	 */
	public int getRequiredGpu() {
		return requiredGpu;
	}
	
	/**
	 * Getter function
	 * @return requiredMemory
	 */
	public int getRequiredMemory() {
		return requiredMemory;
	}
	
	/**
	 * Setter function
	 * @param requiredCpu
	 */
	public void setRequiredCpu(int requiredCpu) {
		this.requiredCpu = requiredCpu;
	}
	
	/**
	 * Setter function
	 * @param requiredGpu
	 */
	public void setRequiredGpu(int requiredGpu) {
		this.requiredGpu = requiredGpu;
	}
	
	/**
	 * Setter function
	 * @param requiredMemory
	 */
	public void setRequiredMemory(int requiredMemory) {
		this.requiredMemory = requiredMemory;
	}
		
}