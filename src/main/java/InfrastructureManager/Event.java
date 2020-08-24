package InfrastructureManager;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * MasterInput class representing an Event (Triggered in the scenarios)
 * The definition of the event objects comes from a JSON file
 */
public class Event implements MasterInput {

    private String command;

    /**
     * Default constructor of the class, initializes values
     */
    public Event() { //This is needed for Jackson to create the object
        this.command = null;
    }

    /**
     * Constructor of the class
     * @param command Command of the event (To be interpreted by the master)
     */
    public Event(String command) {
        this.command = command;
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
}
