package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleNotDefinedException;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.AdvantEDGE.AdvantEdgeModule;
import InfrastructureManager.Modules.Console.ConsoleModule;
import InfrastructureManager.Modules.MatchMaking.Naive.MatchMakingNaiveModule;
import InfrastructureManager.Modules.MatchMaking.Random.MatchMakingRandomModule;
import InfrastructureManager.Modules.MatchMaking.ScoreBased.MatchMakingScoreBasedModule;
import InfrastructureManager.Modules.NetworkStructure.NetworkModule;
import InfrastructureManager.Modules.REST.RESTModule;
import InfrastructureManager.Modules.RemoteExecution.RemoteExecutionModule;
import InfrastructureManager.Modules.Scenario.ScenarioModule;
import InfrastructureManager.Modules.Utility.UtilityModule;

/**
 * Factory for {@link PlatformModule} objects.
 *
 * It defines the valid module types and based on the data, creates and configures the different modules
 * for the platform.
 */
public class ModuleFactory {

    /**
     * Enum representing the valid module types that the platform supports.
     *
     * Based on a value of this type present in the raw module classes, the factory is able to create and
     * configure the appropriate module.
     *
     */
    public enum ModuleType {DEFAULT, CONSOLE, UTILITY, SCENARIO, REST, ADVANTEDGE,
        REMOTE_EXEC, MATCH_MAKING_RANDOM, MATCH_MAKING_NAIVE, MATCH_MAKING_SCORE_BASED,
        NETWORK_STRUCTURE}

    /**
     * Create and configure a {@link PlatformModule} based on raw data representations.
     * @param data Raw representation of the module. Is normally obtained from a {@link InfrastructureManager.Configuration.RawData.ModulesConfigurationFileData} object
     * @return a Module correctly created, configured and initialized based on the raw data.
     * @throws ModuleNotDefinedException If the raw data, represents a module with a non-defined type or the "DEFAULT" type was not changed in the raw module class.
     */
    public PlatformModule create(ModuleConfigData data) throws ModuleNotDefinedException {

        PlatformModule result = switch (data.getType()) {
            case CONSOLE -> new ConsoleModule();
            case UTILITY -> new UtilityModule();
            case SCENARIO -> new ScenarioModule();
            case REST -> new RESTModule();
            case ADVANTEDGE -> new AdvantEdgeModule();
            case REMOTE_EXEC -> new RemoteExecutionModule();
            case MATCH_MAKING_RANDOM -> new MatchMakingRandomModule();
            case MATCH_MAKING_NAIVE -> new MatchMakingNaiveModule();
            case MATCH_MAKING_SCORE_BASED -> new MatchMakingScoreBasedModule();
            case NETWORK_STRUCTURE -> new NetworkModule();
            default -> throw new ModuleNotDefinedException("The module type for module" + data.getName() + "is not defined");
        };
        result.configure(data);
        return result;
    }
}
