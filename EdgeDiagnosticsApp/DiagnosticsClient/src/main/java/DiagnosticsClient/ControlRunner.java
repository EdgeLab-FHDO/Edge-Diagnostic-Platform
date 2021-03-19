package DiagnosticsClient;

import DiagnosticsClient.Communication.ClientPlatformConnection;
import DiagnosticsClient.Control.InstructionManager;
import DiagnosticsClient.Control.RawData.Instruction;
import REST.Exception.RESTClientException;

public class ControlRunner extends AbstractRunner {

    private final ClientPlatformConnection connection;
    private final InstructionManager manager;
    private final InstructionQueue instructionQueue;
    private String lastInstruction;

    public ControlRunner(ClientPlatformConnection connection, InstructionQueue queue) {
        this.connection = connection;
        this.manager = new InstructionManager();
        this.instructionQueue = queue;
        this.lastInstruction = "";
    }

    @Override
    public void runnerOperation() throws InterruptedException {
        try {
            String instructionString = connection.getInstructions();
            if (!instructionString.equals(lastInstruction)) {
                Instruction instruction = manager.createInstruction(instructionString);
                instructionQueue.add(instruction);
                lastInstruction = instructionString;
            }
            Thread.sleep(1000);

        } catch (RESTClientException e) {
            e.printStackTrace();
        }
    }

}
