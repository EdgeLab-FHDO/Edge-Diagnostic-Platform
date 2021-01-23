package InfrastructureManager.NetworkStructure;

enum State {IDLE,RUNNING,COMPLETED,TERMINATED}

public class ApplicationInstance {
	private ApplicationType application;
	private State applicationState;
	
	public ApplicationInstance(ApplicationType application) {
		this.application = application;
		this.applicationState = State.IDLE;
	}
	
	public ApplicationInstance(int applicationId) {
	this.application = new ApplicationType(applicationId);
	this.applicationState = State.IDLE;
	}
	
	public ApplicationInstance() {
	this.application = new ApplicationType(0);
	this.applicationState = State.IDLE;
	}
	
	public ApplicationType getApplicationInstancce() {
		return application;
	}
	
	public void setApplicationInstancce(ApplicationType application) {
		this.application = application;
	}
	
	public State getApplicationState() {
		return applicationState;
	}
	
	public void setApplicationState(State applicationState) {
		this.applicationState = applicationState;
	}
		
}