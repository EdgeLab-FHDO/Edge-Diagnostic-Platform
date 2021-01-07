package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.CommandSet;
import InfrastructureManager.Configuration.RawData.MasterConfigurationData;
import InfrastructureManager.Connection;
import InfrastructureManager.MasterOutput;
import InfrastructureManager.ModuleManagement.Exception.ModuleNotFoundException;
import InfrastructureManager.ModuleManagement.RawData.ConnectionConfigData;

import java.util.List;
import java.util.Optional;

public class ModuleConnector {
    private final List<ConnectionConfigData> data;
    private List<PlatformModule> modules;

    public ModuleConnector(MasterConfigurationData data) {
        this.data = data.getConnections();
    }

    public void setModules(List<PlatformModule> modules) {
        this.modules = modules;
    }

    public List<PlatformModule> getConnectedModules() {
        modules.forEach(this::connectModule);
        return modules;
    }


    private void connectModule(PlatformModule module) {
        String moduleName = module.getName();

        for (ConnectionConfigData connectionData : data) {
            if (getInputModuleName(connectionData).equals(moduleName)) {
                MasterOutput output = findOutput(connectionData.getOut());
                CommandSet commandSet = new CommandSet();
                commandSet.set(connectionData.getCommands());
                Connection connection = new Connection(output,commandSet);
                module.addConnection(connectionData.getIn(),connection);
            }
        }
    }

    private String getInputModuleName(ConnectionConfigData connectionData) {
        return extractName(connectionData.getIn());
    }

    private String extractName(String IOName) {
        return IOName.substring(0,IOName.indexOf('.'));
    }

    private MasterOutput findOutput(String outputName) throws ModuleNotFoundException {
        PlatformModule module = findOutputModule(outputName);
        for (MasterOutput output : module.getOutputs()) {
            if (outputName.equals(output.getName())) {
                return output;
            }
        }
        throw new ModuleNotFoundException("No module could be found with defined output " + outputName);
    }

    private PlatformModule findOutputModule(String outputName) throws ModuleNotFoundException {
        String moduleName = extractName(outputName);
        Optional<PlatformModule> module = modules.stream().filter(m -> m.getName().equals(moduleName))
                .findFirst();
        return module.orElseThrow(() -> new ModuleNotFoundException("Output Module was not found"));
    }

}
