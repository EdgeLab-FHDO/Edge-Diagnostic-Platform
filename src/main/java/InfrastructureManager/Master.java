package InfrastructureManager;

public class Master {
    private final CommandSet commandSet;
    private MasterInput input;

    public Master() {
        commandSet = CommandSet.getInstance();
        input =new ConsoleInput();
    }

    public static void main(String[] args) {
        Master master = new Master();
        String in = master.fromInput();
        String mapping = master.execute(in);
        System.out.println(mapping);
    }

    public String fromInput() {
        return this.input.read();
    }

    public String execute(String command) {
        return this.commandSet.getResponse(command);
    }

}
