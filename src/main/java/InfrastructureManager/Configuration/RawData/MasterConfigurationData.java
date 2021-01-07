package InfrastructureManager.Configuration.RawData;

import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Configuration Data Object (Mapped from JSON Config File)
 */
public class MasterConfigurationData {

    //private final IORawData ioData;
    private final List<ModuleConfigData> modules;
    private final List<ConnectionConfigData> connections;

    public MasterConfigurationData() {
        //Initialize all values in null;
        //ioData = null;
        modules = null;
        connections = null;
    }

    public List<ConnectionConfigData> getConnections() {
        return connections;
    }

    public Set<String> getConnectedInputs() {
        Set<String> result = new HashSet<>();
        if (this.connections != null) {
            for (ConnectionConfigData data : this.connections) {
                result.add(data.getIn());
            }
        }
        return result;
    }

    public List<ModuleConfigData> getModules() {
        return modules;
    }

    public IORawData getIoData() {
        return null;
        //return ioData;
    }
}
