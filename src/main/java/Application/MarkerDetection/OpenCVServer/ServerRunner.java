package Application.MarkerDetection.OpenCVServer;


import Application.Utilities.RemoteExecutionException;
import java.io.IOException;

public class ServerRunner implements Runnable {
    //TODO implement and use exit and running values
    private final OpenCVServerOperator activeOperator;

    private volatile boolean exit = false; //Flag to check status and be able to exit
    private volatile boolean running = false; //Flag to see runner status

    public ServerRunner() {
        activeOperator = OpenCVServerOperator.getInstance();
    }
    @Override
    public void run() {
        while(true) {
            try {
                activeOperator.startConnection();
                activeOperator.processing();
            } catch (IOException | IllegalArgumentException e) {
                try {
                    activeOperator.stopConnection();
                    Thread.sleep(1000);
                } catch (RemoteExecutionException | InterruptedException stopException) {
                    stopException.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }
}