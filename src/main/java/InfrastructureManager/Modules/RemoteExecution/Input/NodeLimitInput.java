package InfrastructureManager.Modules.RemoteExecution.Input;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ModuleInput;
import InfrastructureManager.Modules.RemoteExecution.LimitList;

public class NodeLimitInput extends ModuleInput {

    private final LimitList sharedList;
    private String toSend;

    public NodeLimitInput(String name, LimitList list) {
        super(name);
        this.sharedList = list;
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
