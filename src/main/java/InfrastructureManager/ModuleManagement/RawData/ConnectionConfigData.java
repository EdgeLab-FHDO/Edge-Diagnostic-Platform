package InfrastructureManager.ModuleManagement.RawData;

import java.util.Map;

/**
 * Raw data class that represents Connections. This is the representation of a connection in the configuration
 * file.
 *
 * Raw connections are composed by an input name, an output name (to which the input is connected) and a mapping
 * of commands between the two.
 */
public class ConnectionConfigData {
    private final String in;
    private final String out;
    private final Map<String,String> commands;

    /**
     * Constructor of the class. Initialises all internal fields in null.
     * Is normally called by the {@link com.fasterxml.jackson.databind.ObjectMapper} when deserializing the
     * configuration file.
     */
    public ConnectionConfigData() {
        this.in = null;
        this.out = null;
        this.commands = null;
    }

    /**
     * Returns the input of this connection in form of its name.
     * @return The name of the input in this connection.
     */
    public String getIn() {
        return in;
    }

    /**
     * Returns the output of this connection in form of its name.
     * @return The name of the output in this connection.
     */
    public String getOut() {
        return out;
    }

    /**
     * Returns the raw commands defined in the connection. By raw, it means that parameter mapping ($parameters)
     * have not been substituted yet.
     * @return The mapping between input readings and output commands that defines this connection.
     */
    public Map<String, String> getCommands() {
        return commands;
    }
}
