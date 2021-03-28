package InfrastructureManager.Modules.Diagnostics.Exception.Instruction;

import InfrastructureManager.Modules.Diagnostics.Exception.DiagnosticsModuleException;

public class InstructionHandlingException extends DiagnosticsModuleException {
    public InstructionHandlingException(String message) {
        super(message);
    }

    public InstructionHandlingException(String message, Throwable cause) {
        super(message, cause);
    }
}
