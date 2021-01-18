package InfrastructureManager.Configuration;

import InfrastructureManager.Configuration.Exception.ConfigurationException;
import InfrastructureManager.Configuration.RawData.MasterConfigurationData;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleManagerException;
import InfrastructureManager.ModuleManagement.ModuleManager;
import InfrastructureManager.ModuleManagement.PlatformModule;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Configurator class for the master, that takes the values in the configuration file
 * object and gives the master the different elements based on that (Commands, and Runners)
 */
public class MasterConfigurator {

    private final ModuleManager manager;

    public MasterConfigurator(String configurationFilePath) throws ConfigurationException {
        ObjectMapper mapper = new ObjectMapper(); //Using Jackson Functionality
        manager = ModuleManager.getInstance();
        try {
            //Map the contents of the JSON file to a java object
            //Configuration File Interface
            manager.initialize(mapper.readValue(new File(configurationFilePath), MasterConfigurationData.class));
        } catch (IOException e) {
            throw new ConfigurationException("Error while reading JSON Config File",e);
        }
    }

    public List<PlatformModule> getModules() throws ModuleManagerException {
        return manager.getModules();
    }

}
