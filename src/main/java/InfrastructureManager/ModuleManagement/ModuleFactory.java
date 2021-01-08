package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.RawData.MasterConfigurationData;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.ModuleManagement.RawData.Modules.ScenarioModuleConfigData;
import InfrastructureManager.Modules.Console.ConsoleModule;
import InfrastructureManager.Modules.Scenario.ScenarioModule;
import InfrastructureManager.Modules.Utility.UtilityModule;
import InfrastructureManager.Scenario;
import com.fasterxml.jackson.databind.ObjectMapper;

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
            case UTILITY -> new UtilityModule(data.getName());
            case SCENARIO -> {
                ScenarioModuleConfigData castedData = (ScenarioModuleConfigData) data;
                yield new ScenarioModule(castedData.getName(), castedData.getPath());
            }
        };
    }
}
