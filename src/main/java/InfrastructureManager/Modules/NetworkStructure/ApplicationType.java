package InfrastructureManager.Modules.NetworkStructure;

/**
 * This class contains application requirements.
 *
 * @author Shankar Lokeshwara
 */
public class ApplicationType {
	private String applicationId; // Member variable to hold application ID.
	private ComputeRequirements computeSpecification;
	private NetworkRequirements networkSpecification;


	/**
	 * Parameterized constructor for class ApplicationType
	 * @param applicationId 		applicationId as integer
	 * @param computeSpecification 		
	 * @param networkSpecification 		
	 */
	public ApplicationType(String applicationId,ComputeRequirements computeSpecification,NetworkRequirements networkSpecification) {
		this.applicationId = applicationId;
		this.computeSpecification = computeSpecification;
		this.networkSpecification = networkSpecification;
	}

	/**
	 * Getter function
	 * @return applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * Getter function
	 * @return computeSpecification
	 */
	public ComputeRequirements getComputeSpecification() {
		return computeSpecification;
	}

	/**
	 * Getter function
	 * @return networkSpecification
	 */
	public NetworkRequirements getNetworkSpecification() {
		return networkSpecification;
	}

}