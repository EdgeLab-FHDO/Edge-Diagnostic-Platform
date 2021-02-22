package InfrastructureManager.ModuleManagement;

import InfrastructureManager.Configuration.CommandSet;
import InfrastructureManager.ModuleManagement.Exception.Creation.IncorrectIONameException;
import InfrastructureManager.ModuleManagement.Exception.Creation.IncorrectInputException;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleNotDefinedException;
import InfrastructureManager.ModuleManagement.RawData.ConnectionConfigData;

import java.util.List;
import java.util.Optional;

/**
 * Class that is in charge of connecting {@link PlatformModule} objects, based on connection raw data.
 *
 * In a more concrete manner, creates {@link Connection} objects based on the raw data and adds them to each PlatformModule.
 * It does so by searching for the module where the connection's input is provided and adding a connection related to that input
 * in said module.
 */
public class ModuleConnector {
    private final List<ConnectionConfigData> connectionDataList;
    private final List<PlatformModule> modules;

    /**
     * Constructor of the class.
     * @param data Raw connection data
     * @param modules Modules of the platform to connect. These are already created by a {@link ModuleFactory}.
     */
    public ModuleConnector(List<ConnectionConfigData> data, List<PlatformModule> modules) {
        this.connectionDataList = data;
        this.modules = modules;
    }


    /**
     * Adds connection to a module based on its inputs. For each input, all of the defined connections in the raw data will be created and passed to the module.
     * @param module Module to connect
     * @throws IncorrectIONameException If in the raw data, the input or output in a connection was defined with a wrong naming convention.
     * @throws ModuleNotDefinedException If a raw connection is defined using a module whose name does not correspond to one of the existing modules.
     * @throws IncorrectInputException If a raw connection is defined using an input name that does not correspond to the predefined input names of this module.
     */
    public void connectModule(PlatformModule module) throws IncorrectIONameException, ModuleNotDefinedException, IncorrectInputException {
        String moduleName = module.getName();

        for (ConnectionConfigData connectionData : connectionDataList) {
            if (getInputModuleName(connectionData).equals(moduleName)) {
                PlatformOutput output = findOutput(connectionData.getOut());
                CommandSet commandSet = new CommandSet();
                commandSet.set(connectionData.getCommands());
                Connection connection = new Connection(output,commandSet);
                module.addConnection(connectionData.getIn(),connection);
            }
        }
    }

    /**
     * Finds the name of the module, whose input is defined in a raw Connection
     * @param connectionData Raw connection representation
     * @return Name of the module which has the input defined in the connection
     * @throws IncorrectIONameException If the input is named in a wrong manner.
     */
    private String getInputModuleName(ConnectionConfigData connectionData) throws IncorrectIONameException {
        return extractName(connectionData.getIn());
    }

    /**
     * Based on the name of an input or output, extract the name of the module
     * @param IOName Name of input or output
     * @return Name of the owner module
     * @throws IncorrectIONameException If the input or output is named in a wrong manner.
     */
    private String extractName(String IOName) throws IncorrectIONameException {
        int index = IOName.indexOf('.');
        if (index != -1) {
            return IOName.substring(0,IOName.indexOf('.'));
        } else {
            throw new IncorrectIONameException(IOName + " is not a valid name for a module IO");
        }
    }

    /**
     * Find an output based on its name.
     * @param outputName Name of the output to locate
     * @return A PlatformOutput corresponding to the name
     * @throws ModuleNotDefinedException If no module is defined to have an output with this name.
     * @throws IncorrectIONameException If the output is named in a wrong manner.
     */
    private PlatformOutput findOutput(String outputName) throws ModuleNotDefinedException, IncorrectIONameException {
        PlatformModule module = findOutputModule(outputName);
        for (PlatformOutput output : module.getOutputs()) {
            if (outputName.equals(output.getName())) {
                return output;
            }
        }
        throw new ModuleNotDefinedException("No module could be found with defined output " + outputName);
    }

    /**
     * Based on the name of an output, find the module that has it defined.
     * @param outputName Name of an output
     * @return Module which has the searched output
     * @throws ModuleNotDefinedException If no module is defined to have that output (with that name)
     * @throws IncorrectIONameException If the output is named in a wrong manner.
     */
    private PlatformModule findOutputModule(String outputName) throws ModuleNotDefinedException, IncorrectIONameException {
        String moduleName = extractName(outputName);
        Optional<PlatformModule> module = modules.stream().filter(m -> m.getName().equals(moduleName))
                .findFirst();
        return module.orElseThrow(() -> new ModuleNotDefinedException("Output Module was not found"));
    }

}
