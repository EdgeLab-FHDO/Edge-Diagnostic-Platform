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

public class ModuleManager {

    private ModuleFactory factory;
    private ModuleConnector connector;
    private final List<PlatformModule> modules;
    private boolean initialized;

    public ModuleManager() {
        this.initialized = false;
        this.modules = new ArrayList<>();
    }

    public void initialize(ModulesConfigurationFileData data) {
        this.factory = new ModuleFactory();
        createModules(data.getModules());
        this.connector = new ModuleConnector(data.getConnections(), this.modules);
        connectModules();
        this.initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public List<PlatformModule> getModules() throws ModuleManagerException{
        if (!isInitialized()) throw new ModuleManagerException("Manager was not initialized with data");
        return modules;
    }

    public void exitAllModules() {
        modules.forEach(PlatformModule::stop);
    }

    public void stopModule(String moduleName) throws ModuleNotFoundException {
        findModuleByName(moduleName).stop();
    }

    public void startAllModules() {
        modules.forEach(PlatformModule::start);
    }

    public void startModule(String moduleName) throws ModuleNotFoundException {
        findModuleByName(moduleName).start();
    }

    public void pauseModule(String moduleName) throws ModuleNotFoundException {
        PlatformModule moduleToPause = findModuleByName(moduleName);
        if (moduleToPause.getState() == PlatformModule.ModuleState.RUNNING) {
            moduleToPause.pause();
        }
    }

    public void pauseAllModules() {
        modules.stream().filter(m -> m.getState() == PlatformModule.ModuleState.RUNNING)
                .forEach(PlatformModule::pause);
    }

    public void resumeModule(String moduleName) throws ModuleNotFoundException {
        PlatformModule moduleToResume = findModuleByName(moduleName);
        if (moduleToResume.getState() == PlatformModule.ModuleState.PAUSED) {
            moduleToResume.resume();
        }
    }

    public void resumeAllModules() {
        modules.stream().filter(m -> m.getState() == PlatformModule.ModuleState.PAUSED)
                .forEach(PlatformModule::resume);
    }

    private PlatformModule findModuleByName(String moduleName) throws ModuleNotFoundException {
        return modules.stream().filter(m -> m.getName()
                .equals(moduleName))
                .findFirst()
                .orElseThrow(() -> new ModuleNotFoundException("Module " + moduleName + " was not found"));
    }

    private void createModules(List<ModuleConfigData> data) {
        data.forEach(m -> {
            try {
                this.modules.add(this.factory.create(m));
            } catch (ModuleNotDefinedException e) {
                e.printStackTrace();
            }
        });
    }

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
