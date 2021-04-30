package Control.Instruction;

import LoadManagement.BasicLoadManager;
import LoadManagement.LoadType;

public class InitialInstruction implements Instruction {

    private final String experimentName;
    private final int experimentLength;

    public InitialInstruction() {
        this.experimentLength = 0;
        this.experimentName = "";
    }

    public int getExperimentLength() {
        return experimentLength;
    }

    public String getExperimentName() {
        return experimentName;
    }

    @Override
    public LoadType getLoadType() {
        return null;
    }

    @Override
    public BasicLoadManager.ConnectionType getConnectionType() {
        return null;
    }
}
