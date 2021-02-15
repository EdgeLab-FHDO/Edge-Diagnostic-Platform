package InfrastructureManager.Modules.MatchMaking.Input;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformInput;
import InfrastructureManager.Modules.MatchMaking.MatchMakingModuleObject;

public class MatchMakerInput extends MatchMakingModuleObject implements PlatformInput {

    private String toSend;

    public MatchMakerInput(ImmutablePlatformModule module, String name) {
        super(module,name);
        toSend = "";
    }

    @Override
    public String read() throws InterruptedException {
        this.toSend = "give_node " + waitForMatch();
        String aux = this.toSend;
        this.toSend = "";
        return aux;
    }

    @Override
    public void response(ModuleExecutionException outputException) {}

    private String waitForMatch() throws InterruptedException {
        return this.getSharedList().getLastAdded();
    }
}
