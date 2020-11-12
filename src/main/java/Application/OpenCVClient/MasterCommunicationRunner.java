package Application.OpenCVClient;

import java.io.IOException;
import java.net.StandardSocketOptions;

public class MasterCommunicationRunner implements Runnable {
    //TO DO: change extends Runner and instead implement Runnable
    //TO DO: reuse instances
    private OpenCVClientOperator activeOperator;
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
                activeOperator.setup(communicator.getServer());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("start loop");
            while (evaluation.isGood()) {
                activeOperator.setServerUtilization(1);
                evaluation.evaluate();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            activeOperator.setServerUtilization(0);
        }
    }
}