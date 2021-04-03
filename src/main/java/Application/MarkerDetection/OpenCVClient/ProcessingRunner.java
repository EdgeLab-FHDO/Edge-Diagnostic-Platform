package Application.MarkerDetection.OpenCVClient;

import com.fasterxml.jackson.core.JsonProcessingException;

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
            try {
                activeOperator.markerDetection();
            } catch (JsonProcessingException | InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}