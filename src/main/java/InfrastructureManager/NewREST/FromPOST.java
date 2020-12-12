package InfrastructureManager.NewREST;

import InfrastructureManager.MasterInput;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayDeque;
import java.util.Queue;

import static spark.Spark.post;

public class FromPOST implements MasterInput {

    private Queue<String> toRead;

    private String command;
    private final String path;

    private boolean isActivated;

    private final Object blockLock;
    private volatile boolean block;

    private final Route POSTHandler = (Request request, Response response) -> {
        String bodyAsString = request.body().replaceAll("\\s+","");
        this.toRead.offer(this.command + " " + bodyAsString);
        unBlock();
        return response.status();
    };

    public FromPOST(String path, String command) {
        this.command = command;
        this.path = path;
        this.isActivated = false;
        this.toRead = new ArrayDeque<>();
        this.blockLock = new Object();
        this.block = true;
    }

    @Override
    public String read() throws Exception {
        if (!isActivated) {
            activate();
        }
        String reading = getReading();
        block = this.toRead.peek() == null;
        return reading;
    }

    private String getReading() {
        while (block) {
            synchronized (blockLock) {
                try {
                    blockLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return this.toRead.poll();
    }

    private void unBlock() {
        synchronized (blockLock) {
            block = false;
            blockLock.notifyAll();
        }
    }

    private void activate() {
        try {
            while (!RestServerRunner.getInstance().isRunning()) { //Wait for the REST server to run
                synchronized (RestServerRunner.ServerRunning) {
                    RestServerRunner.ServerRunning.wait();
                }
            }
            post(this.path, this.POSTHandler);
            this.isActivated = true;
        } catch (IllegalStateException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
