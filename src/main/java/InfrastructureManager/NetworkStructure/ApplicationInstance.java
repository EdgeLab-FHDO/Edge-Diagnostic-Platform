package InfrastructureManager.NetworkStructure;
//States of the application
enum State {IDLE,RUNNING,COMPLETED,TERMINATED}


/**
 * This class contains application information.
 *
 * @author Shankar Lokeshwara
 */
public class ApplicationInstance {
	private ApplicationType application; //Application object
	private State applicationState; // Data member to hold the state of the application
	
	/**
	* Default constructor for class ApplicationInstance
	*/
	public ApplicationInstance() {
	this.application = new ApplicationType(0);
	this.applicationState = State.IDLE;
	}
	
	/**
	* Parameterized constructor for class ApplicationInstance
	* @param application 		application object
	*/
	public ApplicationInstance(ApplicationType application) {
		this.application = application;
		this.applicationState = State.IDLE;
	}
	/**
	* Parameterized constructor for class ApplicationInstance
	* @param applicationId 		applicationId as integer
	*/
	public ApplicationInstance(int applicationId) {
	this.application = new ApplicationType(applicationId);
	this.applicationState = State.IDLE;
	}
	
	/**
	 * Getter function
	 * @return application
	 */
	public ApplicationType getApplicationInstance() {
		return application;
	}
	
	/**
	 * Getter function
	 * @return applicationState
	 */
	public State getApplicationState() {
		return applicationState;
	}
	
	/**
	 * Setter function
	 * @param application
	 */
	
	public void setApplicationInstance(ApplicationType application) {
		this.application = application;
	}
	
	/**
	 * Setter function
	 * @param applicationState
	 */
	public void setApplicationState(State applicationState) {
		this.applicationState = applicationState;
	}
		
}