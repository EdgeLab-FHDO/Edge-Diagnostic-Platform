package InfrastructureManager.Rest;

import InfrastructureManager.*;
import spark.Spark;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RestRunner extends Runner {
    private int port = 4567;
    private int retryAttempt = 10;
    private String host = "localhost";
    private String heartbeatPath = "/heartbeat";
    private static RestRouter server = null;
    private static RestRunner restRunner = null;

    private RestRunner(String name, MasterInput input, MasterOutput...outputs) {
        super(name,input,outputs);
    }

    private void startServer() {
        this.server = RestRouter.startRouter(port);
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
    public static RestRunner getRestRunner(String name, MasterInput input, MasterOutput...outputs) {
        if(restRunner == null) {
            restRunner = new RestRunner(name,input,outputs);
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
        super.run();
    }

    @Override
    public void exit() {
        killServer();
        super.exit();
    }
}
