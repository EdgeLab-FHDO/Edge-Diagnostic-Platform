package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.CommandSet;

/**
 * Represents a Connection inside a Module.
 *
 * Each connection is related to one input. A connection is defined by:
 * - One {@link PlatformOutput} (to which the input is connected to)
 * - One {@link CommandSet} which maps commands from the input into commands for the output
 */
public class Connection {

    private final PlatformOutput out;
    private final CommandSet commands;

    /**
     * Constructor of the class.
     * @param out Output of the connection
     * @param commands Mapping between input and output
     */
    public Connection(PlatformOutput out, CommandSet commands) {
        this.out = out;
        this.commands = commands;
    }

    /**
     * Return the output that is part of this connection
     * @return Output of this connection
     */
    public PlatformOutput getOut() {
        return out;
    }

    /**
     * Return the mapping between input and output
     * @return Mapping of commands from the input into commands for the output
     */
    public CommandSet getCommands() {
        return commands;
    }
}
