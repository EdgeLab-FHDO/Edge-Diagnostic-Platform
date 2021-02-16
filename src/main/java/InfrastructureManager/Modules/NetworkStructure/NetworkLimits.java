package InfrastructureManager.Modules.NetworkStructure;

/**
 * This class contains relationship between NetworkLimits information.
 *
 * @author Shankar Lokeshwara
 */

public class NetworkLimits {
	private float latency;
	private float jitter;
	private double throughput;
	private double packetLoss;

	/**
	 * Parameterized constructor for NetworkLimits Class
	 * @param latency
	 * @param jitter
	 * @param throughput 
	 * @param packetLoss 			
	 */
	public NetworkLimits(float latency,float jitter,double throughput,double packetLoss) {
		this.latency = latency;
		this.jitter = jitter;
		this.throughput = throughput;
		this.packetLoss = packetLoss;
	}

	/**
	 * Getter function
	 * @return latency
	 */
	public float getLatency() {
		return latency;
	}

	/**
	 * Getter function
	 * @return jitter
	 */
	public float getJitter() {
		return jitter;
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

}