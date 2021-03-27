package DiagnosticsClient;

import DiagnosticsClient.Communication.ClientPlatformConnection;
import DiagnosticsClient.Communication.Exception.ClientCommunicationException;
import DiagnosticsClient.Communication.ServerInformation;
import Communication.Exception.RESTClientException;
import RunnerManagement.Exception.RunnersNotConfiguredException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Scanner;

public class Client {

    private final ClientRunnerManager manager;

    public Client(String baseURL, String registerURL,
                  String assignURL, String getServerURL,
                  String instructionsURL, String measurementsURL) throws ClientCommunicationException {
        manager = new ClientRunnerManager();
        try {
            ClientPlatformConnection connection = new ClientPlatformConnection(baseURL, registerURL,
                    assignURL, getServerURL, instructionsURL, measurementsURL);
            connection.register(this.getJsonRepresentation());
            ServerInformation server = connection.getServer(this.getJsonRepresentation());
            manager.configure(connection,getJsonRepresentation(),server);
        } catch (JsonProcessingException | RESTClientException e) {
            throw new ClientCommunicationException("Communication with diagnostics platform failed: ", e);
        }
    }

    private String getJsonRepresentation() {
        String id = "diagnostics_client";
        return  "{\"id\":\"" + id + "\"}";
    }

    public ClientRunnerManager getManager() {
        return manager;
    }

    public static void main(String[] args) {
        try {
            String baseURL = args[0];
            String registerURL = args[1];
            String assignURL = args[2];
            String getServerURL = args[3];
            String instructionsURL = args[4];
            String measurementsURL = args[5];
            Client activeClient = new Client(baseURL,registerURL,assignURL,
                    getServerURL, instructionsURL, measurementsURL);
            activeClient.getManager().startRunners();

            // Temporary exit ----------------------
            Scanner in = new Scanner(System.in);
            String input;
            do {
                input = in.nextLine();
            } while (!input.equals("exit"));
            activeClient.getManager().stopRunners();
            //----------------------------------------

        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("To use the program the following arguments are expected:\n" +
                    "1. Base URL\n" +
                    "2. Register URL\n" +
                    "3. Assignment URL\n" +
                    "4. Server GET URL\n" +
                    "5. Instructions URL\n" +
                    "6. Measurements URL");
            System.exit(-1);
        } catch (ClientCommunicationException | RunnersNotConfiguredException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
