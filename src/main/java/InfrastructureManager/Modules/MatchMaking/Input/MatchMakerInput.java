package InfrastructureManager.Modules.MatchMaking.Input;

import InfrastructureManager.ModuleManagement.ModuleInput;
import InfrastructureManager.Modules.MatchMaking.MatchesList;

public class MatchMakerInput extends ModuleInput {

    private final MatchesList sharedMatchesList;
    private String toSend;

    public MatchMakerInput(String name, MatchesList mapping) {
        super(name);
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

    private String waitForMatch() throws InterruptedException {
        return this.sharedMatchesList.getLastAdded();
    }
}
