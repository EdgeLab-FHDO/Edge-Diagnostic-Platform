package InfrastructureManager;

import InfrastructureManager.Configuration.Exception.ConfigurationException;
import InfrastructureManager.Configuration.MasterConfigurator;
import InfrastructureManager.ModuleManagement.Exception.Creation.ModuleManagerException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleNotFoundException;
import InfrastructureManager.ModuleManagement.PlatformModule;
import InfrastructureManager.Modules.Scenario.Exception.Input.InvalidTimeException;
import InfrastructureManager.Modules.Scenario.Scenario;
import InfrastructureManager.Modules.Scenario.ScenarioModule;

import java.util.Arrays;
import java.util.List;

/**
 * Master Class of the Infrastructure Manager, singleton class
 */
public class Master {

    private static String configPath = "src/main/resources/Configuration.json";
    private List<PlatformModule> modules;

    private static Master instance = null;

    /**
     * Constructor of the class
     * Gets the master configured according to the config file.
     */
    private Master() {
        try {
            MasterConfigurator configurator = new MasterConfigurator(configPath);
            modules = configurator.getModules();
        } catch (ModuleManagerException | ConfigurationException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Method for exiting the program, by exiting each running runner
     */
    public void exitAll() {
        modules.forEach(PlatformModule::stop);
    }

    public void stopModule(String moduleName) throws ModuleNotFoundException {
        findModuleByName(moduleName).stop();
    }

    public void startAllModules() {
        modules.forEach(PlatformModule::start);
    }

    public void startModule(String moduleName) throws ModuleNotFoundException {
        findModuleByName(moduleName).start();
    }

    public void pauseModule(String moduleName) throws ModuleNotFoundException {
        PlatformModule moduleToPause = findModuleByName(moduleName);
        if (moduleToPause.getState() == PlatformModule.ModuleState.RUNNING) {
            moduleToPause.pause();
        }
    }

    public void pauseAllModules() {
        modules.stream().filter(m -> m.getState() == PlatformModule.ModuleState.RUNNING)
                .forEach(PlatformModule::pause);
    }

    public void resumeModule(String moduleName) throws ModuleNotFoundException {
        PlatformModule moduleToResume = findModuleByName(moduleName);
        if (moduleToResume.getState() == PlatformModule.ModuleState.PAUSED) {
            moduleToResume.resume();
        }
    }

    public void resumeAllModules() {
        modules.stream().filter(m -> m.getState() == PlatformModule.ModuleState.PAUSED)
                .forEach(PlatformModule::resume);
    }

    private PlatformModule findModuleByName(String moduleName) throws ModuleNotFoundException {
        return modules.stream().filter(m -> m.getName()
                .equals(moduleName))
                .findFirst()
                .orElseThrow(() -> new ModuleNotFoundException("Module " + moduleName + " was not found"));
    }

    /**
     * Method that given a certain Scenario, runs the corresponding ScenarioRunner and adds
     * it to the list of running Runners. If called on a running ScenarioRunner, it will restart
     * it
     * @param scenario Scenario to be run
     */
    public void runScenario(Scenario scenario, long startTime) throws InvalidTimeException, ModuleNotFoundException {
        ScenarioModule scenarioModule = getScenarioModule(scenario);
        scenarioModule.startScenario(startTime);
    }

    /**
     * Stop a running ScenarioRunner, given the scenario that is running
     * @param scenario Running scenario to be stopped
     */
    public void stopScenario(Scenario scenario) throws ModuleNotFoundException {
        ScenarioModule scenarioModule = getScenarioModule(scenario);
        scenarioModule.stopScenario();
    }

    /**
     * Method to pause a running ScenarioRunner, given the scenario that is running
     * @param scenario Running scenario to be paused
     */
    public void pauseScenario(Scenario scenario) throws ModuleNotFoundException {
        ScenarioModule scenarioModule = getScenarioModule(scenario);
        scenarioModule.pauseScenario();
    }

    /**
     * Method to resume a paused ScenarioRunner, given its scenario
     * @param scenario Paused scenario to be resumed
     */
    public void resumeScenario(Scenario scenario) throws ModuleNotFoundException {
        ScenarioModule scenarioModule = getScenarioModule(scenario);
        scenarioModule.resumeScenario();
    }

    private ScenarioModule getScenarioModule(Scenario scenario) throws ModuleNotFoundException {
        String scenarioName = scenario.getName();
        String moduleName = scenarioName.substring(0,scenarioName.indexOf('.'));
        return (ScenarioModule) findModuleByName(moduleName);
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
            //Master.changeConfigPath("src/main/resources/NewConfiguration.json");
            Master.changeConfigPath("src/test/resources/REST/RESTTestConfiguration.json");
            Master.getInstance().startAllModules();
        }
        else {
            try {
                Master.getInstance().startModule("console");
            } catch (ModuleNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
