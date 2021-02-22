package InfrastructureManager.Modules.RemoteExecution.RawData;

import InfrastructureManager.ModuleManagement.ModuleFactory.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Raw data representing a RemoteExecutionModule.
 *
 * To create a RE module only the name is needed.
 * Type is automatically selected as {@link ModuleType}.REMOTE_EXEC
 */
public class RemoteExecutionModuleConfigData extends ModuleConfigData {

    /**
     * Creates a new raw console module. Uses the {@link JsonProperty} annotation for extracting the
     * name while deserializing.
     * @param name Name of the module
     */
    public RemoteExecutionModuleConfigData(@JsonProperty("name") String name) {
        super(name);
        this.type = ModuleType.REMOTE_EXEC;
    }
}
