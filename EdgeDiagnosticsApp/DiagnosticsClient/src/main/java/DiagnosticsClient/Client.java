package DiagnosticsClient;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {


    public static void main(String[] args) {
        String reading = "";
        if (args.length > 0) {
            String hostName = args[0];
            int port = Integer.parseInt(args[1]);
            try (
                    Socket clientSocket = new Socket(hostName,port);
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

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("nothing");
        }
    }
}
