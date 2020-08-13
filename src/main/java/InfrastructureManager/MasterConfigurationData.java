package InfrastructureManager;

import java.util.Map;

/**
 * Configuration Data Object (Mapped from JSON Config File)
 */
public class MasterConfigurationData {
    private final String inputSource;
    private final String outputSource;
    private final Map<String, String> commands;

    public MasterConfigurationData() {
        //Initialize all values in null;
        inputSource = null;
        outputSource = null;
        commands = null;
    }

    /**
     * Method to get the input source
     * @return the input source defined in the configuration
     */
    public String getInputSource() {
        return inputSource;
    }

    /**
     * Method for getting the output source
     * @return the output source defined in the configuration
     */
    public String getOutputSource() {
        return outputSource;
    }

    /**
     * Method to get the commands defined for the master
     * @return The commands defined in the configuration in form of a Map Object
     */
    public Map<String, String> getCommands() {
        return commands;
    }

}
