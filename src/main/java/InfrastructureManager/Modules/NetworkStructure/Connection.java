package InfrastructureManager.Modules.NetworkStructure;

public class Connection {
	private String connectionId;
	private NetworkLimits networkCapacity;
	
   
	public Connection(String connectionId, NetworkLimits networkCapacity) {
        this.connectionId = connectionId;
        this.networkCapacity = networkCapacity;
    }
    
	public String getConnectionId() {
		return connectionId;
	}
	
	public NetworkLimits getNetworkCapacity() {
		return networkCapacity;
	}
	
	public void setNetworkCapacity(NetworkLimits networkCapacity) {
		this.networkCapacity =  networkCapacity;
	}
    
}