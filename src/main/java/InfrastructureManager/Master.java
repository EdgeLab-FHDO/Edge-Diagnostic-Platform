package InfrastructureManager;

import InfrastructureManager.Configuration.Exception.ConfigurationException;
import InfrastructureManager.Configuration.ModuleManagerConfigurator;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleManagerException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.ModuleManagement.ModuleManager;

import java.util.Arrays;
import java.util.List;

/**
 * Master Class of the Infrastructure Manager, singleton class
 */
public class Master {

    private static final String DEFAULT_CONFIG_PATH = "src/main/resources/NewConfiguration.json";
    private static Master instance = null;

    private ModuleManager manager;

    public void configure(String configPath) throws ConfigurationException {
        ModuleManagerConfigurator configurator = new ModuleManagerConfigurator(configPath);
        manager = configurator.getConfiguredManager();
    }

    public ModuleManager getManager() throws ModuleManagerException {
        if (!manager.isInitialized()) {
            throw new ModuleManagerException("Manager was not initialized with data");
        }
        return manager;
    }

    /**
     * Singleton method for getting the only instance of the class
     * @return The instance of the Master Class
     */
    public static Master getInstance() {
        if (instance == null) {
            instance = new Master();
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    private static boolean searchAutoStart(String[] args) {
        if (args.length > 0) {
            return !Arrays.asList(args).contains("--autostart=false");
        }
        return true;
    }

    private static String searchConfigFilePath(String[] args) throws IllegalArgumentException {
        if (args.length > 0) {
            List<String> argList = Arrays.asList(args);
            if (argList.contains("-c")) {
                try {
                    return argList.get(argList.indexOf("-c") + 1);
                } catch (IndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("\"-c\" was used but no path was indicated for the config file");
                }
            }
        }
        return DEFAULT_CONFIG_PATH;
    }

    public static void main(String[] args) {

        try {
            boolean autostart = searchAutoStart(args);
            String configPath = searchConfigFilePath(args);
            Master master = Master.getInstance();
            master.configure(configPath);
            ModuleManager manager = master.getManager();
            if (autostart) {
                manager.startAllModules();
            } else {
                manager.startModule("console");
            }

        } catch (IllegalArgumentException | ConfigurationException | ModuleManagerException | ModuleNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
