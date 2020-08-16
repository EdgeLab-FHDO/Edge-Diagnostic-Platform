package InfrastructureManager;

public class Event implements MasterInput {
    private String command;
    private int time;
    public Event() {
        this.command = null;
        this.time = 0;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String read() {
        //TODO: Implement
        return this.command;
    }

    @Override
    public String toString() {
        return "Event: " + this.command + ", Time: " + this.time;
    }
}
