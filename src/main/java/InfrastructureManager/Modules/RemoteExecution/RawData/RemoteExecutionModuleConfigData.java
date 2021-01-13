package InfrastructureManager.Modules.RemoteExecution.RawData;

import InfrastructureManager.ModuleManagement.ModuleFactory.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RemoteExecutionModuleConfigData extends ModuleConfigData {

    public RemoteExecutionModuleConfigData(@JsonProperty("name") String name) {
        super(name);
        this.type = ModuleType.REMOTE_EXEC;
    }
}
