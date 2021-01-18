package InfrastructureManager.Modules.RemoteExecution.Input;

import InfrastructureManager.ModuleManagement.ModuleInput;
import InfrastructureManager.Modules.RemoteExecution.LimitList;

public class NodeLimitInput extends ModuleInput {

    private final LimitList sharedList;
    private String bodyToSend;

    public NodeLimitInput(String name, LimitList list) {
        super(name);
        this.sharedList = list;
        this.bodyToSend = null;
    }


    @Override
    protected String getSingleReading() {
        return bodyToSend;
    }

    @Override
    protected void storeSingleReading(String reading) {
        bodyToSend = reading;
    }

    @Override
    protected String getReading() throws InterruptedException {
        waitForList();
        return super.getReading();
    }

    protected void waitForList() throws InterruptedException {
        String reading = sharedList.getListAsBody();
        super.storeReadingAndUnblock(reading);
    }

    @Override
    public String read() throws InterruptedException {
        return "set_limits " + getReading();
    }

}
