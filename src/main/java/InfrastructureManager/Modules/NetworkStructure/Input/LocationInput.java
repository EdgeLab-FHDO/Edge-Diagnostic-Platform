package InfrastructureManager.Modules.NetworkStructure.Input;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformInput;
import InfrastructureManager.Modules.NetworkStructure.SharedLocation;
import InfrastructureManager.Modules.NetworkStructure.Network;
import InfrastructureManager.Modules.NetworkStructure.NetworkModuleObject;

public class LocationInput extends NetworkModuleObject implements PlatformInput {

	private final SharedLocation sharedLocation;
	private String toSend;
	public LocationInput(ImmutablePlatformModule module,String name,Network network) {
		super(module,name);
		this.sharedLocation = this.getSharedLocation();	
		this.toSend = null;
	}

	protected String waitForLocationList() throws InterruptedException {
		return sharedLocation.getListAsBody();
	}
	@Override
	public String read() throws InterruptedException {
		toSend = "set_location " + waitForLocationList();
		String aux = toSend;
		toSend = "";
		return aux;
	}

	@Override
	public void response(ModuleExecutionException outputException) {}
}