package InfrastructureManager;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an scenario, as an object with a name and a list of events
 */
public class Scenario {

    private final String name;
    private final List<Event> eventList;
    @JsonIgnore
    private long startTime;

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
        this.startTime = 0; //When a new scenario is created for file or command, start time in 0 (It will be rewritten when is run)
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
     * Set the start time of the scenario
     * @param startTime Absolute time in milliseconds (Since UNIX epoch)
     */
    public void setStartTime(long startTime) throws IllegalArgumentException {
        if (startTime >= System.currentTimeMillis()) {
            this.startTime = startTime;
        } else {
            throw new IllegalArgumentException("Start time in the past!");
        }
    }

    /**
     * Get the defined start time for the scenario
     * @return Defined absolute time in milliseconds (Since UNIX epoch)
     */
    public long getStartTime() {
        return startTime;
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
