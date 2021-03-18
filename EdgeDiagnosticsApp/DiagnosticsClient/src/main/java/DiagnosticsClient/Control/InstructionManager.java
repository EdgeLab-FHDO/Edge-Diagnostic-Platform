package DiagnosticsClient.Control;

import DiagnosticsClient.Control.RawData.Instruction;
import DiagnosticsClient.Load.ClientSocketOptions;
import DiagnosticsClient.Load.LoadTypes.DiagnosticsLoad;
import LoadManagement.BasicLoadManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InstructionManager {

    private Instruction instruction;

    public InstructionManager(String instructionJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            instruction = mapper.readValue(instructionJson,Instruction.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public DiagnosticsLoad getLoad() {
        return this.instruction.getLoad();
    }

    public BasicLoadManager.ConnectionType getConnectionType() {
        return this.instruction.getConnection().getType();
    }

    public ClientSocketOptions getSocketOptions() {
        return this.instruction.getConnection().getSocketOptions();
    }
}
