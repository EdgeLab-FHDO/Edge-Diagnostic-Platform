package InfrastructureManager.Configuration;

import InfrastructureManager.Configuration.Exception.ConfigurationException;
import InfrastructureManager.Configuration.RawData.ModulesConfigurationFileData;
import InfrastructureManager.ModuleManagement.ModuleManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Configurator class for the {@link ModuleManager}, that reads the values in the configuration file
 * and generates an object representing the data. This is then used to initialized and return a configured
 * Module manager
 */
public class ModuleManagerConfigurator {

    private final ModuleManager manager;

    /**
     * Constructor of the class. Uses an {@link ObjectMapper} object to read data from a configuration file,
     * and create an object that represents that data of the type {@link ModulesConfigurationFileData}.
     * With this object, it creates and initializes a {@link ModuleManager}.
     * @param configurationFilePath Path where the configuration file will be read from
     * @throws ConfigurationException If an error occurs while reading the file or parsing it from JSON.
     */
    public ModuleManagerConfigurator(String configurationFilePath) throws ConfigurationException {
        ObjectMapper mapper = new ObjectMapper(); //Using Jackson Functionality
        manager = new ModuleManager();
        try {
            //Map the contents of the JSON file to a java object and initialize the manager with it.
            manager.initialize(mapper.readValue(new File(configurationFilePath), ModulesConfigurationFileData.class));
        } catch (IOException e) {
            throw new ConfigurationException("Error while reading JSON Config File",e);
        }
    }

    /**
     * Get the manager that belongs to this configurator, which was already created and initialized.
     * @return A {@link ModuleManager} that can already be used.
     */
    public ModuleManager getConfiguredManager() {
        return manager;
    }
}
