package InfrastructureManager.Configuration.RawData;

import InfrastructureManager.ModuleManagement.RawData.ConnectionConfigData;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;

import java.util.List;


/**
 * Configuration Data Object (Mapped from JSON Config File). Is basically the POJO representation of the
 * configuration file.
 */
public class ModulesConfigurationFileData {

    private final List<ModuleConfigData> modules;
    private final List<ConnectionConfigData> connections;


    /**
     * Constructor of the class, normally called by the {@link com.fasterxml.jackson.databind.ObjectMapper}
     * that parses the configuration file from JSON to an object of this class. Starts all fields in null.
     */
    public ModulesConfigurationFileData() {
        modules = null;
        connections = null;
    }

    /**
     * Get the defined connections in their raw representation
     * @return A List of {@link ConnectionConfigData} objects.
     */
    public List<ConnectionConfigData> getConnections() {
        return connections;
    }


    /**
     * Get the defined modules in their raw representation
     * @return A List of {@link ModuleConfigData} objects.
     */
    public List<ModuleConfigData> getModules() {
        return modules;
    }
}
