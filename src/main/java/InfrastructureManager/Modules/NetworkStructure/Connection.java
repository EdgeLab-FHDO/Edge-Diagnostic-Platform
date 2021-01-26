package InfrastructureManager.Modules.NetworkStructure;
/**
 * This class contains connection information.
 *
 * @author Shankar Lokeshwara
 */

public class Connection {
	private String connectionId;
	private NetworkLimits networkCapacity;
	
   
	/**
	* Parameterized constructor for class Connection
	* @param connectionId 		integer
	* @param networkCapacity 		
	*/
    public Connection(String connectionId, NetworkLimits networkCapacity) {
        this.connectionId = connectionId;
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
	 * @return networkCapacity
	 */
	public NetworkLimits getNetworkCapacity() {
		return networkCapacity;
	}
	
	/**
	 * Setter function
	 * @param networkCapacity
	 */
	public void setNetworkCapacity(NetworkLimits networkCapacity) {
		this.networkCapacity =  networkCapacity;
	}
    
}