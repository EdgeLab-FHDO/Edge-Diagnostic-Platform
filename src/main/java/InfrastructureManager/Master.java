package InfrastructureManager;

public class Master {
    private final CommandSet commandSet;
    private MasterInput input;
    private MasterOutput output;

    public Master() {
        commandSet = CommandSet.getInstance();
        input =new ConsoleInput();
        output = new ConsoleOutput();
    }

    public static void main(String[] args) {
        Master master = new Master();
        String in = master.fromInput();
        String mapping = master.execute(in);
        master.toOutput(mapping);
    }

    public String fromInput() {
        return this.input.read();
    }
    public String execute(String command) {
        return this.commandSet.getResponse(command);
    }
    public void toOutput(String response) {
        this.output.out(response);
    }

}
