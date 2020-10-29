package InfrastructureManager.Configuration.RawData;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Configuration Data Object (Mapped from JSON Config File)
 */
public class MasterConfigurationData {

    private IORawData ioData;
    private List<ConnectionConfigData> connections;
    //private final Map<String, String> commands;

    public MasterConfigurationData() {
        //Initialize all values in null;
        ioData = null;
        connections = null;
        //commands = null;
    }

    /**
     * Method to get the commands defined for the master
     * @return The commands defined in the configuration in form of a Map Object
     */
    /*public Map<String, String> getCommands() {
        return commands;
    }*/

    public List<ConnectionConfigData> getConnections() {
        return connections;
    }

    public Set<String> getConnectedInputs() {
        Set<String> result = new HashSet<>();
        for (ConnectionConfigData data : this.connections) {
            result.add(data.getIn());
        }
        return result;
    }

    public IORawData getIoData() {
        return ioData;
    }
}
