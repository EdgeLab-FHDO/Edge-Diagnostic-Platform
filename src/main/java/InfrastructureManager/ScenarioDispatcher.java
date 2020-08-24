package InfrastructureManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Scenario Handling class that is an Output of the master
 * Allows for handling scenarios in the following ways :
 * - Load scenarios from JSON files
 * - Run Scenarios
 * - Pause/Resume Scenarios
 */
public class ScenarioDispatcher implements MasterOutput {

    private Scenario scenario;

    public ScenarioDispatcher() {
        this.scenario = null;
    }

    /**
     * Based on responses from the master executes the different functionalities
     * @param response Must be in the way "dispatcher command" and additionally:
     *                 - Loading Scenario : Should include the path of the file (dispatcher fromFile src/resources/scenario.json)
     *                 - Other functionalities : Just the command.
     * @throws IllegalArgumentException If the command is not defined
     */
    @Override
    public void out(String response) {
        String[] command = response.split(" ");
        if (command[0].equals("dispatcher") && command.length > 1) {
            switch (command[1]) {
                case "fromFile":
                    scenarioFromFile(command[2]);
                    break;
                case "run" :
                    runScenario();
                    break;
                case "pause" :
                    pauseScenario();
                    break;
                case "resume" :
                    resumeScenario();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid Command for ScenarioDispatcher!");
            }
        }
    }

    /**
     * Method for pausing the scenario
     */
    private void pauseScenario() {
        Master.getInstance().pauseScenario(this.scenario);
    }

    /**
     * Method to resume the scenario if paused
     */
    private void resumeScenario() {
        Master.getInstance().resumeScenario(this.scenario);
    }

    /**
     * Method to run the scenario
     */
    private void runScenario() {
        Master.getInstance().runScenario(this.scenario);
    }

    /**
     * Method for loading a scenario from a JSON file
     * @param path Path of the file
     */
    private void scenarioFromFile(String path){
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.scenario = mapper.readValue(new File(path),Scenario.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
