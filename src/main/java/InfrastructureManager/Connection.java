package InfrastructureManager;

import InfrastructureManager.Configuration.CommandSet;

public class Connection {

    private final MasterOutput out;
    private final CommandSet commands;

    public Connection(MasterOutput out, CommandSet commands) {
        this.out = out;
        this.commands = commands;
    }

    public MasterOutput getOut() {
        return out;
    }

    public String getOutputName() {
        return out.getName();
    }

    public CommandSet getCommands() {
        return commands;
    }
}
