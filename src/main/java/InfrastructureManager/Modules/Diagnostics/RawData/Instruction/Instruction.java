package InfrastructureManager.Modules.Diagnostics.RawData.Instruction;

public class Instruction {
    private final String clientInstruction;
    private final String serverInstruction;

    public Instruction(String clientInstruction, String serverInstruction) {
        this.clientInstruction = clientInstruction;
        this.serverInstruction = serverInstruction;
    }

    public String getClientInstruction() {
        return clientInstruction;
    }

    public String getServerInstruction() {
        return serverInstruction;
    }
}
