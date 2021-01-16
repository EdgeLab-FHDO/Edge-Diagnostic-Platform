package InfrastructureManager.NetworkStructure;

public class ApplicationInstanceDeviceRelation {
	
    //To be moved to different class later and handle exceptions 
    public void computeResourceCheck(Device device,ApplicationInstance application) {
    	ComputeRequirements lRequiredComputation = application.getApplicationInstancce().getComputeSpecification();
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