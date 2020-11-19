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
                System.out.println("setup server");
                activeOperator.setupTcpConnection("172.17.0.1:10200");
            } catch (InterruptedException | IllegalArgumentException e) {
                e.printStackTrace();
            }

            System.out.println("start loop");
            while (evaluation.isGood()) {
                activeOperator.setServerUtilization(true);
                evaluation.evaluate();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            activeOperator.setServerUtilization(false);
        }
    }
}