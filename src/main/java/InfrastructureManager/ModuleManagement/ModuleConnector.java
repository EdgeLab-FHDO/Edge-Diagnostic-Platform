package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.CommandSet;
import InfrastructureManager.Configuration.RawData.MasterConfigurationData;
import InfrastructureManager.ModuleManagement.Exception.Creation.IncorrectIONameException;
import InfrastructureManager.ModuleManagement.Exception.Creation.IncorrectInputException;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleNotFoundException;
import InfrastructureManager.ModuleManagement.RawData.ConnectionConfigData;

import java.util.List;
import java.util.Optional;

public class ModuleConnector {
    private final List<ConnectionConfigData> data;
    private List<PlatformModule> modules;

    public ModuleConnector(MasterConfigurationData data) {
        this.data = data.getConnections();
    }

    public void connectModules(List<PlatformModule> modules) {
        this.modules = modules;
        modules.forEach(m -> {
            try {
                connectSingleModule(m);
            } catch (IncorrectIONameException | ModuleNotFoundException | IncorrectInputException e) {
                System.err.println("Module " + m.getName() + " could not be connected");
                e.printStackTrace();
            }
        });
    }


    private void connectSingleModule(PlatformModule module) throws IncorrectIONameException, ModuleNotFoundException, IncorrectInputException {
        String moduleName = module.getName();

        for (ConnectionConfigData connectionData : data) {
            if (getInputModuleName(connectionData).equals(moduleName)) {
                ModuleOutput output = findOutput(connectionData.getOut());
                CommandSet commandSet = new CommandSet();
                commandSet.set(connectionData.getCommands());
                Connection connection = new Connection(output,commandSet);
                module.addConnection(connectionData.getIn(),connection);
            }
        }
    }

    private String getInputModuleName(ConnectionConfigData connectionData) throws IncorrectIONameException {
        return extractName(connectionData.getIn());
    }

    private String extractName(String IOName) throws IncorrectIONameException {
        int index = IOName.indexOf('.');
        if (index != -1) {
            return IOName.substring(0,IOName.indexOf('.'));
        } else {
            throw new IncorrectIONameException(IOName + " is not a valid name for a module IO");
        }
    }

    private ModuleOutput findOutput(String outputName) throws ModuleNotFoundException, IncorrectIONameException {
        PlatformModule module = findOutputModule(outputName);
        for (ModuleOutput output : module.getOutputs()) {
            if (outputName.equals(output.getName())) {
                return output;
            }
        }
        throw new ModuleNotFoundException("No module could be found with defined output " + outputName);
    }

    private PlatformModule findOutputModule(String outputName) throws ModuleNotFoundException, IncorrectIONameException {
        String moduleName = extractName(outputName);
        Optional<PlatformModule> module = modules.stream().filter(m -> m.getName().equals(moduleName))
                .findFirst();
        return module.orElseThrow(() -> new ModuleNotFoundException("Output Module was not found"));
    }

}
