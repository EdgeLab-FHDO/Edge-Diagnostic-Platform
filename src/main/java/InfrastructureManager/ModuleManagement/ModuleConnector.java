package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.CommandSet;
import InfrastructureManager.ModuleManagement.Exception.Creation.IncorrectIONameException;
import InfrastructureManager.ModuleManagement.Exception.Creation.IncorrectInputException;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleNotDefinedException;
import InfrastructureManager.ModuleManagement.RawData.ConnectionConfigData;

import java.util.List;
import java.util.Optional;

public class ModuleConnector {
    private final List<ConnectionConfigData> connectionDataList;
    private final List<PlatformModule> modules;

    public ModuleConnector(List<ConnectionConfigData> data, List<PlatformModule> modules) {
        this.connectionDataList = data;
        this.modules = modules;
    }


    public void connectModule(PlatformModule module) throws IncorrectIONameException, ModuleNotDefinedException, IncorrectInputException {
        String moduleName = module.getName();

        for (ConnectionConfigData connectionData : connectionDataList) {
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

    private ModuleOutput findOutput(String outputName) throws ModuleNotDefinedException, IncorrectIONameException {
        PlatformModule module = findOutputModule(outputName);
        for (ModuleOutput output : module.getOutputs()) {
            if (outputName.equals(output.getName())) {
                return output;
            }
        }
        throw new ModuleNotDefinedException("No module could be found with defined output " + outputName);
    }

    private PlatformModule findOutputModule(String outputName) throws ModuleNotDefinedException, IncorrectIONameException {
        String moduleName = extractName(outputName);
        Optional<PlatformModule> module = modules.stream().filter(m -> m.getName().equals(moduleName))
                .findFirst();
        return module.orElseThrow(() -> new ModuleNotDefinedException("Output Module was not found"));
    }

}
