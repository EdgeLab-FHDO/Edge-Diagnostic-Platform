package InfrastructureManager.NetworkStructure;
/**
 * This class contains Device information.
 *
 * @author Shankar Lokeshwara
 */


public class Device {
	private String deviceId;
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
}