package Application.Utilities;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageProcessor {
    public static Mat getImageMat(String filename) {
        return Imgcodecs.imread(filename);
    }

    public static Mat getBufferedImageMat(BufferedImage image) throws IllegalArgumentException {
        //TODO research if there are other possibly unhandled image types
        Mat result;
        int type;
        if(image.getType() == BufferedImage.TYPE_3BYTE_BGR) {
            type = CvType.CV_8UC3;
        } else if(image.getType() == BufferedImage.TYPE_4BYTE_ABGR) {
            type = CvType.CV_8UC4;
        } else if(image.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            type = CvType.CV_8UC1;
        } else {
            throw new IllegalArgumentException("Illegal image stream format");
        }

        result = new Mat(image.getHeight(), image.getWidth(), type);
        result.put(0, 0, ((DataBufferByte) image.getRaster().getDataBuffer()).getData());

        return result;
    }

    public static void writeImage(String filename, Mat output) {
        Imgcodecs.imwrite(filename, output);
    }
 }