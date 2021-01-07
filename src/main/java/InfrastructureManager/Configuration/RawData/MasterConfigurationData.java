package InfrastructureManager.Configuration.RawData;

import InfrastructureManager.ModuleManagement.RawData.ConnectionConfigData;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Configuration Data Object (Mapped from JSON Config File)
 */
public class MasterConfigurationData {

    private final List<ModuleConfigData> modules;
    private final List<ConnectionConfigData> connections;

    public MasterConfigurationData() {
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
