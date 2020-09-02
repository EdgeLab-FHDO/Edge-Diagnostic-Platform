package InfrastructureManager;

import InfrastructureManager.Rest.RestInput;
import InfrastructureManager.Rest.RestOutput;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 * Configurator class for the master, that takes the values in the configuration file
 * object and gives the master the different elements based on that (Different Input types,
 * output types, etc.)
 */
public class MasterConfigurator {
    private final String CONFIG_FILE_PATH = "src/main/resources/config.json";
    private MasterConfigurationData data; //Configuration File Interface
    //TODO: consider making it a singleton

    public MasterConfigurator() {
        ObjectMapper mapper = new ObjectMapper(); //Using Jackson Functionality
        try {
            //Map the contents of the JSON file to a java object
            this.data = mapper.readValue(new File(CONFIG_FILE_PATH), MasterConfigurationData.class);
        } catch (IOException e) {
            System.out.println("Error while reading JSON Config File");
            e.printStackTrace();
        }
    }

    /**
     * Based on the commands defined in the config file, configures the CommandSet instance and returns it
     * @return The command set for the master to use
     */
    public CommandSet getCommands() {
        CommandSet.getInstance().set(data.getCommands());
        return CommandSet.getInstance();
    }

    /**
     * Based on the input defined in the config file, returns different types of input to the master
     * @return an object that implements the MasterInput interface
     */
    public MasterInput getInput() {
        //TODO: Add more inputs
        switch (data.getInputSource()) {
            case "console":
                return new ConsoleInput();
            case "rest":
                return new RestInput();
            default:
                throw new IllegalArgumentException("Invalid input in Configuration");
        }
    }

    /**
     * Based on the output defined in the config file, returns different types of output objects to the master
     * @return an Object that implements the MasterOutput interface
     */
    public MasterOutput getOutput() {
        //TODO: Add more outputs
        switch (data.getOutputSource()) {
            case "console":
                return new ConsoleOutput();
            case "rest":
                return new RestOutput();
            default:
                throw new IllegalArgumentException("Invalid output in Configuration");
        }
    }
}
