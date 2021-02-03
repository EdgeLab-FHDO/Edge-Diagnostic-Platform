package InfrastructureManager.Modules.Scenario;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.ModuleOutput;
import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.Modules.Scenario.Exception.Input.InvalidTimeException;
import InfrastructureManager.Modules.Scenario.Exception.Input.OwnerModuleNotSetUpException;
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
    private final ScenarioModule ownerScenarioModule;
    private static final int DEFAULT_DELAY = 1000; //1 second default delay

    public ScenarioDispatcher(ImmutablePlatformModule module, String name, Scenario scenario) {
        super(module,name);
        this.scenario = scenario;
        this.ownerScenarioModule = (ScenarioModule) module;
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
     */
    @Override
    public void execute(String response) throws ScenarioDispatcherException, InvalidTimeException, OwnerModuleNotSetUpException {
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
    private void stopScenario() {
        this.ownerScenarioModule.stopScenario();
    }

    /**
     * Method for pausing the scenario
     */
    private void pauseScenario() {
        this.ownerScenarioModule.pauseScenario();
    }

    /**
     * Method to resume the scenario if paused
     */
    private void resumeScenario() {
        this.ownerScenarioModule.resumeScenario();
    }

    /**
     * Method to run the scenario
     */
    private void runScenario(long startTime) throws InvalidTimeException, OwnerModuleNotSetUpException {
        this.getOwnerModule().getDebugInput().debug("starting scenario " + this.scenario.getName());
        this.ownerScenarioModule.startScenario(startTime);
    }


    /**
     * Get the scenario assigned to this dispatcher
     * @return Scenario to which all action will be performed
     */
    public Scenario getScenario() { //FOR TESTS
        return scenario;
    }
}
