package InfrastructureManager.Modules.NetworkStructure.Input;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformInput;
import InfrastructureManager.Modules.NetworkStructure.Network;
import InfrastructureManager.Modules.NetworkStructure.NetworkModuleObject;
import InfrastructureManager.Modules.NetworkStructure.Shared.SharedDistance;

public class DistanceInput extends NetworkModuleObject implements PlatformInput {

	private final SharedDistance sharedDistance;
	private String toSend;
	public DistanceInput(ImmutablePlatformModule module,String name) {
		super(module,name);
		this.sharedDistance = this.getSharedDistance();	
		this.toSend = null;
	}

	@Override
	public String read() throws InterruptedException {
		return "distance " + this.sharedDistance.getdistanceAsString();
	}

	@Override
	public void response(ModuleExecutionException outputException) {}
}