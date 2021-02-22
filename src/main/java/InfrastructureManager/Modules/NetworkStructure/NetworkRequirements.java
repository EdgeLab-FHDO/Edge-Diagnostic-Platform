package InfrastructureManager.Modules.NetworkStructure;

/**
 * This class contains NetworkRequirements .
 *
 * @author Shankar Lokeshwara
 */

public class NetworkRequirements {
	private float requiredLatency;
	private float requiredJitter;
	private double requiredThroughput;
	private double requiredPacketLoss;

	/**
	 * Parameterized constructor for NetworkRequirements Class
	 * @param requiredLatency 	
	 * @param requiredThroughput 
	 * @param requiredPacketLoss 			
	 */
	public NetworkRequirements(float requiredLatency,float requiredJitter,double requiredThroughput,double requiredPacketLoss) {
		this.requiredLatency = requiredLatency;
		this.requiredJitter = requiredJitter;
		this.requiredThroughput = requiredThroughput;
		this.requiredPacketLoss = requiredPacketLoss;
	}

	/**
	 * Getter function
	 * @return requiredLatency
	 */
	public float getRequiredLatency() {
		return requiredLatency;
	}

	/**
	 * Getter function
	 * @return requiredJitter
	 */
	public float getRequiredJitter() {
		return requiredJitter;
	}

	/**
	 * Getter function
	 * @return requiredThroughput
	 */
	public double getRequiredThroughput() {
		return requiredThroughput;
	}

	/**
	 * Getter function
	 * @return requiredPacketLoss
	 */
	public double getRequiredPacketLoss() {
		return requiredPacketLoss;
	}

}