package InfrastructureManager;

import java.io.File;

public class MasterConfigurator {
    private MasterConfigurationData data;
    //TODO: Implement reading configuration from a JSON file
    //      Also, consider making it a singleton

    public MasterConfigurator() {
        //TODO: Read JSON and Create the data object
        this.data = new MasterConfigurationData(); //Remove
    }

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
