package InfrastructureManager.Modules.Scenario;

import InfrastructureManager.ModuleManagement.ModuleInput;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an scenario, as an object with a name and a list of events
 */
@JsonIgnoreProperties({"startTime","current", "pausedTime", "resumedTime","started"})
public class Scenario extends ModuleInput {

    private final List<Event> eventList;
    private long startTime;
    private int currentIndex;
    private long pausedTime;
    private long resumedTime;
    private boolean started;

    /**
     * Constructor of the class
     * @param name Name of the new scenario
     */
    public Scenario(@JsonProperty("name") String name) {
        super(name + ".scenario");
        this.eventList = new ArrayList<>();
        this.startTime = 0; //When a new scenario is created for file or command, start time in 0 (It will be rewritten when is run)
        this.currentIndex = 0;
        this.pausedTime = 0L;
        this.resumedTime = 0L;
        this.started = false;
    }

    @Override
    public String read() throws InterruptedException {
        if (!started) {
            this.block();
        }
        if (currentIndex < eventList.size()) {
            Event currentEvent = eventList.get(currentIndex);
            currentIndex++;
            return waitForEvent(currentEvent);
        } else {
            stop();
            return null;
        }
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

    public void start() {
        currentIndex = 0;
        this.started = true;
        this.unblock();
    }

    public void pause() {
        this.pausedTime += System.currentTimeMillis();
        this.getRunner().pause();
    }

    public void resume() {
        this.resumedTime += System.currentTimeMillis();
        this.getRunner().resume();
    }

    public void stop() {
        this.pausedTime = 0L;
        this.resumedTime = 0L;
        currentIndex = 0;
        this.started = false;
        this.getRunner().exit();
        System.out.println("FINISHED SCENARIO: " + this.getName());
    }
    /**
     * Wait for executing the states according to their relative execution times
     * @param e Event to be waited for
     */
    private String waitForEvent (Event e) {
        long absoluteTime = this.getStartTime() + (resumedTime - pausedTime) + e.getExecutionTime();
        try {
            Thread.sleep(absoluteTime - System.currentTimeMillis());
        } catch (InterruptedException ie) {
            stop();
        }
        return e.getCommand();
    }
}
