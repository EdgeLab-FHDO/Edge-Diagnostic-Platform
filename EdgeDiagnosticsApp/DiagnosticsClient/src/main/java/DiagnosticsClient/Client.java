package DiagnosticsClient;

import DiagnosticsClient.Communication.ClientPlatformConnection;
import DiagnosticsClient.Communication.Exception.ClientCommunicationException;
import DiagnosticsClient.Communication.ServerInformation;
import DiagnosticsClient.Load.Exception.TCP.ServerNotSetUpException;
import REST.Exception.RESTClientException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Scanner;

public class Client {

    private final RunnerManager manager;

    public Client(String baseURL, String registerURL,
                  String assignURL, String getServerURL,
                  String instructionsURL) throws ClientCommunicationException {
        manager = new RunnerManager();
        try {
            ClientPlatformConnection connection = new ClientPlatformConnection(baseURL, registerURL, assignURL, getServerURL, instructionsURL);
            connection.register(this.getJsonRepresentation());
            ServerInformation server = connection.getServer(this.getJsonRepresentation());
            manager.configure(connection,getJsonRepresentation(),server);
        } catch (JsonProcessingException | RESTClientException | ServerNotSetUpException e) {
            throw new ClientCommunicationException("Communication with diagnostics platform failed: ", e);
        }
    }

    private String getJsonRepresentation() {
        String id = "diagnostics_client";
        return  "{\"id\":\"" + id + "\"}";
    }

    public RunnerManager getManager() {
        return manager;
    }

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                String baseURL = args[0];
                String registerURL = args[1];
                String assignURL = args[2];
                String getServerURL = args[3];
                String instructionsURL = args[4];
                Client activeClient = new Client(baseURL,registerURL,assignURL,getServerURL, instructionsURL);
                activeClient.getManager().startRunners();

                // Temporary exit ----------------------
                Scanner in = new Scanner(System.in);
                String input;
                do {
                    input = in.nextLine();
                } while (!input.equals("exit"));
                activeClient.getManager().stopRunners();
                //----------------------------------------
            } else {
                System.out.println("No arguments");
            }
        } catch (ClientCommunicationException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
