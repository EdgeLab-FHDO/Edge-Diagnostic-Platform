package InfrastructureManager.Modules.Console.RawData;

import InfrastructureManager.ModuleManagement.ModuleFactory.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Raw data representing a ConsoleModule.
 *
 * To create a console module only the name is needed.
 * Type is automatically selected as {@link ModuleType}.CONSOLE
 */
public class ConsoleModuleConfigData extends ModuleConfigData {

    /**
     * Creates a new raw console module. Uses the {@link JsonProperty} annotation for extracting the
     * name while deserializing.
     * @param name Name of the module
     */
    public ConsoleModuleConfigData(@JsonProperty("name") String name) {
        super(name);
        this.type = ModuleType.CONSOLE;
    }
}
