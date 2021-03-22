package DiagnosticsClient;

import DiagnosticsClient.Communication.ServerInformation;
import DiagnosticsClient.Control.RawData.ClientInstruction;
import DiagnosticsClient.Load.ClientLoadManager;
import DiagnosticsClient.Load.Exception.LoadSendingException;
import DiagnosticsClient.Load.Exception.TCP.ServerNotSetUpException;

public class LoadRunner extends AbstractRunner {

    private final ClientLoadManager manager;
    private final InstructionQueue instructionQueue;

    public LoadRunner(ServerInformation serverInformation, InstructionQueue instructionQueue) throws ServerNotSetUpException {
        this.manager = new ClientLoadManager(serverInformation);
        this.instructionQueue = instructionQueue;
    }

    @Override
    public void runnerOperation() throws InterruptedException {
        try {
            ClientInstruction instruction = instructionQueue.get();
            manager.setSocketOptions(instruction.getSocketOptions());
            manager.setConnectionType(instruction.getConnectionType());
            manager.sendLoad(instruction.getLoad());
            Thread.sleep(100);
        } catch (LoadSendingException e) {
            e.printStackTrace();
        }
    }
}
