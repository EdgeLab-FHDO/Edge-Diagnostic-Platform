package InfrastructureManager.Modules.RemoteExecution;

import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.RemoteExecution.Input.NodeLimitInput;
import InfrastructureManager.Modules.RemoteExecution.Output.NodeLimitOutput;
import InfrastructureManager.Modules.RemoteExecution.Output.SSHClient;

/**
 * {@link PlatformModule} that adds remote execution functionality to the platform.
 *
 * This module provides the platform with the functionality of client and node lifecycle management,
 * by allowing remote execution, as well as resource control in nodes.
 *
 * It provides one NodeLimitInput input hardcoded with the name ".limit.in", one NodeLimitOutput (named ".limit.out") and
 *  one SSHOutput (named ".ssh.out").
 *
 * @see <a href="https://github.com/EdgeLab-FHDO/Edge-Diagnostic-Platform/wiki/Remote-Execution-Module">Wiki Entry</a>
 */
public class RemoteExecutionModule extends PlatformModule implements GlobalVarAccessREModule {

    private final LimitList sharedList;

    /**
     * Creates a new RemoteExecutionModule.
     */
    public RemoteExecutionModule() {
        super();
        this.sharedList = new LimitList(this);
    }

    /**
     * Based on raw module data, configures the module to use it. In this case, extracts the name and creates the input and
     * outputs with their predefined names.
     * @param data Raw module data.
     */
    @Override
    public void configure(ModuleConfigData data) {
        String name = data.getName();
        setName(name);
        setInputs(new NodeLimitInput(this, name + ".limit.in"));
        setOutputs(new SSHClient(this,name + ".ssh.out"),
                new NodeLimitOutput(this,name + ".limit.out"));
    }

    /**
     * Returns the shared limit list used for interaction between {@link NodeLimitInput} and {@link NodeLimitOutput} instances.
     *
     * This method can only be accessed if the reference to this module is through the {@link GlobalVarAccessREModule} interface.
     *
     * @return the shared limit list
     */
    @Override
    public LimitList getLimitList() {
        return sharedList;
    }
}
