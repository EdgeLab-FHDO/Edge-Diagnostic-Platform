package InfrastructureManager;

import java.util.ArrayList;
import java.util.List;

public class Scenario {

    private final String name;
    private final List<Event> eventList;

    public Scenario() {
        this(null);
    }
    public Scenario(String name) {
        this.name = name;
        this.eventList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void addEvent(Event event) {
        this.eventList.add(event);
    }

    public void deleteEvent(int index) {
        this.eventList.remove(index);
    }
}
