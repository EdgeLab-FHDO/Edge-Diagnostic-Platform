package DiagnosticsClient;

import DiagnosticsClient.Communication.ClientPlatformConnection;
import DiagnosticsClient.Communication.Exception.JSONException;
import DiagnosticsClient.Communication.ServerInformation;
import REST.Exception.RESTClientException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private ServerInformation server;

    public Client(String baseURL, String registerURL, String assignURL, String getServerURL) throws RESTClientException, JSONException {
        ClientPlatformConnection connection = new ClientPlatformConnection(baseURL, registerURL, assignURL, getServerURL);
        connection.register(this.getJsonRepresentation());
        this.server = connection.getServer(this.getJsonRepresentation());
    }

    private String getJsonRepresentation() {
        String id = "diagnostics_client";
        return  "{\"id\":\"" + id + "\"}";
    }

    public void connectTCP() throws Exception {
        String reading = "";
        if (this.server == null) throw new Exception("Server not set up"); //TODO: MAKE CUSTOM EXCEPTION
        try (
                Socket clientSocket = new Socket(server.getIpAddress(),server.getPort());
                Scanner stdin = new Scanner(System.in);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            while (!reading.equals("exit")) {
                reading = stdin.nextLine();
                System.out.println("Sent: " + reading);
                out.println(reading);
                System.out.println("The server said: " + in.readLine());
            }
        }
    }

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                String baseURL = args[0];
                String registerURL = args[1];
                String assignURL = args[2];
                String getServerURL = args[3];
                Client activeClient = new Client(baseURL,registerURL,assignURL,getServerURL);
                activeClient.connectTCP();
            } else {
                System.out.println("No arguments");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
