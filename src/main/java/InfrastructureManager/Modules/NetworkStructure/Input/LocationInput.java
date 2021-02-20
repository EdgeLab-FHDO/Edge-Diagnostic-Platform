package InfrastructureManager.Modules.NetworkStructure.Input;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;

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

	@Override
	public String read() {
		String aux = "";
		try {
			toSend = "location" + sharedLocation.getLocationAsJsonBody();
			aux=toSend;
		}
		catch (JsonProcessingException e) {
            e.printStackTrace();
        }
		catch (InterruptedException e) {
            e.printStackTrace();
        }
		return aux;
	}

	@Override
	public void response(ModuleExecutionException outputException) {}
}