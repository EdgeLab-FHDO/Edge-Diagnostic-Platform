package InfrastructureManager;

public class Master {

    private final CommandSet commandSet;
    private final MasterInput input;
    private final MasterOutput output;

    public Master() {
        MasterConfigurator configurator = new MasterConfigurator();
        commandSet = configurator.getCommands();
        input =configurator.getInput();
        output = configurator.getOutput();
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

    public static void main(String[] args) {
        Master master = new Master();
        String in;
        String mapping;
        while (true){
            System.out.println("Input: ");
            in = master.fromInput();
            if (in.equals("exit")) {
                break;
            }
            mapping = master.execute(in);
            master.toOutput(mapping);
        }
    }

}
