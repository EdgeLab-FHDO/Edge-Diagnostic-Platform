package InfrastructureManager;

import InfrastructureManager.Configuration.Exception.ConfigurationException;
import InfrastructureManager.Configuration.ModuleManagerConfigurator;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleManagerException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.ModuleManagement.ModuleManager;

import java.util.Arrays;
import java.util.List;

/**
 * Master Class of the Infrastructure Manager, singleton class.
 *
 * Wraps a {@link ModuleManager} instance which provides all the functionality (interaction with modules).
 *
 * The master acts as entry point for the user, contains the main method of the application and is in charge of
 * configuration of the module manager.
 */
public class Master {

    private static final String DEFAULT_CONFIG_PATH = "src/main/resources/Configuration.json";
    private static Master instance = null;

    private ModuleManager manager;

    /**
     * Configure the master (and its manager) based on a configuration file. This method should be called before calling {@link #getManager()}
     * @param configPath Path for the JSON configuration file
     * @throws ConfigurationException if an error occurs while parsing the file or configuring the module manager
     */
    public void configure(String configPath) throws ConfigurationException {
        ModuleManagerConfigurator configurator = new ModuleManagerConfigurator(configPath);
        manager = configurator.getConfiguredManager();
    }

    /**
     * Get the ModuleManager instance contained in this class. This can later be used to manipulate modules
     * @return A module manager configured and ready to be used
     * @throws ModuleManagerException If the manager has not been initialized ({@link #configure(String)} has not been called yet)
     */
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

    /**
     * Resets the singleton instance, so that next time {@link #getInstance()} is called, a new Master is created.
     * This is useful mainly for automatic testing.
     */
    public static void resetInstance() {
        instance = null;
    }

    /**
     * Based on arguments that come from the execution of the application, search if the autostart feature of the app is to
     * be turned on or off.
     * Autostart makes all defined modules (in the configuration) to start running as soon as the platform starts.
     * If autostart is disabled, the configuration should include a module called "console" which will be the only one started.
     * @param args Arguments coming from console execution of the application
     * @return False if one of the arguments is "--autostart==false". True otherwise
     */
    private static boolean searchAutoStart(String[] args) {
        if (args.length > 0) {
            return !Arrays.asList(args).contains("--autostart=false");
        }
        return true;
    }

    /**
     * Based on arguments that come from the execution of the application, search for the path of the configuration file.
     *
     * The path defaults to "src/main/resources/Configuration.json". In order to specify a custom path, two arguments
     * should be passed in the form of "-c PATH". The "-c" flag indicated that the next argument is the path for the configuration path.
     * @param args Arguments coming from console execution of the application
     * @return The path defined in the arguments or the default path if no path argument is found.
     * @throws IllegalArgumentException If the "-c" flag is passed but no path follows
     */
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
            master.configure(configPath); //Configure master (and manager) based on the configPath
            ModuleManager manager = master.getManager();
            if (autostart) {
                manager.startAllModules(); //If autostart is enabled (default) start all modules
            } else {
                manager.startModule("console"); //Otherwise, just start a console module
            }

        } catch (IllegalArgumentException | ConfigurationException | ModuleManagerException | ModuleNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
