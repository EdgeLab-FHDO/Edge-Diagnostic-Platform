import java.net.StandardSocketOptions;

public class MasterCommunicationRunner implements Runnable {
    //TO DO: change extends Runner and instead implement Runnable
    //TO DO: reuse instances
    private OpenCVClient activeClient;
    private ConnectionEvaluator evaluation;
    public MasterCommunicator communicator;

    public MasterCommunicationRunner() {
        activeClient = OpenCVClient.getInstance();
        evaluation = new ConnectionEvaluator();
        communicator = new MasterCommunicator();
    }

    @Override
    public void run() {
        while(true) {
            System.out.println("setup server");
            activeClient.setup(communicator.getServer());

            System.out.println("start loop");
            while (evaluation.isGood()) {
                activeClient.setServerUtilization(1);
                evaluation.evaluate();
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            activeClient.setServerUtilization(0);
        }
    }
}