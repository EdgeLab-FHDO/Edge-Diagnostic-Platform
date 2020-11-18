package Application.Utilities;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageProcessor {
    public static Mat getImageMat(String filename) {
        return Imgcodecs.imread(filename);
    }

    public static void writeImage(String filename, Mat output) {
        Imgcodecs.imwrite(filename, output);
    }
 }