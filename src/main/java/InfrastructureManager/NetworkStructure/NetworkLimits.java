package InfrastructureManager.NetworkStructure;

/**
 * This class contains relationship between NetworkLimits information.
 *
 * @author Shankar Lokeshwara
 */

public class NetworkLimits {
	private int latency;
	private double throughput;
	private double packetLoss;
	
	/**
	* Default constructor for NetworkLimits Class
	*/
	public NetworkLimits() {
		this.latency = 0;
		this.throughput = 0.0f;
		this.packetLoss = 0.0f;
	}
	
	/**
	* Parameterized constructor for NetworkLimits Class
	* @param latency 	
	* @param throughput 
	* @param packetLoss 			
	*/
	public NetworkLimits(int latency,double throughput,double packetLoss) {
		this.latency = latency;
		this.throughput = throughput;
		this.packetLoss = packetLoss;
	}
	
	/**
	 * Getter function
	 * @return latency
	 */
	public int getLatency() {
		return latency;
	}
	
	/**
	 * Getter function
	 * @return throughput
	 */
	public double getThroughput() {
		return throughput;
	}
	
	/**
	 * Getter function
	 * @return packetLoss
	 */
	public double getPacketLoss() {
		return packetLoss;
	}
	
	/**
	 * Setter function
	 * @param latency
	 */
	public void setLatency(int latency) {
		this.latency =  latency;
	}
	
	/**
	 * Setter function
	 * @param throughput
	 */
	public void setThroughput(double throughput) {
		this.throughput = throughput;
	}
	
	/**
	 * Setter function
	 * @param packetLoss
	 */
	public void setPacketLoss(double packetLoss) {
		this.packetLoss = packetLoss;
	}
	
}