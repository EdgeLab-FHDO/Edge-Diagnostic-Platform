package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.RawData.MasterConfigurationData;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.Console.ConsoleModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleFactory {

    private final List<ModuleConfigData> data;

    public ModuleFactory(MasterConfigurationData data) {
        this.data = data.getModules();
    }

    public List<PlatformModule> getModules() {
        List<PlatformModule> result = new ArrayList<>();
        for (ModuleConfigData moduleData : data) {
            result.add(create(moduleData));
        }
        return result;
    }

    private PlatformModule create(ModuleConfigData data) {
        return switch (data.getType()) {
            case DEFAULT -> null;
            case CONSOLE -> new ConsoleModule(data.getName());
        };
    }
}
