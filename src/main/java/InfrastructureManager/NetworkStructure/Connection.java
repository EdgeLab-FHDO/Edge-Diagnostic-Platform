package InfrastructureManager.NetworkStructure;
/**
 * This class contains connection information.
 *
 * @author Shankar Lokeshwara
 */

public class Connection {
	private int connectionId;
	private NetworkLimits networkCapacity;
	
	/**
	* Default constructor for class Connection
	*/
    public Connection() {
        this.connectionId = 0;
        this.networkCapacity = new NetworkLimits();
    }
	
	/**
	* Parameterized constructor for class Connection
	* @param connectionId 		integer		
	*/
    public Connection(int connectionId) {
        this.connectionId = connectionId;
        this.networkCapacity = new NetworkLimits();
    }
    
	/**
	* Parameterized constructor for class Connection
	* @param connectionId 		integer
	* @param networkCapacity 		
	*/
    public Connection(int connectionId, NetworkLimits networkCapacity) {
        this.connectionId = connectionId;
        this.networkCapacity = networkCapacity;
    }
    
	/**
	 * Getter function
	 * @return connectionId
	 */
	public int getConnectionId() {
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