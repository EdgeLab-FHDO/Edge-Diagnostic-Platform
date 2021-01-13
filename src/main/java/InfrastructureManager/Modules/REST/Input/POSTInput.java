package InfrastructureManager.Modules.REST.Input;

import InfrastructureManager.MasterInput;
import InfrastructureManager.Modules.REST.RestServerRunner;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import static spark.Spark.post;

/**
 * Class to represent data coming from POST requests, it can be configured to extract parameters from the request body json
 */
public class POSTInput extends MasterInput {

    private final Queue<String> toRead;

    private final String URL; //Path where the post handler will listen
    private final List<String> toParse; //List of argument that will be searched for in the json body
    private boolean isActivated; //To synchronize with the rest server

    private final Route POSTHandler;

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
        this.POSTHandler = (Request request, Response response) -> {
            UnknownJSONObject o = new UnknownJSONObject(request.body());
            try {
                this.storeReadingAndUnblock(this.substituteCommand(command,o));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                response.body(e.getMessage());
                response.status(500); //If argument not found, return internal error in response
            }
            return response.status();
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
                } else if(cleanValueName.equals("body")) {
                    finalCommand.append(object.getBody().replaceAll("\\s+",""));
                } else {
                    throw new IllegalArgumentException("Argument in command was not defined to be parsed");
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
        return getReading();
    }

    /**
     * Get the commands to send to the master from the internal FIFO queue
     * @return Command from the queue
     */
    @Override
    protected String getSingleReading() {
        return this.toRead.poll();
    }

    /**
     * Store reading to be sent in the FIFO queue
     * @param reading Command to be sent
     */
    @Override
    protected void storeSingleReading(String reading) {
        this.toRead.offer(reading);
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
        } catch (IllegalStateException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            RestServerRunner.serverCheck.release();
        }
    }
}