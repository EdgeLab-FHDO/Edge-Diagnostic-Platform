package InfrastructureManager.Modules.REST.Input;

import InfrastructureManager.ModuleManagement.Exception.Execution.ModuleExecutionException;
import InfrastructureManager.ModuleManagement.ModuleInput;
import InfrastructureManager.Modules.REST.Exception.Input.ParsingArgumentNotDefinedException;
import InfrastructureManager.Modules.REST.Exception.Input.UnsupportedJSONTypeException;
import InfrastructureManager.Modules.REST.RestServerRunner;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import static spark.Spark.post;

/**
 * Class to represent data coming from POST requests, it can be configured to extract parameters from the request body json
 */
public class POSTInput extends ModuleInput {

    private final Queue<String> toRead;

    private final String URL; //Path where the post handler will listen
    private final List<String> toParse; //List of argument that will be searched for in the json body
    private boolean isActivated; //To synchronize with the rest server

    private final Route POSTHandler;
    private String customResponse;

    private final Semaphore readBlock;
    private final Semaphore responseBlock;

    /**
     * Constructor of the class
     * @param URL Path where the post request will be handled
     * @param command Command that the input will send to the master, it can include parameters using '$'
     * @param toParse List of parameters in the JSON that the input will search for
     */
    public POSTInput(String name, String URL, String command, List<String> toParse) {
        super(name);
        this.URL = URL;
        this.toParse = toParse;
        this.isActivated = false;
        this.toRead = new ArrayDeque<>();
        this.customResponse = null;
        this.readBlock = new Semaphore(0);
        this.responseBlock = new Semaphore(0);
        this.POSTHandler = (Request request, Response response) -> {
            UnknownJSONObject o = new UnknownJSONObject(request.body());
            try {
                this.toRead.offer(this.substituteCommand(command,o));
                this.readBlock.release(); //Unblock the reading
                this.responseBlock.acquire(); //Block until information for response is available
                if (this.customResponse == null) {
                    response.status(200);
                    response.body("200");
                } else {
                    response.body(this.customResponse);
                    this.customResponse = null;
                    response.status(500);
                }
            } catch (ParsingArgumentNotDefinedException | UnsupportedJSONTypeException e) {
                e.printStackTrace();
                response.body(e.getMessage());
                response.status(500); //If argument not found, return internal error in response
            }
            return response.body();
        };
    }

    /**
     * Method to create the command that will be sent to the master by extracting information from the request body
     * In the defined command, all parameters with '$' will be substituted and whatever doesn't use the symbol
     * will be copied to the final command. Finally, if the command had the "$body" parameter, the entire JSON
     * body will be substituted
     * @param rawCommand Command as read from the configuration of the input
     * @param object Object created with the body from the response
     * @return Final command with parameters in '$' substituted
     * @throws IllegalArgumentException if a command contains a parameter not defined to be searched for in the configuration
     */
    private String substituteCommand(String rawCommand, UnknownJSONObject object) throws ParsingArgumentNotDefinedException, UnsupportedJSONTypeException {
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
                } else if(cleanValueName.equals("body")) {
                    finalCommand.append(object.getBody().replaceAll("\\s+",""));
                } else {
                    throw new ParsingArgumentNotDefinedException("Argument " + cleanValueName
                            + " was not defined to be parsed");
                }
            } else {
                finalCommand.append(reading);
            }
        }
        return finalCommand.toString();
    }

    /**
     * MasterInputInterface implementation, blocks whenever there is nothing to send
     * @return Command to trigger events in the master
     */
    @Override
    public String read() throws InterruptedException {
        if (!isActivated) {
            activate();
        }
        this.readBlock.acquire(); //Wait on requests
        return this.toRead.poll();
    }

    @Override
    public void response(ModuleExecutionException outputException) {
        if (outputException != null) {
            outputException.printStackTrace();
            this.customResponse = outputException.getMessage();
        }
        this.responseBlock.release(); //Unblock the thread in charge of responding the request
    }

    /**
     * Synchronize with the REST Server Thread, so the route for POST handling is only created after the
     * server has been initialized
     */
    private void activate() {
        try {
            RestServerRunner.serverCheck.acquire();
            post(this.URL, this.POSTHandler);
            this.isActivated = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            RestServerRunner.serverCheck.release();
        }
    }
}
