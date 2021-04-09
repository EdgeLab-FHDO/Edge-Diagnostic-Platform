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
                  String assignURL, String getServerURL, String heartbeatURL,
                  String instructionsURL, String measurementsURL, String nextURL) throws ClientCommunicationException {
        manager = new ClientRunnerManager();
        try {
            ClientPlatformConnection connection = new ClientPlatformConnection(baseURL, registerURL,
                    assignURL, getServerURL, heartbeatURL ,instructionsURL, measurementsURL, nextURL);
            connection.register(this.getJsonRepresentation(false));
            Thread.sleep(100);
            ServerInformation server = connection.getServer(this.getJsonRepresentation(false));
            manager.configure(connection,getJsonRepresentation(true),server);
        } catch (JsonProcessingException | RESTClientException | InterruptedException e) {
            throw new ClientCommunicationException("Communication with diagnostics platform failed: ", e);
        }
    }

    private String getJsonRepresentation(boolean heartbeat) {
        String id = "diagnostics_client";
        if (heartbeat) {
            return "{\"id\":\"" + id + "\"}";
        }
        return "{\"id\":\"" + id + "\", \"heartBeatInterval\":2000}";
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
            String heartbeatURL = args[4];
            String instructionsURL = args[5];
            String measurementsURL = args[6];
            String nextURL = args[7];
            Client activeClient = new Client(baseURL,registerURL,assignURL,
                    getServerURL, heartbeatURL, instructionsURL, measurementsURL, nextURL);
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
                    "5. Heartbeat URL\n" +
                    "6. Instructions URL\n" +
                    "7. Measurements URL\n" +
                    "8. Next Instruction URL");
            System.exit(-1);
        } catch (ClientCommunicationException | RunnersNotConfiguredException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
