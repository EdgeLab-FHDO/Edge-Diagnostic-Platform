package InfrastructureManager.NetworkStructure;

/**
 * This class contains relationship between application and device information.
 *
 * @author Shankar Lokeshwara
 */

public class ApplicationInstanceDeviceRelation {
	private ApplicationInstance application;
	private Device device;
	
	/**
	* Parameterized constructor for class ApplicationInstanceDeviceRelation
	* @param application 	application object
	* @param device 		device object
	*/
	public ApplicationInstanceDeviceRelation(ApplicationInstance application,Device device) {
		this.application = application;
		this.device = device;
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
	public Device getDevice() {
		return this.device;		
	}
		
}