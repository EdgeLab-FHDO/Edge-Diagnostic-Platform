package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleNotDefinedException;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.AdvantEDGE.AdvantEdgeModule;
import InfrastructureManager.Modules.Console.ConsoleModule;
import InfrastructureManager.Modules.MatchMaking.MatchMakingModule;
import InfrastructureManager.Modules.REST.RESTModule;
import InfrastructureManager.Modules.RemoteExecution.RemoteExecutionModule;
import InfrastructureManager.Modules.Scenario.ScenarioModule;
import InfrastructureManager.Modules.Utility.UtilityModule;

public class ModuleFactory {

    public enum ModuleType {DEFAULT, CONSOLE, UTILITY, SCENARIO, REST, ADVANTEDGE, REMOTE_EXEC, MATCH_MAKING}

    public PlatformModule create(ModuleConfigData data) throws ModuleNotDefinedException {

        PlatformModule result = switch (data.getType()) {
            case CONSOLE -> new ConsoleModule();
            case UTILITY -> new UtilityModule();
            case SCENARIO -> new ScenarioModule();
            case REST -> new RESTModule();
            case ADVANTEDGE -> new AdvantEdgeModule();
            case REMOTE_EXEC -> new RemoteExecutionModule();
            case MATCH_MAKING -> new MatchMakingModule();
            default -> throw new ModuleNotDefinedException("The module type for module" + data.getName() + "is not defined");
        };
        result.configure(data);
        return result;
    }
}
