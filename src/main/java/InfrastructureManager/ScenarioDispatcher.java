package InfrastructureManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ScenarioDispatcher implements MasterOutput {
    private Scenario scenario;
    private String path;

    public ScenarioDispatcher() {
        this.scenario = null;
        this.path = "";
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
        System.out.println("Pause Function");
    }

    private void runScenario() {
        //TODO: Implement
        System.out.println("Run Function");
        scenario.getEventList().forEach(e -> System.out.println(e.toString()));
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
