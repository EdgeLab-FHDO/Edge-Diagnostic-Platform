package Application.MarkerDetection.OpenCVServer;

import java.io.*;
import java.net.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import Application.Commons.CoreOperator;
import Application.Utilities.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.opencv.core.Mat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class OpenCVServerOperator implements CoreOperator {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataInputStream in;
    private PrintWriter out;
    private DetectMarker detector;

    private String serverId;
    private String serverIp;
    private int port;
    private boolean connected;

    private String fileName;

    private BufferedImage currentImage;

    public RegistrationRunner registrationRunner;
    public ServerRunner serverRunner;
    public HeartBeatRunner beatRunner;
    private String beatBody;

    private static OpenCVServerOperator instance = null;

    private OpenCVServerOperator() {
        //TODO allow fixed values to be set externally or update system to reduce reliance on fixed file names
        //TODO send actual connected status as heartbeat
        fileName= "read.png";
        detector = new DetectMarker();
        connected = false;
        beatBody = "";
        serverId = "";
        serverIp = "";
    }

    public void startConnection() throws IOException {
        if(!connected) {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new DataInputStream(clientSocket.getInputStream());
            connected = true;
            updateBeatBody();
        }
    }

    public void processing() throws IOException, IllegalArgumentException {
        currentImage = ImageIO.read(in);
        
        if(currentImage != null) {
            Mat subject = ImageProcessor.getBufferedImageMat(currentImage);

            detector.detect(subject);

            String ids = OpenCVUtil.serializeMat(detector.getIds());
            String corners = OpenCVUtil.serializeMatList(detector.getCorners());
            String result = "";

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode resultObject = mapper.createObjectNode();
            resultObject.put("ids", ids);
            resultObject.put("corners", corners);

            result = mapper.writeValueAsString(resultObject);

            out.println(result);
        } else {
            clientSocket.sendUrgentData(1);
        }
    }

    public void stopConnection() throws RemoteExecutionException {
        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
            connected = false;
            updateBeatBody();
        } catch (IOException | NullPointerException e) {
            throw new RemoteExecutionException(e);
        }
    }

    public static OpenCVServerOperator getInstance() {
        if (instance == null) {
            instance = new OpenCVServerOperator();
        }
        return instance;
    }

    public void setupServerRunners(String[] args) throws IllegalArgumentException {
        String[] argument;
        String masterUrl = "";
        String beatCommand = "";
        String registerCommand = "";
        serverRunner = new ServerRunner();
        int interval = 1000;
        int totalNetwork = 0;
        int totalResource = 0;
        int location = 0;

        List<String> missingParameterList = new ArrayList<>(List.of("SERVER_ID", "SERVER_IP", "MASTER_URL", "REGISTER_COMMAND", "BEAT_COMMAND", "PORT", "TOTAL_NETWORK", "TOTAL_RESOURCE", "LOCATION")); // Connected has default value of false

        //TODO move beat command to other input methods such as configuration files
        for (String arg : args) {
            argument = arg.split("=");
            if (argument.length == 2) {
                switch (argument[0]) {
                    case "SERVER_ID":
                        serverId = argument[1];
                        missingParameterList.remove("SERVER_ID");
                        break;
                    case "SERVER_IP":
                        serverIp = argument[1];
                        missingParameterList.remove("SERVER_IP");
                        break;
                    case "MASTER_URL":
                        masterUrl = argument[1];
                        missingParameterList.remove("MASTER_URL");
                        break;
                    case "REGISTER_COMMAND":
                        registerCommand = argument[1];
                        missingParameterList.remove("REGISTER_COMMAND");
                        break;
                    case "BEAT_COMMAND":
                        beatCommand = argument[1];
                        missingParameterList.remove("BEAT_COMMAND");
                        break;
                    case "PORT":
                        port = Integer.parseInt(argument[1]);
                        missingParameterList.remove("PORT");
                        break;
                    case "TOTAL_RESOURCE":
                        totalResource = Integer.parseInt(argument[1]);
                        missingParameterList.remove("TOTAL_RESOURCE");
                        break;
                    case "TOTAL_NETWORK":
                        totalNetwork = Integer.parseInt(argument[1]);
                        missingParameterList.remove("TOTAL_NETWORK");
                        break;
                    case "LOCATION":
                        location = Integer.parseInt(argument[1]);
                        missingParameterList.remove("LOCATION");
                        break;
                    case "INTERVAL":
                        interval = Integer.parseInt(argument[1]);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid argument");
                }
            }
        }

        if(missingParameterList.size() > 0) {
            throw new IllegalArgumentException("Missing Parameter: " + String.join(",", missingParameterList));
        }

        String registrationUrl = masterUrl +registerCommand;
        //TODO implement something to replace hardcoded value
        String registrationBody = "{\"id\": \"" + serverId + "\""
                + ", \"ipAddress\": \"" + serverIp + " : " + port + "\""
                + ", \"connected\": " + true
                + ", \"totalResource\": " + totalResource
                + ", \"totalNetwork\": " + totalNetwork
                + ", \"location\": " + location
                + ", \"heartBeatInterval\": " + (2*interval) + "}";
        String beatUrl = masterUrl + beatCommand;

        registrationRunner = new RegistrationRunner(instance, registrationUrl, registrationBody);
        updateBeatBodyContent();
        beatRunner = new HeartBeatRunner(beatUrl, beatBody, interval);
        beatRunner.pause();
    }

    private void updateBeatBodyContent() {
        beatBody =  "{\"id\": \"" + serverId + "\"}";
    }

    private void updateBeatBody() {
        updateBeatBodyContent();
        beatRunner.beater.setBody(beatBody);
    }

    @Override
    public void startMasterCommunication() {
        beatRunner.resume();
    }
}
