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
        //TODO: Implement to replace actual implementation with txt
        return CommandSet.getInstance();
    }
    public MasterInput getInput() {
        switch (data.getInputSource()) {
            case "console":
                return new ConsoleInput();
            default:
                throw new IllegalArgumentException("Invalid input in Configuration");
        }
    }
    public MasterOutput getOutput() {
        switch (data.getOutputSource()) {
            case "console":
                return new ConsoleOutput();
            default:
                throw new IllegalArgumentException("Invalid output in Configuration");
        }
    }
}
