package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.RawData.MasterConfigurationData;
import InfrastructureManager.ModuleManagement.Exception.ModuleNotFoundException;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.AdvantEDGE.AdvantEdgeModule;
import InfrastructureManager.Modules.Console.ConsoleModule;
import InfrastructureManager.Modules.MatchMaking.MatchMakingModule;
import InfrastructureManager.Modules.REST.RESTModule;
import InfrastructureManager.Modules.RemoteExecution.RemoteExecutionModule;
import InfrastructureManager.Modules.Scenario.ScenarioModule;
import InfrastructureManager.Modules.Utility.UtilityModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleFactory {

    private final List<ModuleConfigData> data;

    public enum ModuleType {DEFAULT, CONSOLE, UTILITY, SCENARIO, REST, ADVANTEDGE, REMOTE_EXEC, MATCH_MAKING}

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

        PlatformModule result = switch (data.getType()) {
            case CONSOLE -> new ConsoleModule();
            case UTILITY -> new UtilityModule();
            case SCENARIO -> new ScenarioModule();
            case REST -> new RESTModule();
            case ADVANTEDGE -> new AdvantEdgeModule();
            case REMOTE_EXEC -> new RemoteExecutionModule();
            case MATCH_MAKING -> new MatchMakingModule();
            default -> throw new ModuleNotFoundException("The module type for module" + data.getName() + "is not defined");
        };
        result.configure(data);
        return result;
    }
}
