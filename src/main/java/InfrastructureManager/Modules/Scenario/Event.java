package InfrastructureManager.Modules.Scenario;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class representing an Event (Triggered in the scenarios)
 *
 * Events are the building blocks of an {@link Scenario}.
 *
 *
 * An event is defined as a set of:
 * - A command to send to the platform
 * - An execution time for the command
 *
 * @see <a href="https://github.com/EdgeLab-FHDO/Edge-Diagnostic-Platform/wiki/Scenario-Definition#events">Wiki Entry</a>
 */
public class Event{

    private final String command; //Command to be executed in the master
    private final long executionTime; //Execution time relative to the scenario start (in ms)

    /**
     * Constructor of the class. Creates new event. Uses the {@link JsonProperty} annotation to extract information
     * from the "command" and "executionTime" fields while deserializing the Scenario JSON file
     * @param command Command of the event (To be interpreted by the platform)
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
