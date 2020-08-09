package InfrastructureManager;

import java.io.File;

public class MasterConfigurator {
    //TODO: Implement reading configuration from a JSON file
    //      Also, consider making it a singleton

    public CommandSet getCommands() {
        //TODO: Implement
        return CommandSet.getInstance();
    }
    public MasterInput getInput() {
        //TODO: Implement
        return new ConsoleInput();
    }
    public MasterOutput getOutput() {
        //TODO: Implement
        return new ConsoleOutput();
    }
}
