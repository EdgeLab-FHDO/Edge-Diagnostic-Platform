package InfrastructureManager.Configuration.RawData;

import InfrastructureManager.ModuleManagement.RawData.ConnectionConfigData;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;

import java.util.List;


/**
 * Configuration Data Object (Mapped from JSON Config File)
 */
public class ModulesConfigurationFileData {

    private final List<ModuleConfigData> modules;
    private final List<ConnectionConfigData> connections;

    public ModulesConfigurationFileData() {
        //Initialize all values in null;
        modules = null;
        connections = null;
    }

    public List<ConnectionConfigData> getConnections() {
        return connections;
    }


    public List<ModuleConfigData> getModules() {
        return modules;
    }
}
