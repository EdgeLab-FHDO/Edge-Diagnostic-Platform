package InfrastructureManager.Modules.RemoteExecution;

import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.RemoteExecution.Input.NodeLimitInput;
import InfrastructureManager.Modules.RemoteExecution.Output.NodeLimitOutput;
import InfrastructureManager.Modules.RemoteExecution.Output.SSHClient;

public class RemoteExecutionModule extends PlatformModule {

    public RemoteExecutionModule() {
        super();
        LimitList sharedList = new LimitList(this);
        this.addGlobalVariable("limit_list", sharedList);
    }

    @Override
    public void configure(ModuleConfigData data) {
        String name = data.getName();
        setName(name);
        setInputs(new NodeLimitInput(this, name + ".limit.in"));
        setOutputs(new SSHClient(this,name + ".ssh.out"),
                new NodeLimitOutput(this,name + ".limit.out"));
    }
}
