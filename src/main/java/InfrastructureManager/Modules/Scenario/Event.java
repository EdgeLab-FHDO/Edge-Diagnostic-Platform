package InfrastructureManager.Modules.Scenario;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * MasterInputInterface class representing an Event (Triggered in the scenarios)
 * The definition of the event objects comes from a JSON file
 */
public class Event{

    private final String command; //Command to be executed in the master
    private final long executionTime; //Execution time relative to the scenario start (in ms)

    /**
     * Constructor of the class
     * @param command Command of the event (To be interpreted by the master)
     * @param executionTime Execution time of the event in milliseconds, relative to the start of the scenario
     */
    @JsonCreator
    public Event(@JsonProperty("command") String command, @JsonProperty("executionTime") long executionTime) {
        this.command = command;
        this.executionTime = executionTime;
    }

    public String getCommand() {
        return command;
    }

    /**
     * Gets the execution time defined for the event
     * @return Event relative execution time in ms
     */
    public long getExecutionTime() {
        return executionTime;
    }
}
