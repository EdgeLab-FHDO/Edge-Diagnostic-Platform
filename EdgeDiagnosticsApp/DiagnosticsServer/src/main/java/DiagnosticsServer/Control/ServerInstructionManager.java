package DiagnosticsServer.Control;

import DiagnosticsServer.Control.RawData.ServerInstruction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerInstructionManager {
    private ServerInstruction instruction;
    private final ObjectMapper mapper;

    public ServerInstructionManager() {
        this.instruction = null;
        this.mapper = new ObjectMapper();
    }

    public ServerInstruction createInstruction(String jsonRepresentation) {
        try {
            instruction = mapper.readValue(jsonRepresentation, ServerInstruction.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return instruction;
    }
}
