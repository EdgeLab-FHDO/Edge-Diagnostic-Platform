package InfrastructureManager;

import java.util.ArrayList;

public class Master {

    private final CommandSet commandSet;
    //private final MasterInput input;
    //private final MasterOutput output;
    private ArrayList<Runner> runnerList;
    private ArrayList<ScenarioRunner> runningScenarios;
    private static Master instance = null;

    private Master() {
        MasterConfigurator configurator = new MasterConfigurator();
        commandSet = configurator.getCommands();
        //input =configurator.getInput();
        //output = configurator.getOutput();
        runnerList = configurator.getRunners();
        runningScenarios = new ArrayList<>();
    }
    public String execute(String command) {
        return this.commandSet.getResponse(command);
    }

    public void exitAll() {
        for (Runner runner : runnerList) {
            runner.exit();
        }
    }

    //TODO:This could maybe go in configurator
    public void startMainRunner() {
        for (Runner runner : runnerList) {
            if (runner.getName().equals("Main")) {
                new Thread(runner,"MainRunner").start();
                break;
            }
        }
    }

    public void runScenario(Scenario scenario) {
        ScenarioRunner scenarioRunner = getRunner(scenario);
        if (runningScenarios.contains(scenarioRunner)) {
            scenarioRunner.exit();
            runningScenarios.remove(scenarioRunner);
        }
        scenarioRunner.setScenario(scenario);
        runningScenarios.add(scenarioRunner);
        new Thread(scenarioRunner).start();

    }
    public void pauseScenario(Scenario scenario) {
        ScenarioRunner scenarioRunner = getRunner(scenario);
        if (runningScenarios.contains(scenarioRunner)) {
            scenarioRunner.pause();
        }
    }
    public void resumeScenario(Scenario scenario) {
        ScenarioRunner scenarioRunner = getRunner(scenario);
        if (runningScenarios.contains(scenarioRunner)) {
            scenarioRunner.resume();
        }
    }

    private ScenarioRunner getRunner(Scenario scenario) {
        ScenarioRunner scenarioRunner;
        for (Runner runner : runnerList) {
            try {
                scenarioRunner = (ScenarioRunner) runner;
                if (scenarioRunner.getScenarioName().equalsIgnoreCase(scenario.getName())){
                    return scenarioRunner;
                }
            } catch (ClassCastException e) {
                continue;
            }
        }
        throw new IllegalArgumentException("There is no runner configured for the given scenario");
    }

    public static Master getInstance() {
        if (instance == null) {
            instance = new Master();
        }
        return instance;
    }

    public static void main(String[] args) {
        Master.getInstance().startMainRunner();
        /*
        Master master = Master.getInstance();
        String in;
        String mapping;
        while (true){
            in = master.fromInput();
            if (in.equals("exit")) {
                break;
            }
            mapping = master.execute(in);
            master.toOutput(mapping);
        }

         */
    }


}
