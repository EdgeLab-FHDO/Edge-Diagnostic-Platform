package InfrastructureManager;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Scenario implements MasterInput{
    @JsonIgnore
    private int currentEvent;
    private String name;
    private List<Event> eventList;

    public Scenario() {
        this.name = null;
        //TODO: Is it the better implementation?
        this.eventList = new ArrayList<>();
        this.currentEvent = 0;
    }
    public Scenario(String name) {
        this.name = name;
        this.eventList = new ArrayList<>();
        this.currentEvent = 0;
    }

    @Override
    public String read() {
        if (currentEvent < eventList.size()) {
            this.currentEvent++;
            return eventList.get(currentEvent-1).read();
        } else {
            currentEvent = 0;
            return "exit";
        }

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
