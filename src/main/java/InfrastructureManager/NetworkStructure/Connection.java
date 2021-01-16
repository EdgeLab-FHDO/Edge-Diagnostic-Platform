package InfrastructureManager.NetworkStructure;
public class Connection {
	private int connectionId;
	private NetworkLimits networkCapacity;
	
	public int getConnectionId() {
		return connectionId;
	}
	
	public NetworkLimits getNetworkCapacity() {
		return networkCapacity;
	}
	
	public void setNetworkCapacity(NetworkLimits networkCapacity) {
		this.networkCapacity =  networkCapacity;
	}
    
    public Connection(int connectionId) {
        this.connectionId = connectionId;
        this.networkCapacity = new NetworkLimits();
    }

    public Connection(int connectionId, NetworkLimits networkCapacity) {
        this.connectionId = connectionId;
        this.networkCapacity = networkCapacity;
    }
    
}