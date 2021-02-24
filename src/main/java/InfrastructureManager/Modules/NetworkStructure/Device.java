package InfrastructureManager.Modules.NetworkStructure;
/**
 * This class contains Device information.
 *
 * @author Shankar Lokeshwara
 */


public class Device {
	private String deviceId;
	private String applicationRelationId;
	private String locationRelationId;
	private String deviceAddress;
	private DeviceType deviceType;
	private ComputeLimits computeLimits;


	/**
	 * Parameterized constructor of Device Class
	 * @param deviceId 		integer		
	 * @param deviceAddress
	 * @param deviceType
	 * @param computeLimits
	 */
	public Device(String deviceId,String deviceAddress, DeviceType deviceType,ComputeLimits computeLimits) {
		this.deviceId = deviceId;
		this.applicationRelationId = "";
		this.locationRelationId = "";
		this.deviceAddress = deviceAddress;
		this.deviceType = deviceType;
		this.computeLimits = computeLimits;
	}	

	/**
	 * Getter function
	 * @return deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}
	
	/**
	 * Getter function
	 * @return applicationRelationId
	 */
	public String getApplicationRelationId() {
		return applicationRelationId;
	}
	
	
	/**
	 * Getter function
	 * @return locationRelationId
	 */
	public String getLocationRelationId() {
		return locationRelationId;
	}

	/**
	 * Getter function
	 * @return deviceAddress
	 */

	public String getDeviceAddress() {
		return deviceAddress;
	}

	/**
	 * Getter function
	 * @return deviceType
	 */
	public DeviceType getDeviceType() {
		return deviceType;
	}

	/**
	 * Getter function
	 * @return computeLimits
	 */
	public ComputeLimits getComputeLimits() {
		return computeLimits;
	}

	/**
	 * Setter function
	 * @param deviceAddress
	 */
	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

	/**
	 * Setter function
	 * @param deviceAddress
	 */
	public void setComputeLimits(ComputeLimits computeLimits) {
		this.computeLimits = computeLimits;
	}
	
	/**
	 * Setter function
	 * @param applicationRelationId
	 */
	public void setApplicationRelationId(String applicationRelationId) {
		this.applicationRelationId = applicationRelationId;
	}
	
	/**
	 * Setter function
	 * @param locationRelationId
	 */
	public void setLocationRelationId(String locationRelationId) {
		this.locationRelationId = locationRelationId;
	}
}