package InfrastructureManager.OldRest;

import InfrastructureManager.*;
import spark.Spark;

import java.net.HttpURLConnection;
import java.net.URL;

public class RestRunner extends Runner {
    private final int port;
    private final int retryAttempt = 10;
    private final String host = "localhost";
    private final String heartbeatPath = "/heartbeat";
    private RestRouter server = null;
    private static RestRunner restRunner = null;

    private RestRunner(String name,int port) {
        super(name);
        this.port = port;
    }

    private void startServer() {
        this.server = RestRouter.startRouter(this.port);
    }

    /**
     * Starter for the server if it's not started yet, the system would wait up to 10s for the server to start
     * Made public for Testing purpose
     */
    public void startServerIfNotRunning() throws InterruptedException {
        try {
            if(!serverIsRunning()) {
                startServer();
            }
        } catch (IllegalStateException e) {
            throw e;
        }

        waitForServerToRun();
    }

    private void waitForServerToRun() throws InterruptedException {
        int tries = retryAttempt;
        while(tries > 0) {
            if(!serverIsRunning()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw e;
                }
            } else {
                return;
            }
            tries--;
        }
    }

    private boolean serverIsRunning(){
        try{
            HttpURLConnection con = (HttpURLConnection)new URL("http",host, port, heartbeatPath).openConnection();
            return con.getResponseCode()==200;
        }catch(Exception e){
            return false;
        }
    }

    /**
     * Method for stoping the static Spark instance and sets server to null after Spark is stopped
     */
    public void killServer() {
        this.server = null;
        Spark.stop();
        Spark.awaitStop();
    }

    /**
     * Method for getting the Singleton REST Runner instance or set from Configuration if not available
     */
    public static RestRunner getRestRunner() throws Exception {
        if(restRunner == null) {
            throw new Exception("Runner not set");
        }
        return restRunner;
    }

    /**
     * Method for getting the Singleton REST Runner instance or set from parameter if not available
     */
    public static RestRunner getRestRunner(String name, int port) {
        if(restRunner == null) {
            restRunner = new RestRunner(name, port);
        }
        return restRunner;
    }

    @Override
    public void run() {
        try {
            RestRunner.getRestRunner().startServerIfNotRunning();
        } catch (Exception e) {
            e.printStackTrace();
        }
        running = true;
    }

    @Override
    public void exit() {
        killServer();
        super.exit();
    }
}
