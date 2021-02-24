package InfrastructureManager.Modules.NetworkStructure;

/**
 * This class contains relationship between application and device information.
 *
 * @author Shankar Lokeshwara
 */

public class ApplicationInstanceDeviceRelation {
	private String applicationDeviceRelationId;
	private ApplicationInstance application;
	private Device device;

	/**
	 * Parameterized constructor for class ApplicationInstanceDeviceRelation
	 * @param applicationDeviceRelationId
	 * @param application 	application object
	 * @param device 		device object
	 */
	public ApplicationInstanceDeviceRelation(String applicationDeviceRelationId,ApplicationInstance application,Device device) {
		this.applicationDeviceRelationId = applicationDeviceRelationId;
		this.application = application;
		this.application.setDeviceRelationId(applicationDeviceRelationId);
		this.device = device;
		this.device.setApplicationRelationId(applicationDeviceRelationId);
	}

	/**
	 * Getter function
	 * @return applicationDeviceRelationId
	 */

	public String getApplicationDeviceRelationId() {
		return this.applicationDeviceRelationId;		
	}
	
	/**
	 * Getter function
	 * @return application
	 */

	public ApplicationInstance getApplication() {
		return this.application;		
	}

	/**
	 * Getter function
	 * @return device
	 */
	public Device getApplicationDevice() {
		return this.device;		
	}

}