package Control;

import Communication.BasicPlatformConnection;
import Communication.Exception.RESTClientException;
import Control.Exception.InstructionCreationException;
import Control.Instruction.BasicInstructionManager;
import Control.Instruction.Instruction;
import Control.Instruction.InstructionQueue;
import RunnerManagement.AbstractRunner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ControlRunner extends AbstractRunner {

    private final BasicPlatformConnection connection;
    private final BasicInstructionManager manager;
    private final InstructionQueue instructionQueue;
    private final ObjectMapper mapper;
    private int lastID;

    public ControlRunner(BasicPlatformConnection connection, BasicInstructionManager manager, InstructionQueue queue) {
        this.connection = connection;
        this.manager = manager;
        this.instructionQueue = queue;
        this.mapper = new ObjectMapper();
        this.lastID = 0;
    }

    @Override
    public void runnerOperation() throws InterruptedException {
        try {
            String instructionString = connection.getInstructions();
            if (!instructionString.equals("")) {
                JsonNode node = mapper.readTree(instructionString);
                int id = node.get("id").asInt();
                if (lastID != id) {
                    lastID = id;
                    JsonNode instructionNode = node.get("instruction");
                    String instructionAsAString = instructionNode.toString();
                    Instruction instruction =manager.createInstruction(instructionAsAString);
                    instructionQueue.add(instruction);
                }
            }
            Thread.sleep(1000);
        } catch (RESTClientException | InstructionCreationException | JsonProcessingException e) {
            e.printStackTrace();
            this.stop();
        }
    }

}
