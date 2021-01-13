package InfrastructureManager.Modules.RemoteExecution.Input;

import InfrastructureManager.MasterInput;

public class NodeLimitInput extends MasterInput {

    public NodeLimitInput(String name) {
        super(name);
    }

    @Override
    protected String getSingleReading() {
        return null;
    }

    @Override
    protected void storeSingleReading(String reading) {

    }

    @Override
    public String read() throws InterruptedException {
        return null;
    }
}
