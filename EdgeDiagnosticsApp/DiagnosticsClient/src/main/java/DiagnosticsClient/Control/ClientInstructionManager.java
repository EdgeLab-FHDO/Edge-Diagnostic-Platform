package DiagnosticsClient.Control;

import DiagnosticsClient.Control.RawData.ClientInstruction;
import Multithreading.BasicInstructionManager;
import Multithreading.Instruction;
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
    public Instruction createInstruction(String instructionJson) {
        try {
            instruction = mapper.readValue(instructionJson, ClientInstruction.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return instruction;
    }
}
