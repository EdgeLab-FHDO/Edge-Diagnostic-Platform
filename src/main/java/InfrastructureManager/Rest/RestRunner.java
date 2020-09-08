package InfrastructureManager.Rest;

import InfrastructureManager.MasterInput;
import InfrastructureManager.MasterOutput;
import InfrastructureManager.Runner;
import spark.Spark;

import java.net.HttpURLConnection;
import java.net.URL;

public class RestRunner extends Runner {
    private int port = 4567;
    private int retryAttempt = 10;
    private String host = "localhost";
    private String heartbeatPath = "/heartbeat";
    private static RestRouter server = null;

    public RestRunner(String name, MasterInput input, MasterOutput...outputs) {
        super(name,input,outputs);
    }

    private void startServer() {
        this.server = RestRouter.startRouter(port);
    }

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

    //stops the static Spark instance and sets server to null after Spark is stopped
    public void killServer() {
        Spark.stop();
        Spark.awaitStop();
        server = null;
    }

    public boolean serverIsRunning(){
        try{
            HttpURLConnection con = (HttpURLConnection)new URL("http",host, port, heartbeatPath).openConnection();
            return con.getResponseCode()==200;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public void exit() {
        killServer();
        super.exit();
    }
}
