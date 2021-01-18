package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.RawData.MasterConfigurationData;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleManagerException;

import java.util.List;

public class ModuleManager {

    private ModuleFactory factory;
    private ModuleConnector connector;
    private List<PlatformModule> modules;
    private boolean connected;
    private boolean initialized;

    private static ModuleManager instance = null;

    private ModuleManager() {
        this.connected = false;
        this.initialized = false;
    }

    public void initialize(MasterConfigurationData data) {
        this.factory = new ModuleFactory(data);
        this.connector = new ModuleConnector(data);
        this.initialized = true;
    }

    private void connectModules() {
        this.modules = this.factory.getModules();
        this.connector.connectModules(this.modules);
        this.connected = true;
    }

    public List<PlatformModule> getModules() throws ModuleManagerException{
        if (!initialized) throw new ModuleManagerException("Manager was not initialized with data");
        if (!connected) {
            connectModules();
        }
        return modules;
    }

    public static ModuleManager getInstance() {
        if (instance == null) {
            instance = new ModuleManager();
        }
        return instance;
    }
}
