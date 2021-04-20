package InfrastructureManager.Modules.RemoteExecution.Input;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformInput;
import InfrastructureManager.Modules.RemoteExecution.LimitList;
import InfrastructureManager.Modules.RemoteExecution.RemoteExecutionModuleObject;

/**
 * Class that allows to give the limit information created using a {@link InfrastructureManager.Modules.RemoteExecution.Output.NodeLimitOutput}
 * as input to the platform, in order for it to be routed to an output.
 *
 * To communicate with the NodeLimitOutput instance, uses a shared {@link LimitList} in which the output writes limits
 * and instances of this class read it and pass it.
 *
 */
public class NodeLimitInput extends RemoteExecutionModuleObject implements PlatformInput {

    private final LimitList sharedList;
    private String toSend;

    /**
     * Constructor of the class. Creates a new NodeLimitInput.
     *
     * @param module Owner module of this input
     * @param name   Name of this input. Normally hardcoded "MODULE_NAME.limit.in"
     */
    public NodeLimitInput(ImmutablePlatformModule module, String name) {
        super(module,name);
        this.sharedList = this.getLimitList();
        this.toSend = null;
    }

    /**
     * Waits for the {@link LimitList} to unblock and return a value
     * @return The value returned from the LimitList after unblocking
     * @throws InterruptedException If interrupted while waiting.
     */
    protected String waitForList() throws InterruptedException {
        this.getLogger().debug(this.getName(),"Waiting for the Limit List to unblock and return a value");
        return sharedList.getListAsBody();
    }

    /**
     * Implementation of the {@link PlatformInput} interface. It returns the complete shared {@link LimitList}.
     *
     * Blocks until a value is put into the list to return something.
     * @return Predefined command "set_limits" + JSON representation of the LimitList
     * @throws InterruptedException If interrupted while blocked
     */
    @Override
    public String read() throws InterruptedException {
        toSend = "set_limits " + waitForList();
        String aux = toSend;
        toSend = "";
        return aux;
    }

    /**
     * Ignores exceptions in the execution process.
     * @param outputException Exception happening in the platform's execution process. This value is null if the process went correctly (No exception was raised)
     */
    @Override
    public void response(ModuleExecutionException outputException) {}
}
