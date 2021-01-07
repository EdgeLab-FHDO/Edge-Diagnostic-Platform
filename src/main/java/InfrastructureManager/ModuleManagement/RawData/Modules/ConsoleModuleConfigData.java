package InfrastructureManager.ModuleManagement.RawData.Modules;

import InfrastructureManager.ModuleManagement.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;

public class ConsoleModuleConfigData extends ModuleConfigData {

    public ConsoleModuleConfigData(String name) {
        super(name);
        this.type = ModuleType.CONSOLE;
    }
}
