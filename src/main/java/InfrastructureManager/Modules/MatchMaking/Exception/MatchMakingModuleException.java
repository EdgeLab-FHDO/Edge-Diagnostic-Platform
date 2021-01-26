package InfrastructureManager.Modules.MatchMaking.Exception;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;

public class MatchMakingModuleException extends ModuleExecutionException {
    public MatchMakingModuleException(String errorMessage){
        super(errorMessage);
    }

    public MatchMakingModuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
