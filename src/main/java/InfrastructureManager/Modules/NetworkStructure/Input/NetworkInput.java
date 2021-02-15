package InfrastructureManager.Modules.NetworkStructure.Input;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformInput;

import InfrastructureManager.Modules.NetworkStructure.Network;
import InfrastructureManager.Modules.NetworkStructure.NetworkModuleObject;
import InfrastructureManager.PlatformObject;

public class NetworkInput extends NetworkModuleObject implements PlatformInput {
	
	private final Network network;
	
	public NetworkInput(ImmutablePlatformModule module, String name, Network network) {
		super(module,name);
		this.network = network;
	}
    @Override
    public String read() throws InterruptedException  {
        String aux = this.network.saveNetwork();
        return aux;
    }

    @Override
    public void response(ModuleExecutionException outputException) {}
}