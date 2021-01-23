package InfrastructureManager.ModuleManagement;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class ModuleInput {

    private final String name;
    @JsonIgnore
    private Runner runner;

    public ModuleInput(String name) {
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
