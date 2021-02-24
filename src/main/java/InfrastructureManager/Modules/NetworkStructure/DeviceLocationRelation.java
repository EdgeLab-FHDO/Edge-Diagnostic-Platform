package InfrastructureManager.Modules.NetworkStructure;
/**
 * This class contains relationship between device and location information.
 *
 * @author Shankar Lokeshwara
 */

public class DeviceLocationRelation {
	private String deviceLocationRelationId;
	private Device device;
	private Location location;

	/**
	 * Parameterized constructor of Class DeviceLocationRelation 
	 *@param deviceLocationRelationId
	 * @param Device 				
	 * @param Location
	 */
	public DeviceLocationRelation(String deviceLocationRelationId,Device device,Location location) {
		this.deviceLocationRelationId=deviceLocationRelationId;
		this.device = device;
		this.device.setLocationRelationId(deviceLocationRelationId);
		this.location = location;
		this.location.setDeviceRelationId(deviceLocationRelationId);
	}
	
	/**
	 * Getter function
	 * @return location
	 */
	public String getDeviceLocationRelationId() {
		return this.deviceLocationRelationId;		
	}

	/**
	 * Getter function
	 * @return device
	 */
	public Device getDevice() {
		return this.device;		
	}

	/**
	 * Getter function
	 * @return location
	 */
	public Location getLocation() {
		return this.location;		
	}

}