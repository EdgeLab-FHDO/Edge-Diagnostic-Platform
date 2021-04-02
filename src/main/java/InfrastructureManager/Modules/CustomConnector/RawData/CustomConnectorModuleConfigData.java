package InfrastructureManager.Modules.CustomConnector.RawData;

import InfrastructureManager.ModuleManagement.ModuleFactory.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomConnectorModuleConfigData extends ModuleConfigData {

    public CustomConnectorModuleConfigData(@JsonProperty("name") String name)
    {
        super(name);
        this.type = ModuleType.CUSTOM_CONNECTOR;
    }
}