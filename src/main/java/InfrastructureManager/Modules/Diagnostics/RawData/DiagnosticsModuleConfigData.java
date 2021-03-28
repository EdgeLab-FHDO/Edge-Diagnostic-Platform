package InfrastructureManager.Modules.Diagnostics.RawData;

import InfrastructureManager.ModuleManagement.ModuleFactory.ModuleType;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Raw data representing a DiagnosticsModule.
 *
 * To create a module only the name is needed.
 * Type is automatically selected as {@link ModuleType}.DIAGNOSTICS
 */
public class DiagnosticsModuleConfigData extends ModuleConfigData {
    /**
     * Creates a new raw diagnostics module. Uses the {@link JsonProperty} annotation for extracting the
     * name while deserializing.
     * @param name Name of the module
     */
    public DiagnosticsModuleConfigData(@JsonProperty("name") String name) {
        super(name);
        this.type = ModuleType.DIAGNOSTICS;
    }
}
