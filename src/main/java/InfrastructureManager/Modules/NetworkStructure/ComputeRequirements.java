package InfrastructureManager.Modules.NetworkStructure;


/**
 * This class contains Computation requirements of the application.
 *
 * @author Shankar Lokeshwara
 */

public class ComputeRequirements {
	private float requiredCpu;
	private float requiredGpu;
	private float requiredMemory;

	/**
	 * Parameterized constructor for class ComputeRequirements
	 * @param requiredCpu 		integer
	 * @param requiredGpu 		integer
	 * @param requiredMemory 	integer
	 */
	public ComputeRequirements(float requiredCpu,float requiredGpu, float requiredMemory) {
		this.requiredCpu = requiredCpu;
		this.requiredGpu = requiredGpu;
		this.requiredMemory = requiredMemory;
	}

	/**
	 * Getter function
	 * @return requiredCpu
	 */
	public float getRequiredCpu() {
		return requiredCpu;
	}

	/**
	 * Getter function
	 * @return requiredGpu
	 */
	public float getRequiredGpu() {
		return requiredGpu;
	}

	/**
	 * Getter function
	 * @return requiredMemory
	 */
	public float getRequiredMemory() {
		return requiredMemory;
	}

}