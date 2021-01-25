package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"runner", "ownerModule"})
public abstract class ModuleInput {

    private final String name;
    private Runner runner;
    private PlatformModule ownerModule;

    public ModuleInput(PlatformModule ownerModule,String name) {
        this.ownerModule = ownerModule;
        this.name = name;
    }

    protected void setOwnerModule(PlatformModule ownerModule) {
        this.ownerModule = ownerModule;
    }

    protected PlatformModule getOwnerModule() {
        return this.ownerModule;
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
