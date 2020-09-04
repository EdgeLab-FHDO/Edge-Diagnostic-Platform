package InfrastructureManager;

import java.util.List;
import java.util.Map;


/**
 * Configuration Data Object (Mapped from JSON Config File)
 */
public class MasterConfigurationData {

    private final List<RunnerConfigData> runners;
    private final Map<String, String> commands;

    public MasterConfigurationData() {
        //Initialize all values in null;
        commands = null;
        runners = null;
    }

    /**
     * Method to get the commands defined for the master
     * @return The commands defined in the configuration in form of a Map Object
     */
    public Map<String, String> getCommands() {
        return commands;
    }

    /**
     * Mehtod to get the list of runners defined for the master
     * @return The Runners in form of a list of RunnerConfigData (Raw data for Runners) objects
     */
    public List<RunnerConfigData> getRunners() {
        return runners;
    }
}
