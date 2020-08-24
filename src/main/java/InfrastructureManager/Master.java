package InfrastructureManager;

import java.util.ArrayList;

public class Master {

    private final CommandSet commandSet;
    //private final MasterInput input;
    //private final MasterOutput output;
    private ArrayList<Runner> runnerList;
    private static Master instance = null;

    private Master() {
        MasterConfigurator configurator = new MasterConfigurator();
        commandSet = configurator.getCommands();
        //input =configurator.getInput();
        //output = configurator.getOutput();
        runnerList = configurator.getRunners();
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
        /*
        TODO: Search in the runner list, for the scenario runner with the
         scenario name assigned matching the passed scenario. If found,
         start a new thread and run it. This method will be called from the
         mainrunner only.
         */
        ScenarioRunner scenarioRunner;
        for (Runner runner : runnerList) {
            scenarioRunner = (ScenarioRunner) runner;
            if (scenarioRunner.getScenarioName().equalsIgnoreCase(scenario.getName())){
                new Thread(scenarioRunner).start();
                break;
            } else {
                throw new IllegalArgumentException("There is no runner configured for the given scenario");
            }
        }

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
