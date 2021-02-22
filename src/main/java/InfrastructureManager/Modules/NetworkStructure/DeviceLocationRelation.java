package InfrastructureManager.Modules.NetworkStructure;

public class DeviceLocationRelation {
	
	private Device device;
	private Location location;
	
	public DeviceLocationRelation(Device device,Location location) {
		this.device = device;
		this.location = location;		
	}
	
	public Device getDeviceLocation() {
		return this.device;		
	}
	
	public Location getLocation() {
		return this.location;		
	}
	
}