package InfrastructureManager.NetworkStructure;
public class DeviceLocationRelation {
	
	public Device device;
	public Location location;
	
	public DeviceLocationRelation(int deviceId,int locationId) {
		this.device = new Device(deviceId);
		this.location = new Location(locationId);		
	}
	
	public DeviceLocationRelation() {
		this.device = new Device(0);
		this.location = new Location(0);		
	}
	
}