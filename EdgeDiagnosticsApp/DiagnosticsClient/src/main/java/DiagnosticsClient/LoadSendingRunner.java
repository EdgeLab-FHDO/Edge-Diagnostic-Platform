package DiagnosticsClient;

import DiagnosticsClient.Communication.ClientPlatformConnection;
import DiagnosticsClient.Communication.ServerInformation;
import DiagnosticsClient.Control.RawData.ClientInstruction;
import DiagnosticsClient.Load.ClientLoadManager;
import DiagnosticsClient.Load.Exception.LoadSendingException;
import DiagnosticsClient.Load.Exception.TCP.ServerNotSetUpException;
import Multithreading.AbstractRunner;
import Multithreading.InstructionQueue;
import REST.Exception.RESTClientException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class LoadSendingRunner extends AbstractRunner {

    private final ClientLoadManager manager;
    private final InstructionQueue instructionQueue;

    public LoadSendingRunner(ClientPlatformConnection connection, ServerInformation serverInformation, InstructionQueue instructionQueue) throws ServerNotSetUpException {
        this.manager = new ClientLoadManager(connection,serverInformation);
        this.instructionQueue = instructionQueue;
    }

    @Override
    public void runnerOperation() throws InterruptedException {
        try {
            ClientInstruction instruction = (ClientInstruction) instructionQueue.get();
            manager.setSocketOptions(instruction.getSocketOptions());
            manager.setConnectionType(instruction.getConnectionType());
            manager.sendLoad(instruction.getLoad());
            manager.reportMeasurements();
            Thread.sleep(100);
        } catch (LoadSendingException | JsonProcessingException | RESTClientException e) {
            e.printStackTrace();
        }
    }
}
