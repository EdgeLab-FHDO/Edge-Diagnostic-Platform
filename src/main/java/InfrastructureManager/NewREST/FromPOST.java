package InfrastructureManager.NewREST;

import InfrastructureManager.MasterInput;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayDeque;
import java.util.Queue;

import static spark.Spark.awaitInitialization;
import static spark.Spark.post;

public class FromPOST implements MasterInput {

    private Queue<String> commands;

    private String command;
    private final String path;

    private boolean isActivated;

    private final Object blockLock;
    private volatile boolean block;

    private final Route POSTHandler = (Request request, Response response) -> {
        String bodyAsString = request.body().replaceAll("\\s+","");
        this.commands.offer(this.command + " " + bodyAsString);
        unBlock();
        return response.status();
    };

    public FromPOST(String path, String command) {
        this.command = command;
        this.path = path;
        this.isActivated = false;
        this.commands = new ArrayDeque<>();
        this.blockLock = new Object();
        this.block = true;
    }

    @Override
    public String read() throws Exception {
        if (!isActivated) {
            activate();
        }
        String reading = getReading();
        if (this.commands.peek() == null) {
            block = true;
        }
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
        return this.commands.poll();
    }

    private void unBlock() {
        synchronized (blockLock) {
            block = false;
            blockLock.notifyAll();
        }
    }

    private void activate() {
        try {
            if (RestServerRunner.getRestServerRunner().isRunning()) {
                post(this.path, this.POSTHandler);
            } else {
                throw new IllegalStateException("Rest Server is not running");
            }
            this.isActivated = true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


}
