package InfrastructureManager.Configuration;

import InfrastructureManager.Configuration.Exception.ConfigurationException;
import InfrastructureManager.Configuration.RawData.ModulesConfigurationFileData;
import InfrastructureManager.ModuleManagement.ModuleManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Configurator class for the master, that takes the values in the configuration file
 * object and gives the master the different elements based on that (Commands, and Runners)
 */
public class ModuleManagerConfigurator {

    private final ModuleManager manager;

    public ModuleManagerConfigurator(String configurationFilePath) throws ConfigurationException {
        ObjectMapper mapper = new ObjectMapper(); //Using Jackson Functionality
        manager = new ModuleManager();
        try {
            //Map the contents of the JSON file to a java object
            manager.initialize(mapper.readValue(new File(configurationFilePath), ModulesConfigurationFileData.class));
        } catch (IOException e) {
            throw new ConfigurationException("Error while reading JSON Config File",e);
        }
    }

    public ModuleManager getConfiguredManager() {
        return manager;
    }
}
