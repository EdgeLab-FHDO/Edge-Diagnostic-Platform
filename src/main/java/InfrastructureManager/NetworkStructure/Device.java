package InfrastructureManager.NetworkStructure;
/**
 * This class contains Device information.
 *
 * @author Shankar Lokeshwara
 */


public class Device {
	private int deviceId;
	private String deviceAddress;
	private DeviceType deviceType;
	private ComputeLimits computeLimits;
	
	/**
	* Default constructor of Device Class
	*/
    public Device() {
        this.deviceId = 0;
        this.deviceAddress = "";
        this.deviceType = new DeviceType();
        this.computeLimits = new ComputeLimits();
    }
    
	/**
	* Parameterized constructor of Device Class
	* @param deviceId 		integer		
	*/
    public Device(int deviceId) {
        this.deviceId = deviceId;
        this.deviceAddress = "";
        this.deviceType = new DeviceType();
        this.computeLimits = new ComputeLimits();
    }
    
	/**
	* Parameterized constructor of Device Class
	* @param deviceId 		integer		
	* @param deviceAddress
	* @param deviceType
	* @param computeLimits
	*/
    public Device(int deviceId,String deviceAddress, DeviceType deviceType,ComputeLimits computeLimits) {
        this.deviceId = deviceId;
        this.deviceAddress = deviceAddress;
        this.deviceType = deviceType;
        this.computeLimits = computeLimits;
    }	
	
	/**
	 * Getter function
	 * @return deviceId
	 */
	public int getDeviceId() {
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
	 * @param deviceType
	 */
	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}
	
	/**
	 * Setter function
	 * @param computeLimits
	 */
	public void setComputeLimits(ComputeLimits computeLimits) {
		this.computeLimits = computeLimits;
	}
	
	/**
	 * Setter function
	 * @param deviceAddress
	 */
	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}
    
}