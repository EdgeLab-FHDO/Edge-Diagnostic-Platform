package InfrastructureManager.NetworkStructure;
public class Device {
	private int deviceId;
	private String deviceAddress;
	private DeviceType deviceType;
	private ComputeLimits computeLimits;
	
	public int getDeviceId() {
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
	
	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}
	
	public void setComputeLimits(ComputeLimits computeLimits) {
		this.computeLimits = computeLimits;
	}
	
	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}
	
	
	
    public Device() {
        this.deviceId = 0;
        this.deviceAddress = "";
        this.deviceType = new DeviceType();
        this.computeLimits = new ComputeLimits();
    }
	
    public Device(int deviceId,String deviceAddress, DeviceType deviceType,ComputeLimits computeLimits) {
        this.deviceId = deviceId;
        this.deviceAddress = deviceAddress;
        this.deviceType = deviceType;
        this.computeLimits = computeLimits;
    }
	
    public Device(int deviceId) {
        this.deviceId = deviceId;
        this.deviceAddress = "";
        this.deviceType = new DeviceType();
        this.computeLimits = new ComputeLimits();
    }
    

    
    
    
}