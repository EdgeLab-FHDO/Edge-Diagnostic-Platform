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
    protected String getSingleReading() {
        String aux = this.toSend;
        this.toSend = "";
        return aux;
    }

    @Override
    protected void storeSingleReading(String reading) {
        this.toSend = reading;
    }

    @Override
    protected String getReading() throws InterruptedException {
        waitForMatch();
        return super.getReading();
    }

    @Override
    public String read() throws InterruptedException {
        return getReading();
    }

    private void waitForMatch() throws InterruptedException {
        String reading = this.sharedMatchesList.getLastAdded();
        this.storeReadingAndUnblock("give_node " + reading);
    }
}
