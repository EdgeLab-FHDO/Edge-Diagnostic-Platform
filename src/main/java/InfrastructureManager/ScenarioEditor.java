package InfrastructureManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class ScenarioEditor implements MasterOutput{
    private Scenario scenario;
    private final ObjectMapper mapper;

    public ScenarioEditor() {
        this.mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);;
    }
    @Override
    public void out(String response) {
        String[] command = response.split(" ");
        switch (command[0]) {
            case "create":
                create(command[1]);
                break;
            case "addEvent":
                addEvent(command[1], Integer.parseInt(command[2]));
                break;
            case "deleteEvent":
                deleteLastEvent();
                break;
            case "toFile" :
                scenarioToFile();
                break;
            case "fromFile":
                scenarioFromFile(command[1]);
                break;
            default:
                throw new IllegalArgumentException("Invalid command for ScenarioEditor");
        }
    }

    public void create(String name){
        scenario = new Scenario(name);
    }
    public void addEvent(String command, int time){
        scenario.addEvent(new Event(command,time));
    }
    public void deleteLastEvent(){
        int last = scenario.getEventList().size() - 1;
        scenario.deleteEvent(last);
    }
    public void scenarioToFile(){
        String path = "src/main/resources/scenarios/" + scenario.getName() + ".json";
        try {
            mapper.writeValue(new File(path), this.scenario);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void scenarioFromFile(String path){
        try {
            this.scenario = mapper.readValue(new File(path),Scenario.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Scenario getScenario() {
        return scenario;
    }
}
