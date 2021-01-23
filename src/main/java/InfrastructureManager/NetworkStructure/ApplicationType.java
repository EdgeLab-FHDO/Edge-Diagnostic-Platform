package InfrastructureManager.NetworkStructure;

/**
 * This class contains application requirements.
 *
 * @author Shankar Lokeshwara
 */
public class ApplicationType {
	private int applicationId; // Member variable to hold application ID.
	private ComputeRequirements computeSpecification;
	private NetworkRequirements networkSpecification;
	
	/**
	* Default constructor for class ApplicationType
	*/
	public ApplicationType() {
		this.applicationId = 0;
		this.computeSpecification = new ComputeRequirements();
		this.networkSpecification = new NetworkRequirements();
	}
	
	/**
	* Parameterized constructor for class ApplicationType
	* @param applicationId 		applicationId as integer
	*/
	public ApplicationType(int applicationId) {
		this.applicationId = applicationId;
		this.computeSpecification = new ComputeRequirements();
		this.networkSpecification = new NetworkRequirements();
	}
	
	/**
	* Parameterized constructor for class ApplicationType
	* @param applicationId 		applicationId as integer
	* @param computeSpecification 		
	* @param networkSpecification 		
	*/
	public ApplicationType(int applicationId,ComputeRequirements computeSpecification,NetworkRequirements networkSpecification) {
		this.applicationId = applicationId;
		this.computeSpecification = computeSpecification;
		this.networkSpecification = networkSpecification;
	}
	
	/**
	 * Getter function
	 * @return applicationId
	 */
	public int getApplicationId() {
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
	
	/**
	 * Setter function
	 * @param computeSpecification
	 */
	public void setComputeSpecification(ComputeRequirements computeSpecification) {
		this.computeSpecification =  computeSpecification;
	}
	
	/**
	 * Setter function
	 * @param networkSpecification
	 */
	public void setNetworkSpecification (NetworkRequirements networkSpecification) {
		this.networkSpecification =  networkSpecification;
	}
		
}