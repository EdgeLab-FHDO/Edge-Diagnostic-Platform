package InfrastructureManager.Modules.NetworkStructure;

public class ApplicationType {
	private String applicationId; // Member variable to hold application ID.
	private ComputeRequirements computeSpecification;
	private NetworkRequirements networkSpecification;
	
	
	public ApplicationType(String applicationId,ComputeRequirements computeSpecification,NetworkRequirements networkSpecification) {
		this.applicationId = applicationId;
		this.computeSpecification = computeSpecification;
		this.networkSpecification = networkSpecification;
	}
	
	public String getApplicationId() {
		return applicationId;
	}
	
	public ComputeRequirements getComputeSpecification() {
		return computeSpecification;
	}
	
	public NetworkRequirements getNetworkSpecification() {
		return networkSpecification;
	}
		
}