package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.RawData.ModulesConfigurationFileData;
import InfrastructureManager.ModuleManagement.Exception.Creation.IncorrectIONameException;
import InfrastructureManager.ModuleManagement.Exception.Creation.IncorrectInputException;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleManagerException;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleNotDefinedException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;

import java.util.ArrayList;
import java.util.List;

/**
 *Class that allows to create, connect and manipulate the modules of the platform.
 *
 * This class is in charge of managing creation and connection of modules based on raw configuration data
 * It also provides an interface for controlling module's lifecycles allowing to:
 *  - Start all modules (or a particular one)
 *  - Pause all running modules (or a particular one)
 *  - Resume all pause modules (or a particular one)
 *  - Stop all modules (or a particular one)
 */
public class ModuleManager {

    private ModuleFactory factory;
    private ModuleConnector connector;
    private final List<PlatformModule> modules;
    private boolean initialized;

    /**
     * Constructor of the class. Creates a new Module Manager object. Initializes the modules list.
     */
    public ModuleManager() {
        this.initialized = false;
        this.modules = new ArrayList<>();
    }

    /**
     * Initializes the manager based on raw data representing modules. This method shoulb be called directly after
     * creating an object of this class, otherwise causing faulty behavior (Exception).
     *
     * It creates the modules using a {@link ModuleFactory} and connects them using a {@link ModuleConnector}
     * @param data Raw data representing Modules and Connections
     */
    public void initialize(ModulesConfigurationFileData data) {
        this.factory = new ModuleFactory();
        createModules(data.getModules());
        this.connector = new ModuleConnector(data.getConnections(), this.modules);
        connectModules();
        this.initialized = true;
    }

    /**
     * Checks if the manager is initialized (the initialize() method was called)
     * @return True if the manager is initialized, false otherwise.
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Returns the created and connected modules of the platform
     * @return A list of modules of the platform
     * @throws ModuleManagerException If the manager has not been initialized
     */
    public List<PlatformModule> getModules() throws ModuleManagerException{
        if (!isInitialized()) throw new ModuleManagerException("Manager was not initialized with data");
        return modules;
    }

    /**
     * Allows for stopping and exiting execution in all currently running modules.
     */
    public void exitAllModules() {
        modules.forEach(PlatformModule::stop);
    }

    /**
     * Stops a running module
     * @param moduleName Name of the module to stop.
     * @throws ModuleNotFoundException If a module with the passed name could not be found inside the platform's modules
     */
    public void stopModule(String moduleName) throws ModuleNotFoundException {
        findModuleByName(moduleName).stop();
    }

    /**
     *Allows for starting all modules defined in the platform
     */
    public void startAllModules() {
        modules.forEach(PlatformModule::start);
    }

    /**
     * Starts a module. If called on a running module, restarts it.
     * @param moduleName Name of the module to start
     * @throws ModuleNotFoundException If a module with the passed name could not be found inside the platform's modules
     */
    public void startModule(String moduleName) throws ModuleNotFoundException {
        findModuleByName(moduleName).start();
    }

    /**
     * Pauses a running module. If called on a paused module has no effect
     * @param moduleName Name of the module to pause
     * @throws ModuleNotFoundException If a module with the passed name could not be found inside the platform's modules
     */
    public void pauseModule(String moduleName) throws ModuleNotFoundException {
        PlatformModule moduleToPause = findModuleByName(moduleName);
        if (moduleToPause.getState() == PlatformModule.ModuleState.RUNNING) {
            moduleToPause.pause();
        }
    }

    /**
     * Allows to pause all running modules. Only affects running modules
     */
    public void pauseAllModules() {
        modules.stream().filter(m -> m.getState() == PlatformModule.ModuleState.RUNNING)
                .forEach(PlatformModule::pause);
    }

    /**
     * Resumes a paused module. If called on a running module has no effect
     * @param moduleName Name of the module to be paused
     * @throws ModuleNotFoundException If a module with the passed name could not be found inside the platform's modules
     */
    public void resumeModule(String moduleName) throws ModuleNotFoundException {
        PlatformModule moduleToResume = findModuleByName(moduleName);
        if (moduleToResume.getState() == PlatformModule.ModuleState.PAUSED) {
            moduleToResume.resume();
        }
    }

    /**
     *Allows to resume all paused modules
     */
    public void resumeAllModules() {
        modules.stream().filter(m -> m.getState() == PlatformModule.ModuleState.PAUSED)
                .forEach(PlatformModule::resume);
    }

    /**
     * Given the name of a module finds it in the platform's defined module list
     * @param moduleName Name of the module to find
     * @return PlatformModule object equivalent to the passed name
     * @throws ModuleNotFoundException If a module with the passed name could not be found inside the platform's modules
     */
    private PlatformModule findModuleByName(String moduleName) throws ModuleNotFoundException {
        return modules.stream().filter(m -> m.getName()
                .equals(moduleName))
                .findFirst()
                .orElseThrow(() -> new ModuleNotFoundException("Module " + moduleName + " was not found"));
    }

    /**
     * Uses a {@link ModuleFactory} to create all defined modules for the platform
     * @param data List of module raw data
     */
    private void createModules(List<ModuleConfigData> data) {
        data.forEach(m -> {
            try {
                this.modules.add(this.factory.create(m));
            } catch (ModuleNotDefinedException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Uses a {@link ModuleConnector} to connect previously created modules with each other (inputs and outputs)
     */
    private void connectModules() {
        this.modules.forEach(m -> {
            try {
                connector.connectModule(m);
            } catch (IncorrectIONameException | ModuleNotDefinedException | IncorrectInputException e) {
                System.err.println("Module " + m.getName() + " could not be connected");
                e.printStackTrace();
            }
        });
    }
}
