package InfrastructureManager.Modules.Scenario;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ImmutablePlatformModule;
import InfrastructureManager.ModuleManagement.PlatformInput;
import InfrastructureManager.Modules.Scenario.Exception.Input.InvalidTimeException;
import InfrastructureManager.Modules.Scenario.Exception.Input.OwnerModuleNotSetUpException;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Class representing an scenario, defined as a series of events that happen with a defined and guaranteed timing.
 * <p>
 * This class is a form of Platform input, in the sense that gives the different commands for the platform to read and process with the
 * defined timing.
 */
@JsonIgnoreProperties({"startBlock","startTime","currentIndex", "pausedTime",
        "resumedTime","started"})
public class Scenario extends ScenarioModuleObject implements PlatformInput {

    private final List<Event> eventList;
    private final Semaphore startBlock;
    private long startTime;
    private int currentIndex;
    private long pausedTime;
    private long resumedTime;
    private boolean started;


    /**
     * Constructor of the class. Creates a new Scenario and initializes al internall fields.
     * Because it will normally be called by a {@link com.fasterxml.jackson.databind.ObjectMapper} it
     * uses the {@link JacksonInject} annotation to indicate that the pointer to the owner module is to
     * be injected on runtime before deserialization.
     *
     * @param module Owner module of this scenario.
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

    /**
     * Implementation of the {@link PlatformInput} interface. It controls the execution of the different events in the scenario
     * and returns the different corresponding commands.
     * <p>
     * In other words, it returns the command of an event, blocks for the defined time for the next event and then
     * returns the other command (This with all events in the scenario).
     * <p>
     * This blocks until the scenario is started.
     *
     * @return Command of the current event in the scenario. Returns null when an error occurs or when the scenario is finished
     * @throws InterruptedException If interrupted while waiting on an event.
     */
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
            this.getLogger().debug(this.getName() + " - Stopping scenario as current index > eventList size");
            stop();
            return null;
        }
    }

    /**
     * Ignores exceptions in the execution process.
     *
     * @param outputException Exception happening in the platform's execution process. This value is null if the process went correctly (No exception was raised)
     */
    @Override
    public void response(ModuleExecutionException outputException) {}

    /**
     * Get the event list of the scenario
     *
     * @return Event List of the scenario
     */
    public List<Event> getEventList() {
        return eventList;
    }

    /**
     * Set the start time of the scenario
     *
     * @param startTime Absolute time in milliseconds (Since UNIX epoch)
     * @throws InvalidTimeException If the passed time is in the past compared to the instant when this method was called
     */
    public void setStartTime(long startTime) throws InvalidTimeException {
        this.getLogger().debug(this.getName() + " - Setting the start time of the scenario");
        if (startTime >= System.currentTimeMillis()) {
            this.startTime = startTime;
        } else {
            throw new InvalidTimeException("Start time in the past!");
        }
    }

    /**
     * Get the defined start time for the scenario
     *
     * @return Defined absolute time in milliseconds (Since UNIX epoch)
     */
    public long getStartTime() {
        this.getLogger().debug(this.getName() + " - Scenario start time is : "+startTime );
        return startTime;
    }

    /**
     * Add an event to the scenario
     *
     * @param event Event to be added
     */
    public void addEvent(Event event) {
        this.getLogger().debug(this.getName() + " - Scenario event: "+event+" added");
        this.eventList.add(event);
    }

    /**
     * Delete an Event from the scenario
     *
     * @param index Index in the event list of the scenario to be deleted
     */
    public void deleteEvent(int index) {
        this.getLogger().debug(this.getName() + "- Scenario event: "+this.eventList.get(index)+" removed");
        this.eventList.remove(index);
    }

    /**
     * Start the scenario.
     *
     * This unblocks the {@link #read()} method and makes the object start returning commands
     *
     * @throws OwnerModuleNotSetUpException If the owner module was not correctly configured while instantiating.
     */
    public void start() throws OwnerModuleNotSetUpException {
        this.getLogger().debug(this.getName() + " - Scenario Started");
        if (this.getOwnerModule() == null) {
            throw new OwnerModuleNotSetUpException("Owner module of the scenario has not been set up");
        }
        currentIndex = 0;
        this.started = true;
        this.startBlock.release();
    }

    /**
     * Pauses the scenario.
     */
    public void pause(){
        this.pausedTime += System.currentTimeMillis();
        this.getLogger().debug(this.getName() + " - Scenario Paused");
    }

    /**
     * Resumes the scenario.
     */
    public void resume() {
        this.resumedTime += System.currentTimeMillis();
        this.getLogger().debug(this.getName() + " - Scenario Resumed");
    }

    /**
     * Stops the scenario.
     */
    public void stop() {
        this.pausedTime = 0L;
        this.resumedTime = 0L;
        currentIndex = 0;
        this.started = false;
        this.getLogger().debug(this.getName() + " - Scenario Finished");
        //System.out.println("FINISHED SCENARIO: " + this.getName());
    }

    /**
     * Waits for the defined execution time in an event and returns its command
     *
     * @param e Event to be waited for
     * @return Command of e
     * @throws InvalidTimeException If errors with the timing make the waiting time negative.
     */
    private String waitForEvent (Event e) throws InvalidTimeException {
        this.getLogger().debug(this.getName() + " - Waiting for Event");
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
