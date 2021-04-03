package Control;

import Communication.BasicPlatformConnection;
import Communication.Exception.RESTClientException;
import Control.Exception.InstructionCreationException;
import Control.Instruction.BasicInstructionManager;
import Control.Instruction.InitialInstruction;
import Control.Instruction.Instruction;
import Control.Instruction.InstructionQueue;
import RunnerManagement.AbstractRunner;

public class ControlRunner extends AbstractRunner {

    private final BasicPlatformConnection connection;
    private final BasicInstructionManager manager;
    private final InstructionQueue instructionQueue;
    private String lastInstruction;
    private int length;
    private int instructionCounter;

    public ControlRunner(BasicPlatformConnection connection, BasicInstructionManager manager, InstructionQueue queue) {
        this.connection = connection;
        this.manager = manager;
        this.instructionQueue = queue;
        this.lastInstruction = "";
        this.length = 1;
        this.instructionCounter = 0;
    }

    @Override
    public void runnerOperation() throws InterruptedException {
        try {
            String instructionString = connection.getInstructions();
            if (!instructionString.equals("")) {
                Instruction instruction =manager.createInstruction(instructionString);
                instructionQueue.add(instruction);
                if (instruction.getClass().equals(InitialInstruction.class)) {
                    length = ((InitialInstruction) instruction).getExperimentLength();
                } else {
                    instructionCounter++;
                }
                if (instructionCounter == length) {
                    lastInstruction = instructionString;
                    length = 1;
                    instructionCounter = 0;
                }
            }
            Thread.sleep(1000);
        } catch (RESTClientException | InstructionCreationException e) {
            e.printStackTrace();
            this.stop();
        }
    }

}
