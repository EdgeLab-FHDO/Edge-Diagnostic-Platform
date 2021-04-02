package InfrastructureManager.Modules.CustomConnector;

import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.CustomConnector.RawData.CustomConnectorModuleConfigData;

public class CustomConnectorModule extends PlatformModule {

    /**
     * Create a new Custom Connector module. The module created is not initialized
     */
    public CustomConnectorModule() {
        super();
    }

    @Override
    public void configure(ModuleConfigData data) {
        CustomConnectorModuleConfigData castedData = (CustomConnectorModuleConfigData) data;
        String name = castedData.getName();
        setName(name);
        setInputs(new CustomConnectorInput(this, name + ".in"));
        setOutputs(new CustomConnectorOutput(this, name + ".out"));
    }
}