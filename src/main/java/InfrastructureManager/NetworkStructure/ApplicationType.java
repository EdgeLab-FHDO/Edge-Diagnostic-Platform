package InfrastructureManager.NetworkStructure;

public class ApplicationType {
	private int applicationId;
	private ComputeRequirements computeSpecification;
	private NetworkRequirements networkSpecification;
	
	public int getApplicationId() {
		return applicationId;
	}
	
	public ComputeRequirements getComputeSpecification() {
		return computeSpecification;
	}
	
	public void setComputeSpecification(ComputeRequirements computeSpecification) {
		this.computeSpecification =  computeSpecification;
	}
	
	public NetworkRequirements getNetworkSpecification() {
		return networkSpecification;
	}
	
	public void setNetworkSpecification (NetworkRequirements networkSpecification) {
		this.networkSpecification =  networkSpecification;
	}
	
	public ApplicationType() {
		this.applicationId = 0;
		this.computeSpecification = new ComputeRequirements();
		this.networkSpecification = new NetworkRequirements();
	}
	
	public ApplicationType(int applicationId) {
		this.applicationId = applicationId;
		this.computeSpecification = new ComputeRequirements();
		this.networkSpecification = new NetworkRequirements();
	}
	
	public ApplicationType(int applicationId,ComputeRequirements computeSpecification,NetworkRequirements networkSpecification) {
		this.applicationId = applicationId;
		this.computeSpecification = computeSpecification;
		this.networkSpecification = networkSpecification;
	}
		
}