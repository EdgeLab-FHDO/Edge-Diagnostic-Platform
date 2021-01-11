package InfrastructureManager.AdvantEdge.NetworkStructure;

public class Connection {
	private int connectionId;
	private Location sourceLocation;
	private Location destinationLocation;
	private NetworkLimits networkCapacity;
	
	public int getConnectionId() {
		return connectionId;
	}
	
	public setConnectionId(int connectionId) {
		this.connectionId =  connectionId;
	}
	
	
}