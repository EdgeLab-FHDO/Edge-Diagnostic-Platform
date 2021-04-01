package DiagnosticsServer.Control;

import Control.Exception.InstructionCreationException;
import Control.Instruction.BasicInstructionManager;
import Control.Instruction.InitialInstruction;
import Control.Instruction.Instruction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerInstructionManager implements BasicInstructionManager {
    private Instruction instruction;
    private final ObjectMapper mapper;

    public ServerInstructionManager() {
        this.instruction = null;
        this.mapper = new ObjectMapper();
    }

    @Override
    public Instruction createInstruction(String jsonRepresentation) throws InstructionCreationException {
        try {
            JsonNode tree = mapper.readTree(jsonRepresentation);
            if (tree.has("experimentLength")) {
                instruction = mapper.treeToValue(tree, InitialInstruction.class);
            } else {
                instruction = mapper.readValue(jsonRepresentation, ServerInstruction.class);
            }
        } catch (JsonProcessingException e) {
            throw new InstructionCreationException(e);
        }
        return instruction;
    }
}
