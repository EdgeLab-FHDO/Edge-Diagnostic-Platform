package InfrastructureManager;

import InfrastructureManager.Configuration.CommandSet;
import InfrastructureManager.Configuration.MasterConfigurator;
import InfrastructureManager.ModuleManagement.PlatformModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Master Class of the Infrastructure Manager, singleton class
 */
public class Master {

    private static String configPath = "src/main/resources/Configuration.json";

    private final List<Runner> runnerList;
    private final List<Thread> runningThreads;
    private final List<PlatformModule> modules;

    private Thread mainThread;
    private Thread restThread;

    private static Master instance = null;

    /**
     * Constructor of the class
     * Gets the master configured according to the config file.
     */
    private Master() {
        MasterConfigurator configurator = new MasterConfigurator(configPath);
        runnerList = configurator.getRunners();
        modules = configurator.getModules();
        runningThreads = new ArrayList<>();
    }


    public String execute(String fromInput, CommandSet commands) {
        return commands.getResponse(fromInput);
    }

    /**
     * Method for exiting the program, by exiting each running runner
     */
    public void exitAll() {
        modules.forEach(PlatformModule::stop);
    }

    public void startAllModules() {
        modules.forEach(PlatformModule::start);
    }

    /**
     * Method for starting the main runner thread, declared in the config file
     */
    public void startMainRunner() {
        for (Runner runner : runnerList) {
            if (runner.getName().equals("Runner_console_in")) {
                mainThread = new Thread(runner,"MainRunner");
                mainThread.start();
                break;
            }
        }
    }

    /**
     * Method for starting the REST runner thread, declared in the config file
     */
    public void startRunnerThread(String name){
        for (Runner runner : runnerList) {
            if (runner.getName().equals(name)) {
                if (name.equals("RestServer") && restThread == null) {
                    restThread = new Thread(runner, "RestRunner");
                    restThread.start();
                    break;
                }
                new Thread(runner,name).start();
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
    public void runScenario(Scenario scenario, long startTime) {
        try {
            ScenarioRunner scenarioRunner = getRunner(scenario);
            if (!scenarioRunner.isRunning()) { //If running then leave it running
                scenarioRunner.setScenario(scenario, startTime);
                Thread t = new Thread(scenarioRunner, scenarioRunner.getName()); // Run the scenario in another thread
                runningThreads.add(t);
                t.start();
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
            } catch (Exception ignored) {}
        }
        throw new IllegalArgumentException("There is no runner configured for the given scenario");
    }

    public void startRunners() {
        for (Runner runner : runnerList) {
            if (runner.getName().equals("Runner_console_in")) {
                mainThread = new Thread(runner,"MainRunner");
                runningThreads.add(mainThread);
            } else if (runner.getClass() != ScenarioRunner.class) {
                runningThreads.add(new Thread(runner, runner.getName()));
            }
        }
        runningThreads.forEach(Thread::start);
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

    public static void changeConfigPath(String configPath) {
        Master.configPath = configPath;
    }

    public static void resetInstance() {
        instance = null;
    }

    public static void main(String[] args) {
        boolean autostart = true;
        if (args.length > 0) {
            List<String> argList = Arrays.asList(args);
            if (argList.contains("--autostart=false")){
                autostart = false;
            }
            try {
                if (argList.contains("-c")) {
                    Master.changeConfigPath(args[argList.indexOf("-c") + 1]);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("\"-c\" was used but no path was indicated for the config file");
            }
        }
        if (autostart) {
            Master.changeConfigPath("src/main/resources/NewConfiguration.json");
            Master.getInstance().startAllModules();
        }
        else {
            Master.getInstance().startMainRunner();
        }
        /*try {

            Master.getInstance().getMainThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
