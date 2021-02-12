package InfrastructureManager.Modules.RemoteExecution;

import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.ModuleManagement.RawData.ModuleConfigData;
import InfrastructureManager.Modules.RemoteExecution.Input.NodeLimitInput;
import InfrastructureManager.Modules.RemoteExecution.Output.NodeLimitOutput;
import InfrastructureManager.Modules.RemoteExecution.Output.SSHClient;

public class RemoteExecutionModule extends PlatformModule implements GlobalVarAccessREModule {

    private final LimitList sharedList;

    public RemoteExecutionModule() {
        super();
        this.sharedList = new LimitList(this);
    }

    @Override
    public void configure(ModuleConfigData data) {
        String name = data.getName();
        setName(name);
        setInputs(new NodeLimitInput(this, name + ".limit.in"));
        setOutputs(new SSHClient(this,name + ".ssh.out"),
                new NodeLimitOutput(this,name + ".limit.out"));
    }

    @Override
    public LimitList getLimitList() {
        return sharedList;
    }
}
