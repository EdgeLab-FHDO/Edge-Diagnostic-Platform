package DiagnosticsServer.Control;

import DiagnosticsServer.Control.RawData.ServerInstruction;
import Multithreading.BasicInstructionManager;
import Multithreading.Instruction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerInstructionManager implements BasicInstructionManager {
    private Instruction instruction;
    private final ObjectMapper mapper;

    public ServerInstructionManager() {
        this.instruction = null;
        this.mapper = new ObjectMapper();
    }

    @Override
    public Instruction createInstruction(String jsonRepresentation) {
        try {
            instruction = mapper.readValue(jsonRepresentation, ServerInstruction.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return instruction;
    }
}
