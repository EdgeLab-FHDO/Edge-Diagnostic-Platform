package Application.Utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.CvType;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.JsonNode;

public class OpenCVUtil {
    public static String serializeMat(Mat subject) throws InvalidObjectException, JsonProcessingException {
        int type = subject.type();
        int rows = subject.rows();
        int cols = subject.cols();
        int elemSize = (int)subject.elemSize();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        ArrayNode dataNode;
        String returnedResult = "";

        result.put("type", type);
        result.put("rows", rows);
        result.put("cols", cols);

        if(type == CvType.CV_32S || type == CvType.CV_32SC2 || type == CvType.CV_32SC3 || type == CvType.CV_16S) {
            int[] data = new int[cols * rows * elemSize];
            subject.get(0, 0, data);
            dataNode = mapper.valueToTree(data);
        } else if(type == CvType.CV_32F || type == CvType.CV_32FC2) {
            float[] data = new float[cols * rows * elemSize];
            subject.get(0, 0, data);
            dataNode = mapper.valueToTree(data);
        } else if(type == CvType.CV_64F || type == CvType.CV_64FC2) {
            double[] data = new double[cols * rows * elemSize];
            subject.get(0, 0, data);
            dataNode = mapper.valueToTree(data);
        } else if(type == CvType.CV_8U ) {
            byte[] data = new byte[cols * rows * elemSize];
            subject.get(0, 0, data);
            dataNode = mapper.valueToTree(data);
        } else {
            throw new InvalidObjectException("Invalid OpenCV Type");
        }

        result.set("data", dataNode);
        returnedResult = mapper.writeValueAsString(result);

        return returnedResult;
    }

    public static String serializeMatList(List<Mat> subject) throws InvalidObjectException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode result = mapper.createArrayNode();
        String returnedResult = "";

        for (Mat content : subject) {
            result.add(serializeMat(content));
        }
        returnedResult = mapper.writeValueAsString(result);

        return returnedResult;
    }
    
    public static Mat deserializeMat(String subject) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        int type=0;
        int rows=0;
        int cols=0;

        JsonNode node = mapper.readTree(subject);
        type = node.get("type").asInt();
        rows = node.get("rows").asInt();
        cols = node.get("cols").asInt();

        JsonNode dataArray = node.get("data");
        int dataSize = dataArray.size();
        int i = 0;
        Iterator<JsonNode> iterator = dataArray.iterator();

        Mat result = new Mat(rows, cols, type);

        if(type == CvType.CV_32S || type == CvType.CV_32SC2 || type == CvType.CV_32SC3 || type == CvType.CV_16S) {
            int[] data = new int[dataSize];
            while(iterator.hasNext()) {
                data[i] = iterator.next().asInt();
                i++;
            }
            result.put(0,0, data);
        } else if(type == CvType.CV_32F || type == CvType.CV_32FC2) {
            float[] data = new float[dataSize];
            while(iterator.hasNext()) {
                data[i] = (float)iterator.next().asDouble();
                i++;
            }
            result.put(0,0, data);
        } else if(type == CvType.CV_64F || type == CvType.CV_64FC2) {
            double[] data = new double[dataSize];
            while(iterator.hasNext()) {
                data[i] = iterator.next().asDouble();
                i++;
            }
            result.put(0,0, data);
        } else if(type == CvType.CV_8U ) {
            byte[] data = new byte[dataSize];
            while(iterator.hasNext()) {
                data[i] = (byte)iterator.next().asInt();
                i++;
            }
            result.put(0,0, data);
        } else {
            throw new InvalidObjectException("Invalid OpenCV Type");
        }

        return result;
    }

    public static List<Mat> deserializeMatList(String subject) throws IOException {
        List<Mat> result = new ArrayList<>();
        String[] elements = subject.replace("[\"", "").replace("\"]", "").split("\",\"");
        for(String element : elements) {
            result.add(deserializeMat(element));
        }

        return result;
    }

    public static void initOpenCVSharedLibrary() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}