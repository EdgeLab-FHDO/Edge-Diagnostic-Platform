package DiagnosticsClient;

import DiagnosticsClient.Communication.ClientPlatformConnection;
import DiagnosticsClient.Communication.Exception.ClientCommunicationException;
import DiagnosticsClient.Communication.Exception.TCP.ServerNotSetUpException;
import DiagnosticsClient.Communication.Exception.TCP.TCPConnectionException;
import DiagnosticsClient.Communication.ServerInformation;
import REST.Exception.RESTClientException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private ServerInformation server;

    public Client(String baseURL, String registerURL, String assignURL, String getServerURL) throws ClientCommunicationException {
        try {
            ClientPlatformConnection connection = new ClientPlatformConnection(baseURL, registerURL, assignURL, getServerURL);
            connection.register(this.getJsonRepresentation());
            this.server = connection.getServer(this.getJsonRepresentation());
        } catch (JsonProcessingException | RESTClientException e) {
            throw new ClientCommunicationException("Communication with diagnostics platform failed: ", e);
        }
    }

    private String getJsonRepresentation() {
        String id = "diagnostics_client";
        return  "{\"id\":\"" + id + "\"}";
    }

    public void connectTCP() throws TCPConnectionException {
        String reading = "";
        if (this.server == null) throw new ServerNotSetUpException("Client is not connected to the server");
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
        } catch (IOException e) {
            throw new TCPConnectionException("TCP Connection failed: ",e);
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
        } catch (ClientCommunicationException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
