package DiagnosticsClient.Control;

import Control.Exception.InstructionCreationException;
import Control.Instruction.BasicInstructionManager;
import Control.Instruction.Instruction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientInstructionManager implements BasicInstructionManager {

    private Instruction instruction;
    private final ObjectMapper mapper;

    public ClientInstructionManager() {
        mapper = new ObjectMapper();
        instruction = null;
    }

    @Override
    public Instruction createInstruction(String instructionJson) throws InstructionCreationException {
        try {
            instruction = mapper.readValue(instructionJson, ClientInstruction.class);
        } catch (JsonProcessingException e) {
            throw new InstructionCreationException(e);
        }
        return instruction;
    }
}
