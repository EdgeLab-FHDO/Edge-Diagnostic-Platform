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
        if (response.contains("load scenario")) {
            this.path = response.replaceAll("load scenario ", "");
            response = "load scenario";
        }
        switch (response) {
            case "load scenario":
                scenarioFromFile();
                break;
            case "run scenario" :
                runScenario();
                break;
            case "pause scenario" :
                pauseScenario();
                break;
            default:
                throw new IllegalArgumentException("Invalid Command for ScenarioDispatcher!");
        }
    }

    private void pauseScenario() {

    }

    private void runScenario() {
        //TODO: Implement
        scenario.getEventList().forEach(e -> System.out.println(e.toString()));
    }

    private void scenarioFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.scenario = mapper.readValue(new File(path),Scenario.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
