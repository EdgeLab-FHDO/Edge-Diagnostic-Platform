package InfrastructureManager.Modules.NetworkStructure;
/**
 * This class contains relationship between device and location information.
 *
 * @author Shankar Lokeshwara
 */

public class DeviceLocationRelation {
	
	private Device device;
	private Location location;
	
	/**
	* Parameterized constructor of Class DeviceLocationRelation 
	* @param Device 				
	* @param Location
	*/
	public DeviceLocationRelation(Device device,Location location) {
		this.device = device;
		this.location = location;		
	}
	
	/**
	 * Getter function
	 * @return device
	 */
	public Device getDeviceLocation() {
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