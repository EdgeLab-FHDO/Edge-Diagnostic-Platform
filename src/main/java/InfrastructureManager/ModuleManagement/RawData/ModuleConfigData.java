package InfrastructureManager.ModuleManagement.RawData;

import InfrastructureManager.ModuleManagement.ModuleFactory.*;
import InfrastructureManager.Modules.AdvantEDGE.RawData.AdvantEdgeModuleConfigData;
import InfrastructureManager.Modules.Console.RawData.ConsoleModuleConfigData;
import InfrastructureManager.Modules.Diagnostics.RawData.DiagnosticsModuleConfigData;
import InfrastructureManager.Modules.MatchMaking.Naive.RawData.MatchMakingNaiveModuleConfigData;
import InfrastructureManager.Modules.MatchMaking.Random.RawData.MatchMakingRandomModuleConfigData;
import InfrastructureManager.Modules.MatchMaking.ScoreBased.RawData.MatchMakingScoreBasedModuleConfigData;
import InfrastructureManager.Modules.REST.RawData.RESTModuleConfigData;
import InfrastructureManager.Modules.RemoteExecution.RawData.RemoteExecutionModuleConfigData;
import InfrastructureManager.Modules.Scenario.RawData.ScenarioModuleConfigData;
import InfrastructureManager.Modules.Utility.RawData.UtilityModuleConfigData;
import InfrastructureManager.Modules.NetworkStructure.RawData.NetworkModuleConfigData;
import com.fasterxml.jackson.annotation.*;

/**
 * Abstract class that is the raw representation of a module. An object of this class (more specifically one of its childs)
 * is the object representation of a module in the configuration file.
 *
 * Each specific module has a raw data class that inherits from this one.
 *
 * It uses {@link JsonTypeInfo} and {@link JsonSubTypes} annotations to induce polymorphic behavior into the JSON parsing.
 * It does so, by using the "type" argument in the JSON object and generating a specific raw data class, which is a subclass of
 * this one.
 *
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = ModuleConfigData.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConsoleModuleConfigData.class, name = "ConsoleModule"),
        @JsonSubTypes.Type(value = UtilityModuleConfigData.class, name = "UtilityModule"),
        @JsonSubTypes.Type(value = ScenarioModuleConfigData.class, name = "ScenarioModule"),
        @JsonSubTypes.Type(value = RESTModuleConfigData.class, name = "RESTModule"),
        @JsonSubTypes.Type(value = AdvantEdgeModuleConfigData.class, name = "AdvantEdgeModule"),
        @JsonSubTypes.Type(value = RemoteExecutionModuleConfigData.class, name = "RemoteExecutionModule"),
        @JsonSubTypes.Type(value = MatchMakingRandomModuleConfigData.class, name = "MatchMakingModule-Random"),
        @JsonSubTypes.Type(value = MatchMakingNaiveModuleConfigData.class, name = "MatchMakingModule-Naive"),
        @JsonSubTypes.Type(value = MatchMakingScoreBasedModuleConfigData.class, name = "MatchMakingModule-ScoreBased"),
        @JsonSubTypes.Type(value = NetworkModuleConfigData.class, name = "NetworkModule"),
        @JsonSubTypes.Type(value = DiagnosticsModuleConfigData.class, name = "DiagnosticsModule")
})
public abstract class ModuleConfigData {
    private final String name;
    @JsonIgnore
    protected ModuleType type;

    /**
     * Constructor of the class.
     * Sets the module name. This name will determine how the inputs and outputs are named.
     * Defines the type of the module as "DEFAULT". This is to be changed in each specific module
     * @param name Unique name of the module
     */
    @JsonCreator
    public ModuleConfigData(@JsonProperty("name") String name) {
        this.name = name;
        this.type = ModuleType.DEFAULT;
    }

    /**
     * @return The name of this module
     */
    public String getName() {
        return name;
    }

    /**
     * @return The type of this module (one of the defined {@link ModuleType} constants)
     */
    public ModuleType getType() {
        return type;
    }
}
