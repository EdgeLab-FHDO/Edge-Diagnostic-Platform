package Application.MarkerDetection.OpenCVClient;

public class EQRManagementRunner implements Runnable {
    //TODO implement and use pause, exit and running values
    private final OpenCVClientOperator activeOperator;
    private final EQREvaluator evaluator;
    private final int latencyThreshold;

    private volatile boolean exit = false;
    private volatile boolean running = false;

    public EQRManagementRunner() {
        activeOperator = OpenCVClientOperator.getInstance();
        evaluator = (EQREvaluator) activeOperator.evaluator;
        latencyThreshold = activeOperator.latencyThreshold;
    }
    @Override
    public void run() {
        while(true) {
            evaluator.manageEqrChange(latencyThreshold);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
