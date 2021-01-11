package InfrastructureManager.Modules.Console.RawData;

import InfrastructureManager.ModuleManagement.ModuleFactory.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsoleModuleConfigData extends ModuleConfigData {

    public ConsoleModuleConfigData(@JsonProperty("name") String name) {
        super(name);
        this.type = ModuleType.CONSOLE;
    }
}
