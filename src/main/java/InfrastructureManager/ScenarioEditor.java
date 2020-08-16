package InfrastructureManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ScenarioEditor implements MasterOutput{
    @Override
    public void out(String response) {
        //TODO: Implement
    }

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        String path = "src/main/resources/scenarios/dummyScenario.json";
        try {
            Scenario oe = mapper.readValue(new File(path), Scenario.class);
            System.out.println(oe.getName());
            oe.getEventList().forEach(e -> System.out.println(e.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
