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
	
	//ToDo move it to other file with other function implementation
    //To be moved to different class later and handle exceptions 
    public void computeResourceCheck(Device device,ApplicationInstance application) {
    	ComputeRequirements lRequiredComputation = application.getApplicationInstance().getComputeSpecification();
    	if ((device.getComputeLimits().getCpuCount()>lRequiredComputation.getRequiredCpu()) || (device.getComputeLimits().getGpuCount()>lRequiredComputation.getRequiredGpu() || (device.getComputeLimits().getMemoryLimit()>lRequiredComputation.getRequiredMemory())) )
    	{
    		ComputeLimits lComputeLimits = new ComputeLimits();
    		lComputeLimits.setCpuCount(device.getComputeLimits().getCpuCount()- lRequiredComputation.getRequiredCpu());
    		lComputeLimits.setGpuCount(device.getComputeLimits().getGpuCount()- lRequiredComputation.getRequiredGpu());
    		lComputeLimits.setCpuCount(device.getComputeLimits().getMemoryLimit()- lRequiredComputation.getRequiredMemory());
    		device.setComputeLimits(lComputeLimits);
    	}
    	else
    	{
    		//ToDo - Exception handling
    	}
    }
	
		
}