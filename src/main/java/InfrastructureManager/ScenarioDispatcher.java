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
            case "resume" :
                resumeScenario();
                break;
            default:
                throw new IllegalArgumentException("Invalid Command for ScenarioDispatcher!");
        }
    }

    private void pauseScenario() {
        this.runner.pause();
    }
    private void resumeScenario() {
        this.runner.resume();
    }

    private void runScenario() {
        Thread runThread = new Thread(this.runner);
        runThread.start();
    }

    private void scenarioFromFile(String path){
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.scenario = mapper.readValue(new File(path),Scenario.class);
            this.runner = new ScenarioRunner(this.scenario);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
