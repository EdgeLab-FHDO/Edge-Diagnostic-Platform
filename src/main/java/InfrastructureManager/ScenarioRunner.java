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
    }

    /**
     * Set the scenario of the runner
     * @param scenario Scenario to be runned
     */
    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    /**
     * Get the name of the scenario, this runner is to run according to configuration
     * @return Name of the assigned scenario
     */
    public String getScenarioName() {
        return scenarioName;
    }

    /**
     * Overriden method in that goes in the run() method. Here, the execution of the master is
     * specific to run events as inputs. A different event in the list is run, each time this
     * method is called
     */
    @Override
    public void runOperation() {
        this.input = this.scenario.getEventList().get(currentEvent);
        super.runOperation();
        currentEvent++;
        if (currentEvent == this.scenario.getEventList().size()) {
            exit();
        }
        //delay(2000); //Temporary, So right each event "happens" slowly and the scenario can be paused
    }

    @Override
    public void exit() {
        currentEvent = 0;
        super.exit();
    }

    /**
     * Method for making the current thread wait for a given time
     * @param ms Time (in milliseconds) the thread should wait
     */
    private void delay(int ms) {
        try {
            Thread.sleep(ms); //Just to see it sequentially in the console
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }
}
