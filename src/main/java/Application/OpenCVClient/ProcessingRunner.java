package Application.OpenCVClient;

public class ProcessingRunner implements Runnable {
    private OpenCVClientOperator activeOperator;

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