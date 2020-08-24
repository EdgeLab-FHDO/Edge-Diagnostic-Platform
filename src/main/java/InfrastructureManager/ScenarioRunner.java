package InfrastructureManager;

public class ScenarioRunner extends Runner {

    private Scenario scenario;
    private String scenarioName;
    private int currentEvent;

    public ScenarioRunner(String name,String scenarioName, MasterOutput...outputs) {
        //TODO: From configurator
        super(name,null,outputs);
        this.scenarioName = scenarioName;
        this.scenario = null;
        this.currentEvent = 0;
    }

    public void setScenario(Scenario scenario) {
        if (scenario.getName().equalsIgnoreCase(scenarioName)) {
            this.scenario = scenario;
        } else {
            throw new IllegalArgumentException("Scenario cant be assigned because of configuration");
        }
    }

    public String getScenarioName() {
        return scenarioName;
    }

    @Override
    public void runOperation() {
        this.input = this.scenario.getEventList().get(currentEvent);
        super.runOperation();
        currentEvent++;
        if (currentEvent == this.scenario.getEventList().size()) {
            currentEvent = 0;
            exit();
        }
        delay(2000); //Temporary, So each event "happens" slowly and can be paused
    }

    private void delay(int ms) {
        try {
            Thread.sleep(ms); //Just to see it sequentially in the console
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }
}
