package DiagnosticsClient.Control;

import DiagnosticsClient.Control.RawData.Instruction;
import DiagnosticsClient.Load.ClientSocketOptions;
import DiagnosticsClient.Load.LoadTypes.DiagnosticsLoad;
import LoadManagement.BasicLoadManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InstructionManager {

    private Instruction instruction;
    private final ObjectMapper mapper;

    public InstructionManager() {
        mapper = new ObjectMapper();
        instruction = null;
    }

    public Instruction createInstruction(String instructionJson) {
        try {
            instruction = mapper.readValue(instructionJson,Instruction.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return instruction;
    }
}
