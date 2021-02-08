package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.PlatformObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"runner", "ownerModule"})
public abstract class PlatformInput extends PlatformObject {

    private final String name;
    private Runner runner;

    public PlatformInput(ImmutablePlatformModule ownerModule, String name) {
        super(ownerModule);
        this.name = name;
    }

    public void setRunner(Runner runner) {
        this.runner = runner;
    }

    public Runner getRunner() {
        return runner;
    }

    public String getName() {
        return name;
    }

    public abstract String read() throws InterruptedException;
    public abstract void response(ModuleExecutionException outputException);
}
