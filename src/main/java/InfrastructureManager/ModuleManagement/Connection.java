package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.CommandSet;

public class Connection {

    private final PlatformOutput out;
    private final CommandSet commands;

    public Connection(PlatformOutput out, CommandSet commands) {
        this.out = out;
        this.commands = commands;
    }

    public PlatformOutput getOut() {
        return out;
    }

    public CommandSet getCommands() {
        return commands;
    }
}
