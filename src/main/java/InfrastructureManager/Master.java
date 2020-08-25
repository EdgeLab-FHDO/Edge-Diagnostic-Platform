package InfrastructureManager;

import java.util.ArrayList;

/**
 * Master Class of the Infrastructure Manager, singleton class
 */
public class Master {

    private final CommandSet commandSet;
    private final ArrayList<Runner> runnerList;
    private Thread mainThread;

    private static Master instance = null;

    /**
     * Constructor of the class
     * Gets the master configured according to the config file.
     */
    private Master() {
        MasterConfigurator configurator = new MasterConfigurator();
        commandSet = configurator.getCommands();
        runnerList = configurator.getRunners();
    }

    /**
     * Based on the command set predefined, execute a command
     * @param command Command to be executed coming from the input
     * @return Response going to the output(s)
     */
    public String execute(String command) {
        return this.commandSet.getResponse(command);
    }

    /**
     * Method for exiting the program, by exiting each running runner
     */
    public void exitAll() {
        for (Runner runner : runnerList) {
            if (runner.isRunning()){
                runner.exit();
            }
        }
    }

    /**
     * Method for starting the main runner thread, declared in the config file
     */
    public void startMainRunner() {
        for (Runner runner : runnerList) {
            if (runner.getName().equals("Main")) {
                mainThread = new Thread(runner,"MainRunner");
                mainThread.start();
                break;
            }
        }
    }

    public Thread getMainThread() {
        return mainThread;
    }

    /**
     * Method that given a certain Scenario, runs the corresponding ScenarioRunner and adds
     * it to the list of running Runners. If called on a running ScenarioRunner, it will restart
     * it
     * @param scenario Scenario to be run
     */
    public void runScenario(Scenario scenario) {
        try {
            ScenarioRunner scenarioRunner = getRunner(scenario);
            if (!scenarioRunner.isRunning()) { //If running then leave it running
                scenarioRunner.setScenario(scenario);
                new Thread(scenarioRunner).start(); // Run the scenario in another thread
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop a running ScenarioRunner, given the scenario that is running
     * @param scenario Running scenario to be stopped
     */
    public void stopScenario(Scenario scenario) {
        try {
            ScenarioRunner scenarioRunner = getRunner(scenario);
            if (scenarioRunner.isRunning()) {
                scenarioRunner.exit();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to pause a running ScenarioRunner, given the scenario that is running
     * @param scenario Running scenario to be paused
     */
    public void pauseScenario(Scenario scenario) {
        try {
            ScenarioRunner scenarioRunner = getRunner(scenario);
            if (scenarioRunner.isRunning()) {
                scenarioRunner.pause();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to resume a paused ScenarioRunner, given its scenario
     * @param scenario Paused scenario to be resumed
     */
    public void resumeScenario(Scenario scenario) {
        try {
            ScenarioRunner scenarioRunner = getRunner(scenario);
            if (scenarioRunner.isPaused()) {
                scenarioRunner.resume();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for finding a ScenarioRunner in the Runner list, based on the scenario is
     * supposed to run according to the configuration file
     * @param scenario Scenario for which to find the ScenarioRunner
     * @return ScenarioRunner that will run the given scenario
     * @throws IllegalArgumentException If there is no scenarioRunner configured to run the
     * given scenario
     */
    private ScenarioRunner getRunner(Scenario scenario) throws IllegalArgumentException {
        ScenarioRunner scenarioRunner;
        for (Runner runner : runnerList) {
            try {
                scenarioRunner = (ScenarioRunner) runner;
                if (scenarioRunner.getScenarioName().equalsIgnoreCase(scenario.getName())){
                    return scenarioRunner;
                }
            } catch (Exception e) {
                continue;
            }
        }
        throw new IllegalArgumentException("There is no runner configured for the given scenario");
    }

    /**
     * Singleton method for getting the only instance of the class
     * @return The instance of the Master Class
     */
    public static Master getInstance() {
        if (instance == null) {
            instance = new Master();
        }
        return instance;
    }

    public static void main(String[] args) {
        Master.getInstance().startMainRunner();
        try {
            Master.getInstance().getMainThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
