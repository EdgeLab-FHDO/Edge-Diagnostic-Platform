package InfrastructureManager.Modules.Scenario;

import InfrastructureManager.Master;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.ModuleManagement.ModuleOutput;
import InfrastructureManager.Modules.Scenario.Exception.Input.InvalidTimeException;
import InfrastructureManager.Modules.Scenario.Exception.Output.ScenarioDispatcherException;

/**
 * Scenario Handling class that is an Output of the master
 * Allows for handling scenarios in the following ways :
 * - Load scenarios from JSON files
 * - Run Scenarios
 * - Pause/Resume Scenarios
 */
public class ScenarioDispatcher extends ModuleOutput {

    private final Scenario scenario;
    private static final int DEFAULT_DELAY = 1000; //1 second default delay

    public ScenarioDispatcher(String name, Scenario scenario) {
        super(name);
        this.scenario = scenario;
    }


    /**
     * Based on responses from the master executes the different functionalities
     * @param response Must be in the way "dispatcher command" and additionally:
     *                 - Loading Scenario : Should include the path of the file (dispatcher fromFile src/resources/scenario.json)
     *                 - Running Scenario : If command is only "dispatcher run" the scenario is run right away, if the "-d" flag is
     *                 used, a delayed start time can be given using either the "-r" flag for a relative time or "-a" flag for
     *                 an absolute time input (Milliseconds since UNIX epoch), followed by the desired time in milliseconds
     *                 - Other functionalities : Just the command.
     * @throws ScenarioDispatcherException If the command passed is invalid or incomplete
     * @throws InvalidTimeException If when running the scenario there is a problem with the timing
     * @throws ModuleNotFoundException If the scenario to be run doesn't have an underlying module assigned
     */
    @Override
    public void out(String response) throws ScenarioDispatcherException, InvalidTimeException, ModuleNotFoundException {
        String[] command = response.split(" ");
        if (command[0].equals("dispatcher")) {
            try {
                switch (command[1]) {
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
                                    throw new ScenarioDispatcherException("Invalid run command " + command[3]);
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
                        throw new ScenarioDispatcherException("Invalid command " + command[1]
                                + " for ScenarioDispatcher");
                }
            } catch (IndexOutOfBoundsException e){
                throw new ScenarioDispatcherException("Arguments missing for command " + response
                        + " to ScenarioDispatcher");
            }
        }
    }

    /**
     * Method for stopping the current scenario
     */
    private void stopScenario() throws ModuleNotFoundException {
        Master.getInstance().stopScenario(this.scenario);
    }

    /**
     * Method for pausing the scenario
     */
    private void pauseScenario() throws ModuleNotFoundException {
        Master.getInstance().pauseScenario(this.scenario);
    }

    /**
     * Method to resume the scenario if paused
     */
    private void resumeScenario() throws ModuleNotFoundException {
        Master.getInstance().resumeScenario(this.scenario);
    }

    /**
     * Method to run the scenario
     */
    private void runScenario(long startTime) throws InvalidTimeException, ModuleNotFoundException {
        Master.getInstance().runScenario(this.scenario, startTime);
    }


    /**
     * Get the scenario assigned to this dispatcher
     * @return Scenario to which all action will be performed
     */
    public Scenario getScenario() { //FOR TESTS
        return scenario;
    }
}
