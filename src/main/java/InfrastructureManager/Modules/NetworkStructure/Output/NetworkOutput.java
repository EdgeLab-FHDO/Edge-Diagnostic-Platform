package InfrastructureManager.Modules.NetworkStructure.Output;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import com.fasterxml.jackson.core.JsonProcessingException;
import InfrastructureManager.Modules.NetworkStructure.Exception.NetworkModuleException;
import InfrastructureManager.Modules.NetworkStructure.Network;

public class NetworkOutput extends PlatformOutput {
	private Network network;
	public NetworkOutput(ImmutablePlatformModule module, String name, Network network) {
		super(module,name);
		this.network = network;
	}
	@Override
	public void execute(String response) throws NetworkModuleException  {
		String[] command = response.split(" ",2);
		if (command[0].equals("loadNetwork")) { //The commands must come like "advantEdge command"
			try {
				this.network.loadNetwork(command[1]);
			}
			catch(JsonProcessingException e) {
				throw new NetworkModuleException("Error processing JSON data", e);
			}
		}
	}
}