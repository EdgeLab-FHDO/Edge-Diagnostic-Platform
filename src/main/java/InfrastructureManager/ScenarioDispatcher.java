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
public class ScenarioDispatcher extends MasterOutput {

    private Scenario scenario;
    private static final int DEFAULT_DELAY = 1000; //1 second default delay

    public ScenarioDispatcher(String name) {
        super(name);
        this.scenario = null;
    }

    /**
     * Based on responses from the master executes the different functionalities
     * @param response Must be in the way "dispatcher command" and additionally:
     *                 - Loading Scenario : Should include the path of the file (dispatcher fromFile src/resources/scenario.json)
     *                 - Running Scenario : If command is only "dispatcher run" the scenario is run right away, if the "-d" flag is
     *                 used, a delayed start time can be given using either the "-r" flag for a relative time or "-a" flag for
     *                 an absolute time input (Milliseconds since UNIX epoch), followed by the desired time in milliseconds
     *                 - Other functionalities : Just the command.
     * @throws IllegalArgumentException If the command is not defined or is missing arguments
     */
    @Override
    public void out(String response) {
        String[] command = response.split(" ");
        if (command[0].equals("dispatcher")) {
            try {
                switch (command[1]) {
                    case "fromFile":
                        scenarioFromFile(command[2]);
                        break;
                    case "run" :
                        if (command.length > 2 && command[2].equals("-d")) {
                            long startTime = 0;
                            switch (command[3]) {
                                case "-r":
                                    startTime += System.currentTimeMillis();
                                    break;
                                case "-a":
                                    break;
                                default:
                                    throw new IllegalArgumentException("Invalid run command");
                            }
                            startTime += Long.parseLong(command[4]);
                            runScenario(startTime);
                        } else {
                            runScenario(System.currentTimeMillis() + DEFAULT_DELAY);
                        }
                        break;
                    case "pause" :
                        pauseScenario();
                        break;
                    case "resume" :
                        resumeScenario();
                        break;
                    case "stop" :
                        stopScenario();
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid command for ScenarioDispatcher");
                }
            } catch (IndexOutOfBoundsException e){
                throw new IllegalArgumentException("Arguments missing for command  - ScenarioDispatcher");
            }
        }
    }

    /**
     * Method for stopping the current scenario
     */
    private void stopScenario() {
        Master.getInstance().stopScenario(this.scenario);
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
    private void runScenario(long startTime) {
        Master.getInstance().runScenario(this.scenario, startTime);
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

    /**
     * Get the scenario assigned to this dispatcher
     * @return Scenario to which all action will be performed
     */
    public Scenario getScenario() { //FOR TESTS
        return scenario;
    }
}
