package DiagnosticsServer;

import DiagnosticsServer.Communication.Exception.ServerCommunicationException;
import DiagnosticsServer.Communication.RawServerData;
import DiagnosticsServer.Communication.ServerPlatformConnection;
import REST.Exception.RESTClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {

    private final int port;

    public Server(int port, String baseURL, String registerURL) throws ServerCommunicationException {
        try {
            this.port = port;
            ServerPlatformConnection connection = new ServerPlatformConnection(baseURL, registerURL);
            connection.register(this.getJsonRepresentation());
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

    private void startTCP() {
        String reading = "";
        try (
                ServerSocket serverSocket = new ServerSocket(this.port);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ){
            System.out.println("Waiting");
            while (!reading.equals("exit")) {
                reading = in.readLine();
                out.println(reading.equals("exit") ? "Bye" : reading);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                String baseURL = args[0];
                String registerURL = args[1];
                Server activeInstance = new Server(4444, baseURL,registerURL);
                System.out.println("starting TCP");
                activeInstance.startTCP();
            } else {
                System.out.println("No arguments");
            }
        } catch (ServerCommunicationException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
