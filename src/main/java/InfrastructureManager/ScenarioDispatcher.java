package InfrastructureManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ScenarioDispatcher implements MasterOutput {
    private Scenario scenario;

    public ScenarioDispatcher() {
        this.scenario = null;
    }
    @Override
    public void out(String response) {
        String[] command = response.split(" ");
        switch (command[0]) {
            case "fromFile":
                scenarioFromFile(command[1]);
                break;
            case "run" :
                runScenario();
                break;
            case "pause" :
                pauseScenario();
                break;
            default:
                throw new IllegalArgumentException("Invalid Command for ScenarioDispatcher!");
        }
    }

    private void pauseScenario() {
        //TODO: Implement
    }

    private void runScenario() {
        Master master = Master.getInstance();
        for (Event e : this.scenario.getEventList()) {
            String command = e.read(); //So it will not require runtime changes in config
            String mapping = master.execute(command);
            System.out.println(mapping); //TODO: Figure out the output
            try {
                Thread.sleep(1000); //Just to see it sequentially in the console
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }

    }

    private void scenarioFromFile(String path){
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.scenario = mapper.readValue(new File(path),Scenario.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
