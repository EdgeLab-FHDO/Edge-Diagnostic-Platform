package InfrastructureManager.Configuration;

/**
 * Raw data representing a Runner object. Objects of this class are meant to be created
 * by reading a JSON File
 */
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

    /**
     * Get the name of the runner
     * @return Name of the runner
     */
    public String getName() {
        return name;
    }

    /**
     * Get if the runner should be an ScenarioRunner or not
     * @return True if the runner is an ScenarioRunner, false otherwise
     */
    public boolean isScenario() {
        return scenario;
    }

    /**
     * Get the defined input for the runner (raw)
     * @return String representing the input to be assigned to the runner
     */
    public String getInput() {
        return input;
    }

    /**
     * Get the defined outputs for the runner (raw)
     * @return Array of string objects representing the outputs to be assigned to the runner
     */
    public String[] getOutputs() {
        return outputs;
    }
}
