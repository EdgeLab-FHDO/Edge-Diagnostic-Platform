package InfrastructureManager;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * MasterInput class representing an Event (Triggered in the scenarios)
 * The definition of the event objects comes from a JSON file
 */
public class Event implements MasterInput {

    private String command; //Command to be executed in the master
    private long executionTime; //Execution time relative to the scenario start (in ms)

    /**
     * Default constructor of the class, initializes values
     */
    public Event() { //This is needed for Jackson to create the object
        this.command = null;
        this.executionTime = 0;
    }

    /**
     * Constructor of the class
     * @param command Command of the event (To be interpreted by the master)
     * @param executionTime Execution time of the event in milliseconds, relative to the start of the scenario
     */
    public Event(String command, long executionTime) {
        this.command = command;
        this.executionTime = executionTime;
    }

    /**
     * Setter for the command field
     * @param command Command to be set up in the event
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * Read implementation according to MasterInput Interface
     * @return The defined command of the event
     */
    @Override
    @JsonGetter("command")
    public String read() {
        return this.command;
    }

    /**
     * Gets the execution time defined for the event
     * @return Event relative execution time in ms
     */
    public long getExecutionTime() {
        return executionTime;
    }
}
