import java.net.StandardSocketOptions;

public class MasterCommunicationRunner implements Runnable {
    //TO DO: change extends Runner and instead implement Runnable
    //TO DO: reuse instances
    @Override
    public void run() {
        OpenCVClient activeClient = OpenCVClient.getInstance();
        ConnectionEvaluator evaluation = new ConnectionEvaluator();
        MasterCommunicator communicator = new MasterCommunicator();

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