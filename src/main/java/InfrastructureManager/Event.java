package InfrastructureManager;

public class Event implements MasterInput {
    private String command;
    public Event(String command) {
        this.command = command;
    }
    @Override
    public String read() {
        //TODO: Implement
        return this.command;
    }
    public void trigger() {

    }

}
