package InfrastructureManager.Modules.RemoteExecution;

import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.Modules.RemoteExecution.Input.NodeLimitInput;
import InfrastructureManager.Modules.RemoteExecution.Output.NodeLimitOutput;
import InfrastructureManager.Modules.RemoteExecution.Output.SSHClient;

public class RemoteExecutionModule extends PlatformModule {

    public RemoteExecutionModule(String name) {
        super(name);
        LimitList sharedList = new LimitList();
        setInputs(new NodeLimitInput(name + ".limit.in",sharedList));
        setOutputs(new SSHClient(name + ".ssh.out"),
                new NodeLimitOutput(name + ".limit.out", sharedList));
    }
}
