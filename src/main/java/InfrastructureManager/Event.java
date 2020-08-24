package InfrastructureManager;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * MasterInput class representing an Event (Triggered in the scenarios)
 * The definition of the event objects comes from a JSON file
 */
public class Event implements MasterInput {

    private String command;
    private int time;

    /**
     * Default constructor of the class, initializes values
     */
    public Event() { //This is needed for Jackson to create the object
        this.command = null;
        this.time = 0;
    }

    /**
     * Constructor of the class
     * @param command Command of the event (To be interpreted by the master)
     * @param time Instant of time of the event
     */
    public Event(String command, int time) {
        this.command = command;
        this.time = time;
    }

    /**
     * Setter for the command field
     * @param command Command to be set up in the event
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * Getter for the time field
     * @return Time defined for the event
     */
    public int getTime() {
        return time;
    }

    /**
     * Setter for the time field
     * @param time Time to be set up in the event
     */
    public void setTime(int time) {
        this.time = time;
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
}
