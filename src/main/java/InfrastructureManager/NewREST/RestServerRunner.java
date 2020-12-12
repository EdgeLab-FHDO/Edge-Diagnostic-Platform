package InfrastructureManager.NewREST;

import InfrastructureManager.Runner;
import spark.Spark;

import java.net.HttpURLConnection;
import java.net.URL;

import static spark.Spark.get;

public class RestServerRunner extends Runner {
    private final int port;

    private final int retryAttempt = 10;
    private final String host = "localhost";
    private final String heartbeatPath = "/heartbeat";
    private static RestServerRunner restRunner = null;

    private RestServerRunner(String name, int port) {
        super(name);
        this.port = port;
    }

    private void startServer() {
        Spark.port(this.port);
        get(heartbeatPath, (request, response) -> response.status());
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
        Spark.stop();
        Spark.awaitStop();
    }

    /**
     * Method for getting the Singleton REST Runner instance or set from Configuration if not available
     */
    public static RestServerRunner getRestServerRunner() throws IllegalStateException {
        if(restRunner == null) {
            throw new IllegalStateException("Rest Server is not configured");
        }
        return restRunner;
    }

    /**
     * Method for getting the Singleton REST Runner instance or set from parameter if not available
     */
    public static void configure(String name, int port) {
        if(restRunner == null) {
            restRunner = new RestServerRunner(name, port);
        }
    }

    @Override
    public void run() {
        try {
            this.startServerIfNotRunning();
            System.out.println("waaa");
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
