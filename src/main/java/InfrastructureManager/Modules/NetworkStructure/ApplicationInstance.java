package InfrastructureManager.Modules.NetworkStructure;
//States of the application

/**
 * This class contains application information.
 *
 * @author Shankar Lokeshwara
 */
public class ApplicationInstance {
	public enum State {RUNNING,COMPLETED,SUSPENDED,PAUSED}
	private ApplicationType application; 
	private State applicationState; 


	/**
	 * Parameterized constructor for class ApplicationInstance
	 * @param application 		application object
	 */
	public ApplicationInstance(ApplicationType application, State initialState) {
		this.application = application;
		this.applicationState = initialState;
	}

	/**
	 * Getter function
	 * @return application
	 */
	public ApplicationType getApplicationType() {
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
	 * @param applicationState
	 */
	public void setApplicationState(State applicationState) {
		this.applicationState = applicationState;
	}

}