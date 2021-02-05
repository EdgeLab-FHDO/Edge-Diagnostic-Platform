package InfrastructureManager.Modules.NetworkStructure.Output;

import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.ModuleOutput;
import com.fasterxml.jackson.core.JsonProcessingException;
import InfrastructureManager.Modules.NetworkStructure.Exception.NetworkModuleException;
import InfrastructureManager.Modules.NetworkStructure.Network;

public class LocationOutput extends ModuleOutput{
	public LocationOutput(PlatformModule module, String name) {
		super(module,name);
	}
	@Override
	public void execute(String response) throws NetworkModuleException  {

	}
}