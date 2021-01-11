package InfrastructureManager.AdvantEdge.NetworkStructure;


public class Device {
	private int deviceId = 0;
	private String deviceAddress = "";
	private DeviceType deviceType;
	private ComputeLimits limits;
	
	public int getDeviceId() {
		return deviceId;
	}
	
	public String getDeviceAddress() {
		return deviceAddress;
	}
	
	public setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	
	public setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}
	
}