package InfrastructureManager.Modules.Diagnostics.Exception.Instruction;

public class InstructionFileLoadingException extends InstructionHandlingException {
    public InstructionFileLoadingException(String message) {
        super(message);
    }

    public InstructionFileLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
