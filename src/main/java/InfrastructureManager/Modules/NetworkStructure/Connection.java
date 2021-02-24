package InfrastructureManager.Modules.NetworkStructure;
/**
 * This class contains connection information.
 *
 * @author Shankar Lokeshwara
 */

public class Connection {
	private String connectionId;
	private String deviceRelationId;
	private String locationRelationId;
	private NetworkLimits networkCapacity;


	/**
	 * Parameterized constructor for class Connection
	 * @param connectionId 		integer
	 * @param networkCapacity 		
	 */
	public Connection(String connectionId,NetworkLimits networkCapacity) {
		this.connectionId = connectionId;
		this.deviceRelationId = "";
		this.locationRelationId = "";
		this.networkCapacity = networkCapacity;
	}

	/**
	 * Getter function
	 * @return connectionId
	 */
	public String getConnectionId() {
		return connectionId;
	}

	/**
	 * Getter function
	 * @return deviceRelationId
	 */
	public String getDeviceRelationId() {
		return deviceRelationId;
	}

	/**
	 * Getter function
	 * @return locationRelationId
	 */
	public String getLocationRelationId() {
		return locationRelationId;
	}

	
	
	/**
	 * Getter function
	 * @return networkCapacity
	 */
	public NetworkLimits getNetworkCapacity() {
		return networkCapacity;
	}

	/**
	 * Setter function
	 * @param deviceRelationId
	 */
	public void setNetworkCapacity(String deviceRelationId) {
		this.deviceRelationId =  deviceRelationId;
	}
	
	/**
	 * Setter function
	 * @param locationRelationId
	 */
	public void setLocationRelationId(String locationRelationId) {
		this.locationRelationId =  locationRelationId;
	}
	
	/**
	 * Setter function
	 * @param networkCapacity
	 */
	public void setNetworkCapacity(NetworkLimits networkCapacity) {
		this.networkCapacity =  networkCapacity;
	}

}