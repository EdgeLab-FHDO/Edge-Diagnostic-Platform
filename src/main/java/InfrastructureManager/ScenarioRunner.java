package InfrastructureManager;

/**
 * Subclass of Runner specific to run scenarios
 */
public class ScenarioRunner extends Runner {

    private Scenario scenario;
    private final String scenarioName;
    private int currentEvent;

    /**
     * Constructor of the class, has no input parameter because input will be the events in the
     * scenario
     * @param name Name of the runner
     * @param scenarioName Name of the scenario that the runner is supposed to run
     * @param outputs 1 or more MasterOutput objects to be defined as outputs of the runner
     */
    public ScenarioRunner(String name,String scenarioName, MasterOutput...outputs) {
        super(name,null,outputs);
        this.scenarioName = scenarioName;
        this.scenario = null;
        this.currentEvent = 0;

        System.out.println("Scenario load input, copy and paste the line below so you don't have to look for them in wiki: \nload_scenario src/main/resources/scenarios/dummyScenario.json\nrun_scenario");
    }

    /**
     * Set the scenario of the runner
     * @param scenario Scenario to be run.
     */
    public void setScenario(Scenario scenario, long startTime) throws IllegalArgumentException{
        this.scenario = scenario;
        this.scenario.setStartTime(startTime);
    }

    /**
     * Get the name of the scenario, this runner is to run according to configuration
     * @return Name of the assigned scenario
     */
    public String getScenarioName() {
        return scenarioName;
    }


    /**
     * Overridden method in that goes in the run() method. Here, the execution of the master is
     * specific to run events as inputs. A different event in the list is run, each time this
     * method is called
     */
    @Override
    public void runOperation() {
        Event current = this.scenario.getEventList().get(currentEvent);
        waitForEvent(current);
        this.input = current;
        super.runOperation();
        currentEvent++;
        if (currentEvent == this.scenario.getEventList().size()) {
            exit();
        }
    }

    @Override
    public void exit() {
        currentEvent = 0;
        super.exit();
    }

    /**
     * Wait for executing the states according to their relative execution times
     * @param e Event to be waited for
     */
    private void waitForEvent (Event e) {
        long absoluteTime = this.scenario.getStartTime() + e.getExecutionTime();
        try {
            Thread.sleep(absoluteTime - System.currentTimeMillis());
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
