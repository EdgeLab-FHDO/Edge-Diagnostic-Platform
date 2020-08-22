package InfrastructureManager;

import java.util.ArrayList;

public class Master {

    private final CommandSet commandSet;
    private final MasterInput input;
    private final MasterOutput output;
    private ArrayList<Runner> runnerList;
    private static Master instance = null;

    private Master() {
        MasterConfigurator configurator = new MasterConfigurator();
        commandSet = configurator.getCommands();
        input =configurator.getInput();
        output = configurator.getOutput();
        //TODO: Fix, this comes from configurator
        Runner mainRunner = new Runner(new ConsoleInput(),new ConsoleOutput(), new MasterUtility());
        runnerList = new ArrayList<>();
        runnerList.add(mainRunner);
    }
    public String execute(String command) {
        return this.commandSet.getResponse(command);
    }

    public void exitAll() {
        for (Runner runner : runnerList) {
            runner.exit();
        }
    }

    //TODO:Fix this can go in configurator
    public void startMainRunner() {
        new Thread(runnerList.get(0),"MainRunner").start();
    }

    public static Master getInstance() {
        if (instance == null) {
            instance = new Master();
        }
        return instance;
    }

    public static void main(String[] args) {
        Master.getInstance().startMainRunner();
        /*
        Master master = Master.getInstance();
        String in;
        String mapping;
        while (true){
            in = master.fromInput();
            if (in.equals("exit")) {
                break;
            }
            mapping = master.execute(in);
            master.toOutput(mapping);
        }

         */
    }


}
