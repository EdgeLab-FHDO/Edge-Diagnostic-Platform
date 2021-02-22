package InfrastructureManager.Modules.NetworkStructure.Input;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformInput;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedLocation;
import InfrastructureManager.Modules.NetworkStructure.Network;
import InfrastructureManager.Modules.NetworkStructure.NetworkModuleObject;

public class LocationInput extends NetworkModuleObject implements PlatformInput {

	private final SharedLocation sharedLocation;
	private String toSend;
	public LocationInput(ImmutablePlatformModule module,String name) {
		super(module,name);
		this.sharedLocation = this.getSharedLocation();	
		this.toSend = null;
	}

	@Override
	public String read() throws InterruptedException {
		String aux = "";
		try {
			toSend = "location" + sharedLocation.getLocationAsJsonBody();
			aux=toSend;
		}
		catch (JsonProcessingException e) {
			aux= null;
        }

		return aux;
	}

	@Override
	public void response(ModuleExecutionException outputException) {}
}