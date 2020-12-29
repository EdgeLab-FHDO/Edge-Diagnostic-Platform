package Application.MarkerDetection.OpenCVClient;

import java.io.IOException;

public class MasterCommunicationRunner implements Runnable {
    //TODO today, request assign client during the first time
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
                activeOperator.setServerUtilization(true);
            } catch (InterruptedException | IllegalArgumentException | IOException e) {
                e.printStackTrace();
            }

            while (OpenCVClientOperator.getInstance().utilizeServer && evaluation.isGood()) {
                //TODO consider moving the new server utilization value into a parameter or take it from the evaluation result
                //TODO add condition if processing is local, break from this loop and skip evaluation (make a custom Exception and throw it)
                evaluation.evaluate();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
            activeOperator.setServerUtilization(false);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}