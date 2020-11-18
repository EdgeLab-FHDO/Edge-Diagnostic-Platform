package Application.OpenCVServer;


import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.io.InvalidObjectException;

public class ServerRunner implements Runnable {
    private final OpenCVServerOperator activeOperator;

    public ServerRunner() {
        activeOperator = OpenCVServerOperator.getInstance();
    }
    @Override
    public void run() {
        while(true) {
            try {
                activeOperator.startConnection();
                activeOperator.standbyForConnection();
                activeOperator.stopConnection();
            } catch (IllegalArgumentException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}