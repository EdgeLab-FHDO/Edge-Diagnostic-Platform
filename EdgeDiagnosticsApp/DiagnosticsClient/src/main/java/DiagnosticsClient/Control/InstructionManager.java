package DiagnosticsClient.Control;

import DiagnosticsClient.Control.RawData.ClientInstruction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InstructionManager {

    private ClientInstruction instruction;
    private final ObjectMapper mapper;

    public InstructionManager() {
        mapper = new ObjectMapper();
        instruction = null;
    }

    public ClientInstruction createInstruction(String instructionJson) {
        try {
            instruction = mapper.readValue(instructionJson, ClientInstruction.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return instruction;
    }
}
