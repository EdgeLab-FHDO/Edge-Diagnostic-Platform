package DiagnosticsServer;

import DiagnosticsServer.Communication.Exception.ServerCommunicationException;
import DiagnosticsServer.Communication.RawServerData;
import DiagnosticsServer.Communication.ServerPlatformConnection;
import Communication.Exception.RESTClientException;
import RunnerManagement.Exception.RunnersNotConfiguredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Server {

    private final ServerRunnerManager manager;
    private final int port;

    public Server(int port, String baseURL, String registerURL, String instructionsURL) throws ServerCommunicationException {
        this.manager = new ServerRunnerManager();
        this.port = port;
        try {

            ServerPlatformConnection connection = new ServerPlatformConnection(baseURL, registerURL, instructionsURL);
            connection.register(this.getJsonRepresentation());
            manager.configure(connection,getJsonRepresentation(),port);
        } catch (RESTClientException | UnknownHostException | JsonProcessingException e) {
            throw new ServerCommunicationException("Communication with Diagnostics Platform failed: ", e);
        }
    }

    private String getIP() throws UnknownHostException {
        InetAddress myIP = InetAddress.getLocalHost();
        return myIP.getHostAddress();
    }

    private String getJsonRepresentation() throws UnknownHostException, JsonProcessingException {
        String id = "diagnostics_server";
        String completeAddress = this.getIP() + ":" + this.port;
        RawServerData rawServerData = new RawServerData(id, completeAddress);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(rawServerData);
    }

    public ServerRunnerManager getManager() {
        return manager;
    }

    public static void main(String[] args) {
        try {

            String baseURL = args[0];
            String registerURL = args[1];
            String instructionsURL = args[2];
            Server activeInstance = new Server(4444, baseURL,registerURL, instructionsURL);
            System.out.println("Starting Server");
            activeInstance.getManager().startRunners();

            // Temporary exit ----------------------
            Scanner in = new Scanner(System.in);
            String input;
            do {
                input = in.nextLine();
            } while (!input.equals("exit"));
            activeInstance.getManager().stopRunners();
            //----------------------------------------

        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("To use the program the following arguments are expected:\n" +
                    "1. Base URL\n" +
                    "2. Register URL\n" +
                    "3. Instructions URL\n");
            System.exit(-1);
        } catch (ServerCommunicationException | RunnersNotConfiguredException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
