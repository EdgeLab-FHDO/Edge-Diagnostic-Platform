package Application.OpenCVServer;

import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.net.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import Application.Utilities.DetectMarker;
import Application.Utilities.HeartBeatRunner;
import Application.Utilities.ImageProcessor;
import Application.Utilities.OpenCVUtil;
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

    public OpenCVServerOperator() {
        fileName= "read.png";
    }

    public void startConnection() {
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new DataInputStream(clientSocket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // change name to something more appropriate
    public void standbyForConnection() {
        //if input is faulty, timeout and continue
        try {
            currentImage = ImageIO.read(ImageIO.createImageInputStream(clientSocket.getInputStream()));
            ImageIO.write(currentImage, "png", new File(fileName));
            Mat subject = ImageProcessor.getImageMat(fileName);

            detector = new DetectMarker();
            detector.detect(subject);
            String ids = OpenCVUtil.serializeMat(detector.getIds());
            String corners = OpenCVUtil.serializeMatList(detector.getCorners());
            String result = "";

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode resultObject = mapper.createObjectNode();
            resultObject.put("ids", ids);
            resultObject.put("corners", corners);
            System.out.println(ids);
            System.out.println(corners);

            result = mapper.writeValueAsString(resultObject);

            out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static OpenCVServerOperator getInstance() {
        if (instance == null) {
            instance = new OpenCVServerOperator();
        }
        return instance;
    }

    public void setupServerRunners(String args[]) throws IllegalArgumentException {
        String[] argument;
        String masterUrl = ""; // for test: http://host.docker.internal:4567/
        String beatCommand = ""; // for test: client/register
        String serverId = "";
        String serverIp = "";
        boolean connected = false;

        serverRunner = new ServerRunner();

        int missingParameter = 0;
        String missingParameterList = "";

        for(int i=0; i<args.length; i++) {
            argument = args[i].split("=");
            if(argument.length == 2) {
                switch (argument[0]) {
                    case "SERVER_ID":
                        serverId = argument[1];
                        break;
                    case "SERVER_IP":
                        serverIp = argument[1];
                        break;
                    case "MASTER_URL":
                        masterUrl = argument[1];
                        break;
                    case "BEAT_COMMAND":
                        beatCommand = argument[1];
                        break;
                    case "CONNECTED":
                        connected = Boolean.parseBoolean(argument[1]);
                        break;
                    case "PORT":
                        port = Integer.parseInt(argument[1]);
                        break;
                    default:
                        throw new IllegalArgumentException("Invaild argument");
                }
            }
        }
        if(serverId.trim().isEmpty()) {
            missingParameter++;
            missingParameterList = missingParameterList + "SERVER_ID,";
        }
        if(serverIp.trim().isEmpty()) {
            missingParameter++;
            missingParameterList = missingParameterList + "SERVER_IP,";
        }
        if(masterUrl.trim().isEmpty()) {
            missingParameter++;
            missingParameterList = missingParameterList + "MASTER_URL,";
        }
        if(beatCommand.trim().isEmpty()) {
            missingParameter++;
            missingParameterList = missingParameterList + "BEAT_COMMAND,";
        }
        if(port == 0) {
            missingParameter++;
            missingParameterList = missingParameterList + "PORT";
        }
        if(missingParameter > 0) {
            throw new IllegalArgumentException("Missing Parameter: " + missingParameterList);
        }

        String beatUrl = masterUrl + beatCommand;
        String beatBody =  "{" +
                "    \"id\": \"" + serverId + "\"," +
                "    \"ipAddress\": \"" + serverIp + "\"," +
                "    \"connected\": " + connected + "," +
                "    \"port\": " + port +
                "}";
        beatRunner = new HeartBeatRunner(beatUrl, beatBody);
    }
}
