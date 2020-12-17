package InfrastructureManager.REST.Input;

import InfrastructureManager.MasterInput;
import InfrastructureManager.REST.RestServerRunner;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import static spark.Spark.post;
//TODO: Comment and javadoc
public class POSTInput implements MasterInput {

    private final Queue<String> toRead;

    private final String path;
    private final List<String> toParse;
    private boolean isActivated;

    private final Object blockLock;
    private volatile boolean block;

    private final Route POSTHandler;


    public POSTInput(String path, String command, List<String> toParse) {
        this.path = path;
        this.toParse = toParse;
        this.isActivated = false;
        this.toRead = new ArrayDeque<>();
        this.blockLock = new Object();
        this.block = true;
        this.POSTHandler = (Request request, Response response) -> {
            UnknownJSONObject o = new UnknownJSONObject(request.body());
            try {
                this.toRead.offer(this.substituteCommand(command,o));
            } catch (IllegalArgumentException e) {
                response.status(500);
            } finally {
                unBlock();
            }
            return response.status();
        };
    }

    private String substituteCommand(String rawCommand, UnknownJSONObject object) {
        String[] splitCommand = rawCommand.split(" ");
        StringBuilder finalCommand = new StringBuilder(splitCommand[0]);
        String cleanValueName;
        String reading;
        for (int i = 1; i < splitCommand.length; i++) {
            finalCommand.append(' ');
            reading = splitCommand[i];
            if (reading.matches("\\$.*")) {
                cleanValueName = reading.replaceFirst("\\$","");
                if (toParse.contains(cleanValueName)) {
                    finalCommand.append(object.getValue(cleanValueName));
                } else {
                    //TODO: Handle exception
                    throw new IllegalArgumentException("Argument in command was not defined to be parsed");
                }

            } else {
                finalCommand.append(reading);
            }
        }
        return finalCommand.toString();
    }

    @Override
    public String read() {
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
