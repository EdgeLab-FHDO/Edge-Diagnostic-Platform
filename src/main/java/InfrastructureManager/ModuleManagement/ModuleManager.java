package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.RawData.MasterConfigurationData;

import java.util.List;

public class ModuleManager {

    private ModuleFactory factory;
    private ModuleConnector connector;
    private List<PlatformModule> modules;
    private boolean connected;

    private static ModuleManager instance = null;

    private ModuleManager() {
        this.connected = false;
    }

    public void initialize(MasterConfigurationData data) {
        this.factory = new ModuleFactory(data);
        this.connector = new ModuleConnector(data);
    }

    private void connectModules() {
        this.modules = this.factory.getModules();
        this.connector.connectModules(this.modules);
        this.connected = true;
    }

    public List<PlatformModule> getModules() {
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
