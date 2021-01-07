package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.RawData.MasterConfigurationData;

import java.util.List;

public class ModuleManager {

    private ModuleFactory factory;
    private ModuleConnector connector;

    private static ModuleManager instance = null;

    private ModuleManager() {}

    public void initialize(MasterConfigurationData data) {
        this.factory = new ModuleFactory(data);
        this.connector = new ModuleConnector(data);
    }

    public List<PlatformModule> getModules() {
        this.connector.setModules(this.factory.getModules());
        return this.connector.getConnectedModules();
    }

    public static ModuleManager getInstance() {
        if (instance == null) {
            instance = new ModuleManager();
        }
        return instance;
    }
}
