package InfrastructureManager.Configuration.RawData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Configuration Data Object (Mapped from JSON Config File)
 */
public class MasterConfigurationData {

    private final IORawData ioData;
    private final List<ConnectionConfigData> connections;

    public MasterConfigurationData() {
        //Initialize all values in null;
        ioData = null;
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

    public IORawData getIoData() {
        return ioData;
    }
}
