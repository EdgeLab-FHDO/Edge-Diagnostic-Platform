package InfrastructureManager.Modules.NetworkStructure;


public class Device {
	private String deviceId;
	private String deviceAddress;
	private DeviceType deviceType;
	private ComputeLimits computeLimits;
	
   
	public Device(String deviceId,String deviceAddress, DeviceType deviceType,ComputeLimits computeLimits) {
        this.deviceId = deviceId;
        this.deviceAddress = deviceAddress;
        this.deviceType = deviceType;
        this.computeLimits = computeLimits;
    }	
	
	public String getDeviceId() {
		return deviceId;
	}
	
	public String getDeviceAddress() {
		return deviceAddress;
	}
	
	public DeviceType getDeviceType() {
		return deviceType;
	}
	
	public ComputeLimits getComputeLimits() {
		return computeLimits;
	}
	
	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}
	
	public void setComputeLimits(ComputeLimits computeLimits) {
		this.computeLimits = computeLimits;
	}
}