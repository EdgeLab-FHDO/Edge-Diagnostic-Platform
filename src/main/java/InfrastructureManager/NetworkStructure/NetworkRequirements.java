package InfrastructureManager.NetworkStructure;

/**
 * This class contains NetworkRequirements .
 *
 * @author Shankar Lokeshwara
 */

public class NetworkRequirements {
	private int requiredLatency;
	private double requiredThroughput;
	private double requiredPacketLoss;
	
	/**
	* Default constructor for NetworkRequirements Class
	*/
	public NetworkRequirements() {
		this.requiredLatency = 0;
		this.requiredThroughput = 0.0f;
		this.requiredPacketLoss = 0.0f;
	}
	
	/**
	* Parameterized constructor for NetworkRequirements Class
	* @param requiredLatency 	
	* @param requiredThroughput 
	* @param requiredPacketLoss 			
	*/
	public NetworkRequirements(int requiredLatency,double requiredThroughput,double requiredPacketLoss) {
		this.requiredLatency = requiredLatency;
		this.requiredThroughput = requiredThroughput;
		this.requiredPacketLoss = requiredPacketLoss;
	}
	
	/**
	 * Getter function
	 * @return requiredLatency
	 */
	public int getRequiredLatency() {
		return requiredLatency;
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
	
	/**
	 * Setter function
	 * @param requiredLatency
	 */
	public void setRequiredLatency(int requiredLatency) {
		this.requiredLatency =  requiredLatency;
	}
	
	/**
	 * Setter function
	 * @param requiredThroughput
	 */
	public void setRequiredThroughput(double requiredThroughput) {
		this.requiredThroughput = requiredThroughput;
	}
	
	/**
	 * Setter function
	 * @param requiredPacketLoss
	 */
	public void setRequiredPacketLoss(double requiredPacketLoss) {
		this.requiredPacketLoss = requiredPacketLoss;
	}
	
}