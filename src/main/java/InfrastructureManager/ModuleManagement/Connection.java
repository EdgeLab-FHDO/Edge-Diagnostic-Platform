package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.CommandSet;
import InfrastructureManager.MasterOutput;

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
