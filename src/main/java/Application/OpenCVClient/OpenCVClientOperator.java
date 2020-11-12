package Application.OpenCVClient;

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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.opencv.core.Mat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.concurrent.Semaphore;

public class OpenCVClientOperator {
    private Socket clientSocket;
    private BufferedReader in;
    private DataOutputStream out;
    private DetectMarker detector;
    private ObjectMapper mapper;

    private String fileName = "singlemarkerssource.png";
    private String resultFileName = "detected.png";
    private String fileExtension = "png";
    private Mat subject;
    private BufferedImage currentImage;

    public Semaphore ipLock;
    public Semaphore serverLock;

    //Server Communication Properties
    private int utilizeServer;
    private String ip;
    private int port;

    public HeartBeatRunner beatRunner;
    public MasterCommunicationRunner masterCommunicationRunner;
    public ProcessingRunner processingRunner;

    private static OpenCVClientOperator instance = null;

    public OpenCVClientOperator() {
        mapper = new ObjectMapper();
        ipLock = new Semaphore(1);
        serverLock = new Semaphore(1);
        utilizeServer = 0;
    }

    public static OpenCVClientOperator getInstance() {
        if (instance == null) {
            instance = new OpenCVClientOperator();
        }
        return instance;
    }

    public void startConnection() {
        try {
            ipLock.acquire();
            clientSocket = new Socket(ip, port);
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ipLock.release();
        }
    }

    public String sendImage() throws IOException {
        String resp = "";
        Mat ids;
        currentImage = ImageIO.read(new File(fileName));
        ImageIO.write(currentImage, fileExtension, clientSocket.getOutputStream());
        System.out.println("sendImage3");
        resp = in.readLine();

        return resp;
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public String getFileName() {
        return fileName;
    }

    public int useServer() {
        return utilizeServer;
    }

    public void setServerUtilization(int input) {
        try {
            serverLock.acquire();
            utilizeServer = input;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            serverLock.release();
        }

    }

    public void setup(String[] args) throws IllegalArgumentException, ArrayIndexOutOfBoundsException, InterruptedException {
        String[] argument;
        // make a rest request to the master to ask for server and port for server
        // do locally if no response
        try {
            ipLock.acquire();
            for(int i=0; i<args.length; i++) {
                argument = args[i].split("=");
                switch(argument[0]) {
                    case "PORT":
                        port = Integer.parseInt(argument[1]);
                        System.out.println("port :" +port);
                        break;
                    case "IP":
                        ip = argument[1];
                        System.out.println("ip :" +ip);
                        break;
                    default :
                        throw new IllegalArgumentException("Invaild argument");
                }
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw e;
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            ipLock.release();
        }
    }

    public void setupClientRunners(String args[]) throws IllegalArgumentException {
        String[] argument;
        String clientId = ""; // for test: client13
        String masterUrl = ""; // for test: http://host.docker.internal:4567/
        String beatCommand = ""; // for test: client/register
        String getServerCommand = ""; // for test: client/get_node/
        processingRunner = new ProcessingRunner();

        int missingParameter = 0;
        String missingParameterList = "";

        for(int i=0; i<args.length; i++) {
            System.out.println(args[i]);
            argument = args[i].split("=");
            if(argument.length == 2) {
                switch (argument[0]) {
                    case "CLIENT_ID":
                        clientId = argument[1];
                        break;
                    case "MASTER_URL":
                        masterUrl = argument[1];
                        break;
                    case "BEAT_COMMAND":
                        beatCommand = argument[1];
                        break;
                    case "GET_SERVER_COMMAND":
                        getServerCommand = argument[1];
                        break;
                    default:
                        throw new IllegalArgumentException("Invaild argument");
                }
            }
        }
        if(clientId.trim().isEmpty()) {
            missingParameter++;
            missingParameterList = missingParameterList + "CLIENT_ID,";
        }
        if(masterUrl.trim().isEmpty()) {
            missingParameter++;
            missingParameterList = missingParameterList + "MASTER_URL,";
        }
        if(beatCommand.trim().isEmpty()) {
            missingParameter++;
            missingParameterList = missingParameterList + "BEAT_COMMAND,";
        }
        if(getServerCommand.trim().isEmpty()) {
            missingParameter++;
            missingParameterList = missingParameterList + "GET_SERVER_COMMAND";
        }
        if(missingParameter > 0) {
            throw new IllegalArgumentException("Missing Parameter: " + missingParameterList);
        }
        String beatUrl = masterUrl + beatCommand;
        String beatBody =  "{" +
                "\"id\" : \"" + clientId + "\"" +
                "}";
        beatRunner = new HeartBeatRunner(beatUrl, beatBody);
        String masterCommunicationUrl = masterUrl + getServerCommand + clientId;
        masterCommunicationRunner = new MasterCommunicationRunner(masterCommunicationUrl);
    }

    public void detectMarkerInServer() throws JsonProcessingException, InvalidObjectException, IOException {
        System.out.println("detectMarkerInServer");
        // connect to server and detect marker
        String response;
        JsonNode node;

        startConnection();
        response = sendImage().replace("\\\\", "");

        node = mapper.readTree(response);
        Mat ids = OpenCVUtil.deserializeMat(node.get("ids").asText());
        List<Mat> corners = OpenCVUtil.deserializeMatList(node.get("corners").asText());
        System.out.println("Marker Drawn with remote informations");

        detector.setIds(ids);
        detector.setCorners(corners);
    }

    public void detectMarkerInClient() {
        //System.out.println("detectMarkerInClient");
        detector.detect(subject);
        System.out.println("Marker Drawn locally");
    }

    private void handleServerDetectException(Exception e) {
        e.printStackTrace();
        detectMarkerInClient();
    }

    public void markerDetection() {
        Mat result;
        subject = ImageProcessor.getImageMat(fileName);
        detector = new DetectMarker();
        try {
            serverLock.acquire();
            if(1 == utilizeServer) {
                detectMarkerInServer();
            } else {
                detectMarkerInClient();
            }
        } catch (InvalidObjectException e) {
            handleServerDetectException(e);
        } catch (JsonProcessingException e) {
            handleServerDetectException(e);
        } catch (IOException e) {
            handleServerDetectException(e);
        } catch (Exception e) {
            handleServerDetectException(e);
        } finally {
            serverLock.release();
        }

        result = detector.drawDetectedMarkers(subject);
        ImageProcessor.writeImage(resultFileName, result);
    }
}
