package InfrastructureManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class MasterConfigurator {
    private MasterConfigurationData data;
    //TODO: Implement reading configuration from a JSON file
    //      Also, consider making it a singleton

    public MasterConfigurator() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.data = mapper.readValue(new File("src/main/resources/config.json"), MasterConfigurationData.class);
        } catch (IOException e) {
            System.out.println("Error while reading JSON Config File");
            e.printStackTrace();
        }
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
