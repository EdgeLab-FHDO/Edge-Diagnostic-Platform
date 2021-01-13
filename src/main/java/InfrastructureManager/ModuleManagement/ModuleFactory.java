package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.RawData.MasterConfigurationData;
import InfrastructureManager.ModuleManagement.Exception.ModuleNotFoundException;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.AdvantEDGE.AdvantEdgeModule;
import InfrastructureManager.Modules.AdvantEDGE.RawData.AdvantEdgeModuleConfigData;
import InfrastructureManager.Modules.Scenario.RawData.ScenarioModuleConfigData;
import InfrastructureManager.Modules.Console.ConsoleModule;
import InfrastructureManager.Modules.REST.RESTModule;
import InfrastructureManager.Modules.REST.RawData.RESTModuleConfigData;
import InfrastructureManager.Modules.Scenario.ScenarioModule;
import InfrastructureManager.Modules.Utility.UtilityModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleFactory {

    private final List<ModuleConfigData> data;

    public enum ModuleType {DEFAULT, CONSOLE, UTILITY, SCENARIO, REST, ADVANTEDGE, REMOTE_EXEC}

    public ModuleFactory(MasterConfigurationData data) {
        this.data = data.getModules();
    }

    public List<PlatformModule> getModules() {
        List<PlatformModule> result = new ArrayList<>();
        for (ModuleConfigData moduleData : data) {
            try {
                result.add(create(moduleData));
            } catch (ModuleNotFoundException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private PlatformModule create(ModuleConfigData data) {
        return switch (data.getType()) {
            case DEFAULT -> throw new ModuleNotFoundException("The module type for module" + data.getName() + "is not defined");
            case CONSOLE -> new ConsoleModule(data.getName());
            case UTILITY -> new UtilityModule(data.getName());
            case SCENARIO -> {
                ScenarioModuleConfigData castedData = (ScenarioModuleConfigData) data;
                yield new ScenarioModule(castedData.getName(), castedData.getPath());
            }
            case REST -> {
                RESTModuleConfigData castedData = (RESTModuleConfigData) data;
                yield new RESTModule(castedData.getName(), castedData.getPort(),
                        castedData.getBaseURL(), castedData.getPOSTInputs(), castedData.getGETOutputs());
            }
            case ADVANTEDGE -> {
                AdvantEdgeModuleConfigData castedData = (AdvantEdgeModuleConfigData) data;
                yield new AdvantEdgeModule(castedData.getName(), castedData.getPort(), castedData.getAddress());
            }
            case REMOTE_EXEC -> null;
        };
    }
}
