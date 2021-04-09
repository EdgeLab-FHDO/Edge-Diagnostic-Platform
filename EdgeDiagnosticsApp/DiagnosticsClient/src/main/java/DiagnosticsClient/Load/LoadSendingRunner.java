package DiagnosticsClient.Load;

import Communication.Exception.RESTClientException;
import Control.Instruction.Instruction;
import Control.Instruction.InstructionQueue;
import DiagnosticsClient.Communication.ClientPlatformConnection;
import DiagnosticsClient.Communication.ServerInformation;
import DiagnosticsClient.Control.ClientInstruction;
import Control.Instruction.InitialInstruction;
import DiagnosticsClient.Load.Exception.LoadSendingException;
import RunnerManagement.AbstractRunner;
import com.fasterxml.jackson.core.JsonProcessingException;

public class LoadSendingRunner extends AbstractRunner {

    private final ClientLoadManager manager;
    private final InstructionQueue instructionQueue;
    private int instructionCounter;
    private int experimentLength;
    private String experimentName;

    public LoadSendingRunner(ClientPlatformConnection connection, ServerInformation serverInformation, InstructionQueue instructionQueue) {
        this.manager = new ClientLoadManager(connection,serverInformation);
        this.instructionQueue = instructionQueue;
        this.experimentLength = 1;
        this.instructionCounter=0;
    }

    @Override
    public void runnerOperation() throws InterruptedException {
        try {
            manager.signalNextInstruction();
            Instruction instruction = instructionQueue.get();
            if (instruction.getClass().equals(InitialInstruction.class)) {
                InitialInstruction initialInstruction = (InitialInstruction) instruction;
                experimentLength = initialInstruction.getExperimentLength();
                experimentName = initialInstruction.getExperimentName();
                System.out.println("Starting " + experimentName);
                instructionCounter = 0;
            } else {
                ClientInstruction clientInstruction = (ClientInstruction) instruction;
                if (instructionCounter == 0) {
                    manager.setConnectionType(clientInstruction.getConnectionType());
                }
                manager.setSocketOptions(clientInstruction.getSocketOptions());
                manager.sendLoad(clientInstruction.getLoad(), clientInstruction.getConnection().getBufferInformation());
                instructionCounter++;
            }
            if (instructionCounter == experimentLength) {
                experimentLength = 1;
                instructionCounter = 0;
                manager.reportMeasurements(experimentName);
            }

            Thread.sleep(100);
        } catch (LoadSendingException | JsonProcessingException | RESTClientException e) {
            e.printStackTrace();
            this.stop();
        }
    }
}
