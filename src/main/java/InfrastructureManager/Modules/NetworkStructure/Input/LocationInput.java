package InfrastructureManager.Modules.NetworkStructure.Input;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.Modules.NetworkStructure.Exception.NetworkModuleException;

import com.fasterxml.jackson.core.JsonProcessingException;

import InfrastructureManager.ModuleManagement.ModuleInput;
import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.Modules.NetworkStructure.Network;

public class LocationInput extends ModuleInput {
	
	
	public LocationInput(PlatformModule module,String name,Network network) {
		super(module,name);
	}
    @Override
    public String read() throws InterruptedException  {
        String aux = " ";
        return aux;
    }

    @Override
    public void response(ModuleExecutionException outputException) {}
}