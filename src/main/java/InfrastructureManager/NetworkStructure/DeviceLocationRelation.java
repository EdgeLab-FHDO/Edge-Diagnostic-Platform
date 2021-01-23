package InfrastructureManager.NetworkStructure;
public class DeviceLocationRelation {
	
	private Device device;
	private Location location;
	
	public DeviceLocationRelation(int deviceId,int locationId) {
		this.device = new Device(deviceId);
		this.location = new Location(locationId);		
	}
	
	public DeviceLocationRelation() {
		this.device = new Device(0);
		this.location = new Location(0);		
	}
	
	public Device getDevice() {
		return this.device;		
	}
	
	public Location getLocation() {
		return this.location;		
	}
	

	
}