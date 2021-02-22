package InfrastructureManager.Modules.NetworkStructure;
//States of the application

public class ApplicationInstance {
	enum State {RUNNING,COMPLETED,SUSPENDED,PAUSED}
	private ApplicationType application; 
	private State applicationState; 
	

	public ApplicationInstance(ApplicationType application, State initialState) {
		this.application = application;
		this.applicationState = initialState;
	}
	
	public ApplicationType getApplicationType() {
		return application;
	}
	
	public State getApplicationState() {
		return applicationState;
	}
		
	public void setApplicationState(State applicationState) {
		this.applicationState = applicationState;
	}
		
}