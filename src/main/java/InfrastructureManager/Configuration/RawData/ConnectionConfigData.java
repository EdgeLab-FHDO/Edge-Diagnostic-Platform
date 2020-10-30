package InfrastructureManager.Configuration.RawData;

import java.util.Map;

public class ConnectionConfigData {
    private final String in;
    private final String out;
    private final Map<String,String> commands;

    public ConnectionConfigData() {
        this.in = null;
        this.out = null;
        this.commands = null;
    }

    public String getIn() {
        return in;
    }

    public String getOut() {
        return out;
    }

    public Map<String, String> getCommands() {
        return commands;
    }
}
