package InfrastructureManager;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/*
public class ScenarioEditorTests {
    ScenarioEditor editor = new ScenarioEditor();
    final String scenarioName = "testScenario";
    @Test
    public void createTest() {
        editor.out("create " + scenarioName);
        Assert.assertNotNull(editor.getScenario());
    }
    @Test
    public void addEventTest() {
        editor.out("create " + scenarioName);
        editor.out("addEvent test_event0 0");
        String expected = "Event: test_event0, Time: 0";
        StringBuilder result = new StringBuilder();
        for (Event e : editor.getScenario().getEventList()) {
            result.append(e.toString());
        }
        Assert.assertEquals(result.toString(),expected);
    }
    @Test
    public void addAnotherEventTest() {
        editor.out("create " + scenarioName);
        editor.out("addEvent test_event0 0");
        editor.out("addEvent test_event1 1");
        String expected = "Event: test_event0, Time: 0" +
                "Event: test_event1, Time: 1";
        StringBuilder result = new StringBuilder();
        for (Event e : editor.getScenario().getEventList()) {
            result.append(e.toString());
        }
        Assert.assertEquals(result.toString(),expected);
    }
    @Test
    public void deleteEventTest() {
        editor.out("create " + scenarioName);
        editor.out("addEvent test_event0 0");
        editor.out("addEvent test_event1 1");
        editor.out("deleteEvent");
        String expected = "Event: test_event0, Time: 0";
        StringBuilder result = new StringBuilder();
        for (Event e : editor.getScenario().getEventList()) {
            result.append(e.toString());
        }
        Assert.assertEquals(result.toString(),expected);
    }
    @Test
    public void saveToFileTest(){
        editor.out("create " + scenarioName);
        editor.out("addEvent test_event0 0");
        editor.out("addEvent test_event1 1");
        editor.out("toFile src/test/resources/");
        File scenarioFile = new File("src/test/resources/" + scenarioName + ".json");
        Assert.assertTrue(scenarioFile.exists());
    }
    @Test
    public void loadFromFileTest() {
        editor.out("fromFile src/test/resources/dummyScenario.json");
        String expected = "Event: deploy_application, Time: 0" +
                "Event: node_request, Time: 1" +
                "Event: update_gui, Time: 2";
        StringBuilder result = new StringBuilder();
        for (Event e : editor.getScenario().getEventList()) {
            result.append(e.toString());
        }
        Assert.assertEquals(result.toString(),expected);
    }
}*/
