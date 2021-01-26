package InfrastructureManager.Modules.AdvantEDGE.Exception;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;


/**
 * Signals different errors in execution of the {@link InfrastructureManager.Modules.AdvantEDGE.AdvantEdgeModule}
 */
public class AdvantEdgeModuleException extends ModuleExecutionException {
    public AdvantEdgeModuleException(String message) {
        super(message);
    }

    public AdvantEdgeModuleException(String message, Throwable cause) {
        super(message, cause);
    }

}
