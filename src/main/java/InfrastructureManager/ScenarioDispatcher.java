package InfrastructureManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ScenarioDispatcher implements MasterOutput {
    private Scenario scenario;
    private ScenarioRunner runner;
    public ScenarioDispatcher() {
        this.scenario = null;
    }
    @Override
    public void out(String response) {
        String[] command = response.split(" ");
        if (command[0].equals("dispatcher")){
            switch (command[1]) {
                case "fromFile":
                    scenarioFromFile(command[2]);
                    break;
                case "run" :
                    runScenario();
                    break;
                case "pause" :
                    pauseScenario();
                    break;
                case "resume" :
                    resumeScenario();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid Command for ScenarioDispatcher!");
            }
        }
    }

    private void pauseScenario() {
        this.runner.pause();
    }
    private void resumeScenario() {
        this.runner.resume();
    }

    private void runScenario() {
        Master.getInstance().runScenario(this.scenario);
    }

    private void scenarioFromFile(String path){
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.scenario = mapper.readValue(new File(path),Scenario.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRunner(ScenarioRunner runner) {
        this.runner = runner;
    }
}
