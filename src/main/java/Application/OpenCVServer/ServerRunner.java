package Application.OpenCVServer;


public class ServerRunner implements Runnable {
    private OpenCVServerOperator activeOperator;

    public ServerRunner() {
        activeOperator = OpenCVServerOperator.getInstance();
    }
    @Override
    public void run() {
        while(true) {
            activeOperator.startConnection();
            activeOperator.standbyForConnection();
            activeOperator.stopConnection();
        }
    }
}