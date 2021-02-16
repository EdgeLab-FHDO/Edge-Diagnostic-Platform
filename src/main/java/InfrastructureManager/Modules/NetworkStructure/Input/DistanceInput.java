package InfrastructureManager.Modules.NetworkStructure.Input;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformInput;
import InfrastructureManager.Modules.NetworkStructure.Network;
import InfrastructureManager.Modules.NetworkStructure.NetworkModuleObject;
import InfrastructureManager.Modules.NetworkStructure.SharedDistance;

public class DistanceInput extends NetworkModuleObject implements PlatformInput {

	private final SharedDistance sharedDistance;
	private String toSend;
	public DistanceInput(ImmutablePlatformModule module,String name,Network network) {
		super(module,name);
		this.sharedDistance = this.getSharedDistance();	
		this.toSend = null;
	}

	protected String waitForDistanceList() throws InterruptedException {
		return sharedDistance.getListAsBody();
	}
	@Override
	public String read() throws InterruptedException {
		toSend = "set_distance " + waitForDistanceList();
		String aux = toSend;
		toSend = "";
		return aux;
	}

	@Override
	public void response(ModuleExecutionException outputException) {}
}