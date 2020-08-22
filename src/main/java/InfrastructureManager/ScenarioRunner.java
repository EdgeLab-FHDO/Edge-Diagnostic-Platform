package InfrastructureManager;

public class ScenarioRunner extends Runner {

    private final Scenario scenario;
    private int currentEvent;

    public ScenarioRunner(Scenario scenario) {
        //TODO: From configurator
        super(null,new ConsoleOutput());
        this.scenario = scenario;
        this.currentEvent = 0;
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
