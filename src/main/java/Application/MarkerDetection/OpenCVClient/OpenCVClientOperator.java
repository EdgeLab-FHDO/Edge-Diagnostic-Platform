package Application.MarkerDetection.OpenCVClient;

import Application.Commons.CoreOperator;
import Application.Utilities.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

public class OpenCVClientOperator implements CoreOperator {
    private Socket clientSocket;
    private BufferedReader in;
    private DataOutputStream out;
    private DetectMarker detector;
    private ObjectMapper mapper;
    private String clientId;

    //TODO allow fixed values to be set externally or update system to reduce reliance on fixed file names
    private final String fileName= "singlemarkerssource.png";
    private final String resultFileName = "detected.png";
    private final String fileExtension = "png";
    private Mat subject;
    private BufferedImage currentImage;
    private List<String> reportQueue;

    public Semaphore ipLock;
    public Semaphore serverLock;
    public Semaphore reportLock;

    //Server Communication Properties
    public boolean utilizeServer;
    private String ip;
    private int port;
    public boolean connected;

    public RegistrationRunner registrationRunner;
    public HeartBeatRunner beatRunner;
    public MasterCommunicationRunner masterCommunicationRunner;
    public ProcessingRunner processingRunner;
    public LatencyReporterRunner reportRunner;

    private static OpenCVClientOperator instance = null;

    private boolean debugMode = true; //TODO make this available when starting application

    private OpenCVClientOperator() {
        mapper = new ObjectMapper();
        ipLock = new Semaphore(1);
        serverLock = new Semaphore(1);
        reportLock = new Semaphore(1);
        utilizeServer = false;
        detector = new DetectMarker();
        connected = false;
        reportQueue = new ArrayList<>();
        clientId = "";
    }

    public void startMasterCommunication() {
        beatRunner.resume();
        masterCommunicationRunner.resume();
    }

    public static OpenCVClientOperator getInstance() {
        if (instance == null) {
            instance = new OpenCVClientOperator();
        }
        return instance;
    }

    public void startConnection() throws IOException, InterruptedException {
        if(!connected) {
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
        String masterUrl = "";
        String registerCommand = "";
        String beatCommand = "";
        String getServerCommand = "";
        String latencyReportCommand = "";
        processingRunner = new ProcessingRunner();
        int interval = 1000;

        List<String> missingParameterList = new ArrayList<>(List.of("CLIENT_ID", "MASTER_URL", "REGISTER_COMMAND", "BEAT_COMMAND", "GET_SERVER_COMMAND", "LATENCY_REPORT_COMMAND"));

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
                    case "REGISTER_COMMAND":
                        registerCommand = argument[1];
                        missingParameterList.remove("REGISTER_COMMAND");
                        break;
                    case "BEAT_COMMAND":
                        beatCommand = argument[1];
                        missingParameterList.remove("BEAT_COMMAND");
                        break;
                    case "GET_SERVER_COMMAND":
                        getServerCommand = argument[1];
                        missingParameterList.remove("GET_SERVER_COMMAND");
                        break;
                    case "LATENCY_REPORT_COMMAND":
                        latencyReportCommand = argument[1];
                        missingParameterList.remove("LATENCY_REPORT_COMMAND");
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
        String registrationBody = "{\"id\" : \"" + clientId + "\"}";
        String beatUrl = masterUrl + beatCommand;
        String beatBody =  "{\"id\" : \"" + clientId + "\"}";
        String masterCommunicationUrl = masterUrl + getServerCommand + clientId;
        String reportUrl = masterUrl + latencyReportCommand;

        registrationRunner = new RegistrationRunner(instance, registrationUrl, registrationBody);
        beatRunner = new HeartBeatRunner(beatUrl, beatBody, interval);
        beatRunner.pause();
        masterCommunicationRunner = new MasterCommunicationRunner(masterCommunicationUrl);
        masterCommunicationRunner.pause();
        reportRunner = new LatencyReporterRunner(reportUrl);
    }

    public void detectMarkerInServer() throws RemoteExecutionException {
        String response;
        JsonNode node;
        try {
            startConnection();
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

    //TODO move this to a latency reporter utility
    public void queueLatencyReport(String location, boolean serverUsage, long startTime, long endTime) throws JsonProcessingException, InterruptedException {
        String timestamp = new SimpleDateFormat("YYYY-mm-dd_HH:mm:ss").format(new Date());
        long latency = (endTime-startTime)/1000000;
        String reportedIp="";
        if(debugMode) {
            System.out.println("[" + timestamp + "] Detected in " + location + " Execution Time: " + latency + "ms");
        }
        try {
            ipLock.acquire();
            reportedIp = ip;
        } finally {
            ipLock.release();
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode resultObject = mapper.createObjectNode();
        resultObject.put("timestamp", timestamp);
        resultObject.put("latency", latency);
        resultObject.put("use_server", serverUsage);
        resultObject.put("server_ip", reportedIp);

        String result = mapper.writeValueAsString(resultObject);

        try {
            reportLock.acquire();
            reportQueue.add(result);
        } finally {
            reportLock.release();
        }
    }

    public String getReportBody() {
        String returnedResult = "";
        try {
            reportLock.acquire();
            if(!reportQueue.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode result = mapper.createObjectNode();
                String reportFileName = "report_" + clientId + ".txt";

                result.put("path", reportFileName);
                result.put("content", String.join("", reportQueue));
                returnedResult = mapper.writeValueAsString(result);
                reportQueue.clear();
            }
        } catch (InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            reportLock.release();
        }
        return returnedResult;
    }

    public void markerDetection() throws JsonProcessingException, InterruptedException {
        Mat result;
        long startTime = System.nanoTime();
        String location = "Client";
        subject = ImageProcessor.getImageMat(fileName);
        boolean serverUsage = false;

        try {
            serverLock.acquire();
            serverUsage = utilizeServer;

            if(utilizeServer) {
                detectMarkerInServer();
                location = "Server";
            } else {
                detectMarkerInClient();
            }
        } catch (RemoteExecutionException e) {
            detectMarkerInClient();
            serverUsage = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            serverLock.release();
        }

        result = detector.drawDetectedMarkers(subject);
        ImageProcessor.writeImage(resultFileName, result);

        long endTime = System.nanoTime();

        queueLatencyReport(location, serverUsage, startTime, endTime);
    }
}
