package InfrastructureManager.NewREST;

import InfrastructureManager.MasterInput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.*;

import static spark.Spark.post;

public class FromPOST implements MasterInput {

    private final Queue<String> toRead;

    //private final String command;
    private final String path;
    private final List<String> toParse;

    private boolean isActivated;

    private final Object blockLock;
    private volatile boolean block;

    private final Route POSTHandler;

    Map<String,String> stringValues = new HashMap<>();
    Map<String,Integer> intValues = new HashMap<>();
    Map<String,Boolean> booleanValues = new HashMap<>();

    public FromPOST(String path, String command, List<String> toParse) {
        //this.command = command;
        this.path = path;
        this.toParse = toParse;
        this.isActivated = false;
        this.toRead = new ArrayDeque<>();
        this.blockLock = new Object();
        this.block = true;
        this.POSTHandler = (Request request, Response response) -> {
            //String bodyAsString = request.body().replaceAll("\\s+","");
            parseJson(request.body());
            this.toRead.offer(this.createCommand(command));
            unBlock();
            return response.status();
        };
    }

    private void parseJson(String body) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(body);
            for (String value : this.toParse) {
                putValue(value,node);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void putValue(String searched, JsonNode parentNode) {
        JsonNode node = parentNode.get(searched);
        if (node == null) {
            System.out.println("Not found");
            return;
        }
        switch (node.getNodeType()) {
            case STRING:
                this.stringValues.put(searched,node.textValue());
                break;
            case BOOLEAN:
                this.booleanValues.put(searched, node.booleanValue());
                break;
            case NUMBER:
                this.intValues.put(searched,node.intValue());
                break;
            default:
                System.out.println("error");
        }
    }

    private String createCommand(String rawCommand) {
        String[] splitCommand = rawCommand.split(" ");
        StringBuilder finalCommand = new StringBuilder(splitCommand[0]);
        String cleanValueName;
        String reading;
        for (int i = 1; i < splitCommand.length; i++) {
            finalCommand.append(' ');
            reading = splitCommand[i];
            if (reading.matches("\\$.*")) {
                cleanValueName = reading.replaceFirst("\\$","");
                if (stringValues.containsKey(cleanValueName)) finalCommand.append(stringValues.get(cleanValueName));
                if (intValues.containsKey(cleanValueName)) finalCommand.append(intValues.get(cleanValueName));
                if (booleanValues.containsKey(cleanValueName)) finalCommand.append(booleanValues.get(cleanValueName));
            } else {
                finalCommand.append(reading);
            }
        }
        return finalCommand.toString();
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
