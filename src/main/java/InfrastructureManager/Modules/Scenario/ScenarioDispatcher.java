package InfrastructureManager.Modules.Scenario;

import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformOutput;
import InfrastructureManager.Modules.Scenario.Exception.Input.InvalidTimeException;
import InfrastructureManager.Modules.Scenario.Exception.Input.OwnerModuleNotSetUpException;
import InfrastructureManager.Modules.Scenario.Exception.Output.ScenarioDispatcherException;
import InfrastructureManager.Modules.Scenario.Exception.ScenarioNotSetUpException;

/**
 * Class that represent Scenario lifecycle management as a platform output.
 *
 * This type of output is used for executing (including running, pausing and stopping) the scenario of its owner module by controlling its execution.
 *
 * It provides the following functionalities:
 *
 * - Running a Scenario
 * - Pausing a running Scenario
 * - Resuming a paused Scenario
 * - Stopping an Scenario's execution
 */
public class ScenarioDispatcher extends ScenarioModuleObject implements PlatformOutput {

    private final ScenarioModule ownerScenarioModule;
    private static final int DEFAULT_DELAY = 1000; //1 second default delay

    /**
     * Constructor of the class. Creates a new Scenario dispatcher.
     *
     * @param module Owner module of this output
     * @param name   Name of this output. Normally hardcoded "MODULE_NAME.dispatcher"
     */
    public ScenarioDispatcher(ImmutablePlatformModule module, String name) {
        super(module,name);
        this.ownerScenarioModule = (ScenarioModule) module;
    }


    /**
     * Based on processed responses from the inputs executes the different functionalities
     *
     * @param response Must be in the way "dispatcher COMMAND" and additionally:
     *                 - Running Scenario : If command is only "dispatcher run" the scenario is run right away, if the "-d" flag is
     *                 used, a delayed start time can be given using either the "-r" flag for a relative time or "-a" flag for
     *                 an absolute time input (Milliseconds since UNIX epoch), followed by the desired time in milliseconds
     *                 - Other functionalities : Just the command.
     * @throws ScenarioDispatcherException  If the command passed is invalid or incomplete
     * @throws InvalidTimeException         If when running the scenario there is a problem with the timing
     * @throws OwnerModuleNotSetUpException If the Scenario's owner module was not correctly configured in its instantiation.
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
            } catch (ScenarioNotSetUpException e) {
                throw  new ScenarioDispatcherException("Scenario could not be accessed in the module");
            }
        }
    }

    /**
     * Method for stopping the scenario
     *
     * @throws ScenarioNotSetUpException If the owner module does not have a runner configured for its scenario
     */
    private void stopScenario() throws ScenarioNotSetUpException {
        this.ownerScenarioModule.stopScenario();
    }

    /**
     * Method for pausing the scenario
     *
     * @throws ScenarioNotSetUpException If the owner module does not have a runner configured for its scenario.
     */
    private void pauseScenario() throws ScenarioNotSetUpException {
        this.ownerScenarioModule.pauseScenario();
    }

    /**
     * Method to resume the scenario if paused
     *
     * @throws ScenarioNotSetUpException If the owner module does not have a runner configured for its scenario.
     */
    private void resumeScenario() throws ScenarioNotSetUpException {
        this.ownerScenarioModule.resumeScenario();
    }

    /**
     * Method to run the scenario
     *
     * @param startTime The start time of the scenario. This is an absolute time input (Milliseconds since UNIX epoch)
     * @throws InvalidTimeException         If the starting time is in the past when compared with the time when this method was called
     * @throws OwnerModuleNotSetUpException If the Owner module of the scenario was not set up during instantiation
     */
    private void runScenario(long startTime) throws InvalidTimeException, OwnerModuleNotSetUpException {
        this.ownerScenarioModule.startScenario(startTime);
    }
}
