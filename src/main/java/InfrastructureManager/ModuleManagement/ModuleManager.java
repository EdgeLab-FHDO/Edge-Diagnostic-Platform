package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.RawData.MasterConfigurationData;
import InfrastructureManager.ModuleManagement.Exception.Creation.IncorrectIONameException;
import InfrastructureManager.ModuleManagement.Exception.Creation.IncorrectInputException;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleManagerException;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleNotDefinedException;
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

    public void initialize(MasterConfigurationData data) {
        this.factory = new ModuleFactory();
        createModules(data.getModules());
        this.connector = new ModuleConnector(data.getConnections(), this.modules);
        connectModules();
        this.initialized = true;
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

    public List<PlatformModule> getModules() throws ModuleManagerException{
        if (!initialized) throw new ModuleManagerException("Manager was not initialized with data");
        return modules;
    }
}
