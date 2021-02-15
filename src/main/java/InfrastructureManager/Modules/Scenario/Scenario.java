package InfrastructureManager.Modules.Scenario;

import InfrastructureManager.ModuleManagement.Exception.Creation.IncorrectInputException;
import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformInput;
import InfrastructureManager.ModuleManagement.Runner;
import InfrastructureManager.Modules.Scenario.Exception.Input.InvalidTimeException;
import InfrastructureManager.Modules.Scenario.Exception.Input.OwnerModuleNotSetUpException;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Class representing an scenario, as an object with a name and a list of events
 */
@JsonIgnoreProperties({"startBlock","startTime","currentIndex", "pausedTime",
        "resumedTime","started","ownerModule", "name"})
public class Scenario extends ScenarioModuleObject implements PlatformInput {

    private final List<Event> eventList;
    private final Semaphore startBlock;
    private long startTime;
    private int currentIndex;
    private long pausedTime;
    private long resumedTime;
    private boolean started;


    /**
     * Constructor of the class
     *
     */
    public Scenario(@JacksonInject ImmutablePlatformModule module) {
        super(module, module.getName() + ".scenario");
        this.eventList = new ArrayList<>();
        this.startBlock = new Semaphore(0);
        this.startTime = 0; //When a new scenario is created for file or command, start time in 0 (It will be rewritten when is run)
        this.currentIndex = 0;
        this.pausedTime = 0L;
        this.resumedTime = 0L;
        this.started = false;
    }

    @Override
    public String read() throws InterruptedException {
        if (!started) {
            this.startBlock.acquire();
        }
        if (currentIndex < eventList.size()) {
            Event currentEvent = eventList.get(currentIndex);
            currentIndex++;
            try {
                return waitForEvent(currentEvent);
            } catch (InvalidTimeException e) {
                e.printStackTrace();
                stop();
                return null;
            }
        } else {
            stop();
            return null;
        }
    }

    @Override
    public void response(ModuleExecutionException outputException) {}

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
    public void setStartTime(long startTime) throws InvalidTimeException {
        if (startTime >= System.currentTimeMillis()) {
            this.startTime = startTime;
        } else {
            throw new InvalidTimeException("Start time in the past!");
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

    public void start() throws OwnerModuleNotSetUpException {
        if (this.getOwnerModule() == null) {
            throw new OwnerModuleNotSetUpException("Owner module of the scenario has not been set up");
        }
        currentIndex = 0;
        this.started = true;
        this.startBlock.release();
    }

    public void pause() {
        this.pausedTime += System.currentTimeMillis();
    }

    public void resume() {
        this.resumedTime += System.currentTimeMillis();
    }

    public void stop() {
        this.pausedTime = 0L;
        this.resumedTime = 0L;
        currentIndex = 0;
        this.started = false;
        //System.out.println("FINISHED SCENARIO: " + this.getName());
    }
    /**
     * Wait for executing the states according to their relative execution times
     * @param e Event to be waited for
     */
    private String waitForEvent (Event e) throws InvalidTimeException {
        long absoluteTime = this.getStartTime() + (resumedTime - pausedTime) + e.getExecutionTime();
        try {
            Thread.sleep(absoluteTime - System.currentTimeMillis());
        } catch (InterruptedException ie) {
            stop();
        } catch (IllegalArgumentException iae) {
            throw new InvalidTimeException("Handling event " + e.getCommand() + " resulted in a negative waiting time");
        }
        return e.getCommand();
    }
}
