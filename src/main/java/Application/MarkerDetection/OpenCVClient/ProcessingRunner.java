package Application.OpenCVClient;

public class ProcessingRunner implements Runnable {
    //TODO implement and use exit and running values
    private final OpenCVClientOperator activeOperator;

    private volatile boolean exit = false; //Flag to check status and be able to exit
    private volatile boolean running = false; //Flag to see runner status

    public ProcessingRunner() {
        activeOperator = OpenCVClientOperator.getInstance();
    }
    @Override
    public void run() {
        while(true) {
            activeOperator.markerDetection();
        }
    }
}