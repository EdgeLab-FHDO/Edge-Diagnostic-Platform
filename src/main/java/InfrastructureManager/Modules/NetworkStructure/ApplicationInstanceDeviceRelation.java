package InfrastructureManager.Modules.NetworkStructure;

public class ApplicationInstanceDeviceRelation {
	private ApplicationInstance application;
	private Device device;
	
	public ApplicationInstanceDeviceRelation(ApplicationInstance application,Device device) {
		this.application = application;
		this.device = device;
	}
	
	public ApplicationInstance getApplication() {
		return this.application;		
	}
	
	public Device getDevice() {
		return this.device;		
	}
		
}