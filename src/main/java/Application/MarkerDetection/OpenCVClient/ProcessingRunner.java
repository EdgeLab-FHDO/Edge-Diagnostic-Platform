package Application.MarkerDetection.OpenCVClient;

import Application.Utilities.RemoteExecutionException;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ProcessingRunner implements Runnable {
    //TODO implement and use exit and running values
    private final ProcessingOperator processingOperator;

    private volatile boolean exit = false; //Flag to check status and be able to exit
    private volatile boolean running = false; //Flag to see runner status

    public ProcessingRunner() {
        processingOperator = new ProcessingOperator();
    }
    @Override
    public void run() {
        while(true) {
            try {
                processingOperator.mainOperation();
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