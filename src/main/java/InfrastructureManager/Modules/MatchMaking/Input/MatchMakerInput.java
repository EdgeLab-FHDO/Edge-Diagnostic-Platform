package InfrastructureManager.Modules.MatchMaking.Input;

import InfrastructureManager.MasterInput;
import InfrastructureManager.Modules.MatchMaking.MatchMakerType;
import InfrastructureManager.Modules.MatchMaking.MatchesDoneList;

public class MatchMakerInput extends MasterInput {

    private final MatchMakerType type;
    private MatchesDoneList sharedMatchesList;
    private String toSend;

    public MatchMakerInput(String name, MatchMakerType type, MatchesDoneList mapping) {
        super(name);
        this.type = type;
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
    public String read() throws InterruptedException {
        return getReading();
    }

    private void waitForMatch() {
        //TODO: Implement with shared protected list
    }
}
