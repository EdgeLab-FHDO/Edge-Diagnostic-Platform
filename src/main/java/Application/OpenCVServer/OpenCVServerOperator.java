package Application.OpenCVServer;

import java.io.*;
import java.net.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import Application.Utilities.DetectMarker;
import Application.Utilities.HeartBeatRunner;
import Application.Utilities.ImageProcessor;
import Application.Utilities.OpenCVUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.opencv.core.Mat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class OpenCVServerOperator {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataInputStream in;
    private PrintWriter out;
    private DetectMarker detector;
    private int port;

    private String fileName;

    private BufferedImage currentImage;

    public ServerRunner serverRunner;
    public HeartBeatRunner beatRunner;

    private static OpenCVServerOperator instance = null;

    private OpenCVServerOperator() {
        //TODO allow fixed values to be set externally or update system to reduce reliance on fixed file names
        fileName= "read.png";
        detector = new DetectMarker();
    }

    public void startConnection() throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new DataInputStream(clientSocket.getInputStream());
    }

    public void processing() throws IOException, IllegalArgumentException {
        currentImage = ImageIO.read(ImageIO.createImageInputStream(clientSocket.getInputStream()));
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
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
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
        String serverId = "";
        String serverIp = "";
        boolean connected = false;

        serverRunner = new ServerRunner();

        List<String> missingParameterList = new ArrayList<>(List.of("SERVER_ID", "SERVER_IP", "MASTER_URL", "BEAT_COMMAND", "PORT")); // Connected has default value of false

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
                    case "BEAT_COMMAND":
                        beatCommand = argument[1];
                        missingParameterList.remove("BEAT_COMMAND");
                        break;
                    case "CONNECTED":
                        connected = Boolean.parseBoolean(argument[1]);
                        break;
                    case "PORT":
                        port = Integer.parseInt(argument[1]);
                        missingParameterList.remove("PORT");
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid argument");
                }
            }
        }

        if(missingParameterList.size() > 0) {
            throw new IllegalArgumentException("Missing Parameter: " + String.join(",", missingParameterList));
        }

        String beatUrl = masterUrl + beatCommand;
        String beatBody =  "{\"id\": \"" + serverId + "\", \"ipAddress\": \"" + serverIp + "\", \"connected\": " + connected + "}";
        beatRunner = new HeartBeatRunner(beatUrl, beatBody);
    }
}
