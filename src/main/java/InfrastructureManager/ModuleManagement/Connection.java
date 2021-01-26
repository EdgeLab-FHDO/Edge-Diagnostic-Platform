package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.CommandSet;

public class Connection {

    private final ModuleOutput out;
    private final CommandSet commands;

    public Connection(ModuleOutput out, CommandSet commands) {
        this.out = out;
        this.commands = commands;
    }

    public ModuleOutput getOut() {
        return out;
    }

    public CommandSet getCommands() {
        return commands;
    }
}
