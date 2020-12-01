package Application.OpenCVClient;

import java.io.IOException;

public class MasterCommunicationRunner implements Runnable {
    private final OpenCVClientOperator activeOperator;
    private ConnectionEvaluator evaluation;
    public MasterCommunicator communicator;

    public MasterCommunicationRunner(String masterUrl) {
        activeOperator = OpenCVClientOperator.getInstance();
        evaluation = new ConnectionEvaluator();
        communicator = new MasterCommunicator(masterUrl);
    }

    @Override
    public void run() {
        while(true) {
            try {
                activeOperator.setupTcpConnection(communicator.getServer());
            } catch (InterruptedException | IllegalArgumentException | IOException e) {
                e.printStackTrace();
            }

            while (evaluation.isGood()) {
                //TODO consider moving the new server utilization value into a parameter or take it from the evaluation result
                activeOperator.setServerUtilization(true);
                evaluation.evaluate();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
            activeOperator.setServerUtilization(false);
        }
    }
}