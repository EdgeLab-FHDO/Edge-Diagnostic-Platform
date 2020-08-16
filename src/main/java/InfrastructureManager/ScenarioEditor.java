package InfrastructureManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ScenarioEditor implements MasterOutput{
    private Scenario scenario;
    private final ObjectMapper mapper;

    public ScenarioEditor() {
        this.mapper = new ObjectMapper();
    }
    @Override
    public void out(String response) {
        //TODO: Implement
    }

    public void create(){
        scenario = new Scenario();
    }
    public void addEvent(String command, int time){
        scenario.getEventList().add(new Event(command,time));
    }
    public void deleteEvent(){
        int last = scenario.getEventList().size() - 1;
        scenario.getEventList().remove(last);
    }
    public void scenarioToFile(String path){
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

}
