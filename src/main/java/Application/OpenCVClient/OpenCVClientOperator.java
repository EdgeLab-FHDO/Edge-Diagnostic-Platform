package Application.OpenCVClient;

import Application.Utilities.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class OpenCVClientOperator {
    private Socket clientSocket;
    private BufferedReader in;
    private DataOutputStream out;
    private DetectMarker detector;
    private ObjectMapper mapper;

    //TODO allow fixed values to be set externally or update system to reduce reliance on fixed file names
    private final String fileName= "singlemarkerssource.png";
    private final String resultFileName = "detected.png";
    private final String fileExtension = "png";
    private Mat subject;
    private BufferedImage currentImage;

    public Semaphore ipLock;
    public Semaphore serverLock;

    //Server Communication Properties
    private boolean utilizeServer;
    private String ip;
    private int port;
    private boolean connected;

    public HeartBeatRunner beatRunner;
    public MasterCommunicationRunner masterCommunicationRunner;
    public ProcessingRunner processingRunner;

    private static OpenCVClientOperator instance = null;

    private OpenCVClientOperator() {
        mapper = new ObjectMapper();
        ipLock = new Semaphore(1);
        serverLock = new Semaphore(1);
        utilizeServer = false;
        detector = new DetectMarker();
        connected = false;
    }

    public static OpenCVClientOperator getInstance() {
        if (instance == null) {
            instance = new OpenCVClientOperator();
        }
        return instance;
    }

    public void startConnection() throws IOException, InterruptedException {
        try {
            ipLock.acquire();
            clientSocket = new Socket(ip, port);
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            connected = true;
        } finally {
            ipLock.release();
        }
    }

    public String sendImage() throws IOException {
        String resp = "";

        currentImage = ImageIO.read(new File(fileName));
        ImageIO.write(currentImage, fileExtension, clientSocket.getOutputStream());
        resp = in.readLine();

        return resp;
    }

    public void stopConnection() throws RemoteExecutionException {
        try {
            in.close();
            out.close();
            clientSocket.close();
            connected = false;
        } catch (IOException | NullPointerException e) {
            throw new RemoteExecutionException(e);
        }
    }

    public void setServerUtilization(boolean input) {
        try {
            serverLock.acquire();
            utilizeServer = input;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            serverLock.release();
        }

    }

    public void setupTcpConnection(String ipAddress) throws IllegalArgumentException, InterruptedException {
        String[] argument;
        try {
            ipLock.acquire();
            argument = ipAddress.split(":");
            if(argument.length == 2) {
                ip = argument[0];
                port = Integer.parseInt(argument[1]);
            } else {
                throw new IllegalArgumentException("Invalid argument");
            }
        } finally {
            ipLock.release();
        }
    }

    public void setupClientRunners(String[] args) throws IllegalArgumentException {
        String[] argument;
        String clientId = "";
        String masterUrl = "";
        String beatCommand = "";
        String getServerCommand = "";
        processingRunner = new ProcessingRunner();

        List<String> missingParameterList = new ArrayList<>(List.of("CLIENT_ID", "MASTER_URL", "BEAT_COMMAND", "GET_SERVER_COMMAND"));

        //TODO move beat and get server commands to other input methods such as configuration files
        for(int i=0; i<args.length; i++) {
            argument = args[i].split("=");
            if(argument.length == 2) {
                switch (argument[0]) {
                    case "CLIENT_ID":
                        clientId = argument[1];
                        missingParameterList.remove("CLIENT_ID");
                        break;
                    case "MASTER_URL":
                        masterUrl = argument[1];
                        missingParameterList.remove("MASTER_URL");
                        break;
                    case "BEAT_COMMAND":
                        beatCommand = argument[1];
                        missingParameterList.remove("BEAT_COMMAND");
                        break;
                    case "GET_SERVER_COMMAND":
                        getServerCommand = argument[1];
                        missingParameterList.remove("GET_SERVER_COMMAND");
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
        String beatBody =  "{\"id\" : \"" + clientId + "\"}";
        beatRunner = new HeartBeatRunner(beatUrl, beatBody);
        String masterCommunicationUrl = masterUrl + getServerCommand + clientId;
        masterCommunicationRunner = new MasterCommunicationRunner(masterCommunicationUrl);
    }

    public void detectMarkerInServer() throws RemoteExecutionException, IOException {
        String response;
        JsonNode node;
        try {
            if(!connected) {
                startConnection();
            }
            response = sendImage().replace("\\\\", "");

            node = mapper.readTree(response);
            Mat ids = OpenCVUtil.deserializeMat(node.get("ids").asText());
            List<Mat> corners = OpenCVUtil.deserializeMatList(node.get("corners").asText());

            detector.setIds(ids);
            detector.setCorners(corners);
        } catch (IOException | InterruptedException e) {
            stopConnection();
            throw new RemoteExecutionException(e);
        }
    }

    public void detectMarkerInClient() {
        detector.detect(subject);
    }

    public void markerDetection() {
        Mat result;
        subject = ImageProcessor.getImageMat(fileName);
        try {
            serverLock.acquire();
            if(utilizeServer) {
                detectMarkerInServer();
            } else {
                detectMarkerInClient();
            }
        } catch (RemoteExecutionException e) {
            detectMarkerInClient();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            serverLock.release();
        }

        result = detector.drawDetectedMarkers(subject);
        ImageProcessor.writeImage(resultFileName, result);
    }
}
