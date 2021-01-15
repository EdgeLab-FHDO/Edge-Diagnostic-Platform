package InfrastructureManager;

import InfrastructureManager.Configuration.CommandSet;
import InfrastructureManager.Configuration.MasterConfigurator;
import InfrastructureManager.ModuleManagement.Exception.ModuleManagerException;
import InfrastructureManager.ModuleManagement.Exception.ModuleNotFoundException;
import InfrastructureManager.ModuleManagement.PlatformModule;
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
        MasterConfigurator configurator = new MasterConfigurator(configPath);
        try {
            modules = configurator.getModules();
        } catch (ModuleManagerException e) {
            e.printStackTrace();
            System.exit(-1);
        }
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

    public void startModule(String moduleName) {
        try {
            findModuleByName(moduleName).start();
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void pauseModule(String moduleName) {
        try {
            PlatformModule moduleToPause = findModuleByName(moduleName);
            if (moduleToPause.getState() == PlatformModule.ModuleState.RUNNING) {
                moduleToPause.pause();
            }
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void pauseAllModules() {
        modules.stream().filter(m -> m.getState() == PlatformModule.ModuleState.RUNNING)
                .forEach(PlatformModule::pause);
    }

    public void resumeModule(String moduleName) {
        try {
            PlatformModule moduleToResume = findModuleByName(moduleName);
            if (moduleToResume.getState() == PlatformModule.ModuleState.PAUSED) {
                moduleToResume.resume();
            }
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
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
    public void runScenario(Scenario scenario, long startTime) {
        try {
            ScenarioModule scenarioModule = getScenarioModule(scenario);
            scenarioModule.startScenario(startTime);
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop a running ScenarioRunner, given the scenario that is running
     * @param scenario Running scenario to be stopped
     */
    public void stopScenario(Scenario scenario) {
        try {
            ScenarioModule scenarioModule = getScenarioModule(scenario);
            scenarioModule.stopScenario();
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to pause a running ScenarioRunner, given the scenario that is running
     * @param scenario Running scenario to be paused
     */
    public void pauseScenario(Scenario scenario) {
        try {
            ScenarioModule scenarioModule = getScenarioModule(scenario);
            scenarioModule.pauseScenario();
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to resume a paused ScenarioRunner, given its scenario
     * @param scenario Paused scenario to be resumed
     */
    public void resumeScenario(Scenario scenario) {
        try {
            ScenarioModule scenarioModule = getScenarioModule(scenario);
            scenarioModule.resumeScenario();
        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }
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
            Master.changeConfigPath("src/main/resources/NewConfiguration.json");
            Master.getInstance().startAllModules();
        }
        else {
            Master.getInstance().startModule("console");
        }
    }
}
