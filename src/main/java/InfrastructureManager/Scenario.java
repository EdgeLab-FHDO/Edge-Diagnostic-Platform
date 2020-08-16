package InfrastructureManager;

import java.util.ArrayList;
import java.util.List;

public class Scenario {
    private String name;
    private List<Event> eventList;

    public Scenario() {
        this.name = null;
        this.eventList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Event> getEventList() {
        return eventList;
    }
}
