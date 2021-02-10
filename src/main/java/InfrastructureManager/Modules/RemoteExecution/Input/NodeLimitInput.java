package InfrastructureManager.Modules.RemoteExecution.Input;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformInput;
import InfrastructureManager.Modules.RemoteExecution.LimitList;

public class NodeLimitInput extends PlatformInput {

    private final LimitList sharedList;
    private String toSend;

    public NodeLimitInput(ImmutablePlatformModule module, String name) {
        super(module,name);
        this.sharedList = (LimitList) this.getOwnerModuleWithGlobalVar().getResource("limit_list");
        this.toSend = null;
    }

    protected String waitForList() throws InterruptedException {
        return sharedList.getListAsBody();
    }

    @Override
    public String read() throws InterruptedException {
        toSend = "set_limits " + waitForList();
        String aux = toSend;
        toSend = "";
        return aux;
    }

    @Override
    public void response(ModuleExecutionException outputException) {}
}
