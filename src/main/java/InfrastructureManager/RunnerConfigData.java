package InfrastructureManager;

import java.util.List;

public class RunnerConfigData {

    private String name;
    private boolean scenario;
    private String input;
    private String[] outputs;

    public RunnerConfigData() {
        name = null;
        scenario = false;
        input = null;
        outputs = null;
    }

    public String getName() {
        return name;
    }

    public boolean isScenario() {
        return scenario;
    }

    public String getInput() {
        return input;
    }

    public String[] getOutputs() {
        return outputs;
    }
}
