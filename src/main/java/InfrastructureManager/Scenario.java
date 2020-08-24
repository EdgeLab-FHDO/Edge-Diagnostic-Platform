package InfrastructureManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an scenario, as an object with a name and a list of events
 */
public class Scenario {

    private final String name;
    private final List<Event> eventList;

    public Scenario() { //Needed for Jackson
        this(null);
    }

    /**
     * Constructor of the class
     * @param name Name of the new scenario
     */
    public Scenario(String name) {
        this.name = name;
        this.eventList = new ArrayList<>();
    }

    /**
     * Get the name of the current scenario
     * @return Name of the scenario
     */
    public String getName() {
        return name;
    }

    /**
     * Get the event list of the current scenario
     * @return Event List of the scenario
     */
    public List<Event> getEventList() {
        return eventList;
    }

    /**
     * Add an event to the scenario
     * @param event Event to be added
     */
    public void addEvent(Event event) {
        this.eventList.add(event);
    }

    /**
     * Delete an Event from the scenario
     * @param index Index in the event list of the scenario to be deleted
     */
    public void deleteEvent(int index) {
        this.eventList.remove(index);
    }
}
