package InfrastructureManager.Modules.MatchMaking.Input;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformInput;
import InfrastructureManager.Modules.MatchMaking.MatchesList;

public class MatchMakerInput extends PlatformInput {

    private final MatchesList sharedMatchesList;
    private String toSend;

    public MatchMakerInput(ImmutablePlatformModule module, String name, MatchesList mapping) {
        super(module,name);
        toSend = "";
        this.sharedMatchesList = mapping;
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
        return this.sharedMatchesList.getLastAdded();
    }
}