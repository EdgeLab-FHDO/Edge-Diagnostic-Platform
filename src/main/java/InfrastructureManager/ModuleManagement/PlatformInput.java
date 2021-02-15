package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.PlatformObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public interface PlatformInput {

    String getName();
    String read() throws InterruptedException;
    void response(ModuleExecutionException outputException);


}
