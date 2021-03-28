package InfrastructureManager.Modules.Diagnostics.Exception;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

public class DiagnosticsModuleException extends ModuleExecutionException {
    public DiagnosticsModuleException(String message) {
        super(message);
    }

    public DiagnosticsModuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
