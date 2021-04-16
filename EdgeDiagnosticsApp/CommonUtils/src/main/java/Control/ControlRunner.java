package Control;

import Communication.BasicPlatformConnection;
import Communication.Exception.RESTClientException;
import Control.Exception.InstructionCreationException;
import Control.Instruction.BasicInstructionManager;
import Control.Instruction.Instruction;
import Control.Instruction.InstructionQueue;
import RunnerManagement.AbstractRunner;

public class ControlRunner extends AbstractRunner {

    private final BasicPlatformConnection connection;
    private final BasicInstructionManager manager;
    private final InstructionQueue instructionQueue;

    public ControlRunner(BasicPlatformConnection connection, BasicInstructionManager manager, InstructionQueue queue) {
        this.connection = connection;
        this.manager = manager;
        this.instructionQueue = queue;
    }

    @Override
    public void runnerOperation() throws InterruptedException {
        try {
            String instructionString = connection.getInstructions();
            if (!instructionString.equals("")) {
                Instruction instruction =manager.createInstruction(instructionString);
                instructionQueue.add(instruction);
            }
            Thread.sleep(1000);
        } catch (RESTClientException | InstructionCreationException e) {
            e.printStackTrace();
            this.stop();
        }
    }

}
