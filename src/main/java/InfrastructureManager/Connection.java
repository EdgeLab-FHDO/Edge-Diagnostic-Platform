package InfrastructureManager;

import InfrastructureManager.Configuration.CommandSet;

public class Connection {

    //private final MasterInput in;
    private final MasterOutput out;
    private final CommandSet commands;

    public Connection(MasterOutput out, CommandSet commands) {
        //this.in = in;
        this.out = out;
        this.commands = commands;
    }

    /*public String getInputName() {
        return in.getName();
    }*/

    public String getOutputName() {
        return out.getName();
    }

    public CommandSet getCommands() {
        return commands;
    }
}
